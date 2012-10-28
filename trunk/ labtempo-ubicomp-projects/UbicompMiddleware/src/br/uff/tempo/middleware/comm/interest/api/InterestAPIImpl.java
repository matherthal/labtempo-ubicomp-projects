package br.uff.tempo.middleware.comm.interest.api;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import ufrj.coppe.lcp.repa.RepaMessage;
import android.util.Log;
import br.uff.tempo.middleware.comm.common.Callable;
import br.uff.tempo.middleware.comm.common.InterestAPI;
import br.uff.tempo.middleware.management.ResourceAgentNS;

public class InterestAPIImpl implements InterestAPI {
	
	private static final String INDEX_SEPARATOR = "://:";
	
	private static ConcurrentHashMap<String, BlockingQueue<Callable>> myInterests = new ConcurrentHashMap<String, BlockingQueue<Callable>>();
	
	private static ConcurrentHashMap<String, BlockingQueue<Callable>> agentsInterestsIndex = new ConcurrentHashMap<String, BlockingQueue<Callable>>();
	
	private static ConcurrentHashMap<String, REPAComm> repaCommMap = new ConcurrentHashMap<String, InterestAPIImpl.REPAComm>();
	
	private static InterestAPIImpl api;
	
	private CommREPAD commREPAD;
	
	public synchronized static InterestAPIImpl getInstance() {
		if (api == null) {
			api = new InterestAPIImpl(); 
		}
		return api;
	}
	
	public InterestAPIImpl() {
		commREPAD = new CommREPAD() {
			@Override
			public String serve(RepaMessageContent repaMessageContent) {
				return dispatchRepaMessage(repaMessageContent);
			}
		};
	}

	private static String dispatchRepaMessage(RepaMessageContent messageContent) {
		if (messageContent.isReply()) {
			Log.d("SmartAndroid", String.format("Response %s was received to %s", messageContent.getContent(), messageContent.getRaNSTo()));
			repaCommMap.get(messageContent.getId()).notifyResponseReceived(messageContent.getContent());
			return null;
		}
		
		BlockingQueue<Callable> callbacks;
		ResourceAgentNS raNSFrom = null;
		if (messageContent.getRaNSTo() == null) {
			callbacks = myInterests.get(messageContent.getInterest());
		} else {
			String indexKey = messageContent.getRaNSTo().getRans() + INDEX_SEPARATOR + messageContent.getInterest();
			callbacks = agentsInterestsIndex.get(indexKey);
			raNSFrom = messageContent.getRaNSFrom();
		}
		
		Log.d("SmartAndroid", "Starting calling callbacks of rans: " + messageContent.getRaNSTo());
		String callbackResult = null;
		for (Callable callback : callbacks) {
			callbackResult = callback.call(raNSFrom, messageContent.getInterest(), messageContent.getContent());
		}
		Log.d("SmartAndroid", "End of calling callback of rans: " + messageContent.getRaNSTo());
		
		return callbackResult;
	}
	
	@Override
	public void registerInterest(String rans) throws Exception {
		this.commREPAD.registerInterest(rans);
	}
	
	@Override
	public void registerInterest(String rans, String interest, Callable callback) throws Exception {
		this.addInterest(interest, callback); // Adding to default interests map

		// Adding to specific agents interests map index
		String indexKey = rans + INDEX_SEPARATOR + interest;
		if (agentsInterestsIndex.get(indexKey) == null) {
			agentsInterestsIndex.put(indexKey, new LinkedBlockingQueue<Callable>(Arrays.asList(callback))); // first callback
		} else {
			agentsInterestsIndex.get(indexKey).add(callback); // add new callback of the same interest
		}
		Log.d("SmartAndroid", "agentsInterestsIndex - Adding a new callback of interest: " + interest);
		
		this.commREPAD.registerInterest(interest);
		
		Log.d("SmartAndroid", "REPA registerInterest was called with interest: " + interest);
	}	

	@Override
	public void registerInterest(String interest, Callable callback) throws Exception {
		this.addInterest(interest, callback); // Adding to default interests map

		this.commREPAD.registerInterest(interest);
		
		Log.d("SmartAndroid", "REPA registerInterest was called with interest: " + interest);
	}

	@Override
	public String sendMessage(ResourceAgentNS raNSFrom, ResourceAgentNS raNSTo, String interest, String message) throws Exception {
		String commId = UUID.randomUUID().toString();
		RepaMessageContent repaMessageContent = new RepaMessageContent(commId, raNSFrom, raNSTo, interest, message);
		
		return send(commId, repaMessageContent);
	}
	
	@Override
	public String sendMessage(ResourceAgentNS raNSFrom, Integer prefixTo, String interest, String message) throws Exception {
		String commId = UUID.randomUUID().toString();
		RepaMessageContent repaMessageContent = new RepaMessageContent(commId, raNSFrom, prefixTo, interest, message);
		
		return send(commId, repaMessageContent);
	}
	
	@Override
	public String sendMessage(Integer prefixFrom, ResourceAgentNS raNSTo, String interest, String message) throws Exception {
		String commId = UUID.randomUUID().toString();
		RepaMessageContent repaMessageContent = new RepaMessageContent(commId, prefixFrom, raNSTo, interest, message);
		
		return send(commId, repaMessageContent);
	}
	
	@Override
	public String sendMessage(Integer prefixFrom, Integer prefixTo, String interest, String message) throws Exception {
		String commId = UUID.randomUUID().toString();
		RepaMessageContent repaMessageContent = new RepaMessageContent(commId, prefixFrom, prefixTo, interest, message);
		
		return send(commId, repaMessageContent);
	}
	
	private void addInterest(String interest, Callable callback) throws Exception {
		if (myInterests.get(interest) == null) {
			myInterests.put(interest, new LinkedBlockingQueue<Callable>(Arrays.asList(callback))); // first callback
		} else {
			myInterests.get(interest).add(callback); // add new callback of the same interest
		}
		
		Log.d("SmartAndroid", "myInterests - Adding a new callback of interest: " + interest);
	}

	private String send(String commId, RepaMessageContent repaMessageContent) throws Exception, InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		repaCommMap.put(commId, new REPAComm(countDownLatch));
		
		this.commREPAD.repaSend(repaMessageContent);
		
		countDownLatch.await();
		
		REPAComm repaComm = repaCommMap.get(commId);
		
		repaCommMap.remove(commId);
		
		return repaComm.getResponse();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void removeInterestCallback(String interest, Callable callback) throws Exception {
		for (Callable c : myInterests.get(interest)) {
			if (c.equals(callback)) {
				myInterests.remove(c);
				break;
			}
		}
	}
	
	@Override
	public void sendMessage(String contextVariable, String value) throws Exception {
		this.commREPAD.repaSendAsync(new RepaMessage(contextVariable, value));
	}
	
	@Override
	public Object fetchContextVariable(String contextVariable, String rai) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object callService(String serviceName, String rai) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public void registerInterest(String interest, String rai, Callable callback) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void sendMessageTo(String rai, String contextVariable, String value) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeInterest(String contextVariable, String rai) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object fetchContextVariable(String contextVariable) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeInterest(String contextVariable) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public int getPrefix() {
		return this.commREPAD.getRepaNodeAdress().getPrefix();
	}
	
	public class REPAComm {
		
		private CountDownLatch countDownLatch;
		
		private String response;
		
		public REPAComm(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		public void notifyResponseReceived(String response) {
			this.response = response;
			this.countDownLatch.countDown();
		}

		public String getResponse() {
			return response;
		}
	}
}
