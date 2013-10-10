package br.uff.tempo.middleware.comm.interest.api;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;
import br.uff.tempo.middleware.comm.common.Callable;
import br.uff.tempo.middleware.comm.common.InterestAPI;
import br.uff.tempo.middleware.management.ResourceAgentNS;

public class InterestAPIImpl implements InterestAPI {
	
	private static final String INDEX_SEPARATOR = "://:";
	
	private static ConcurrentHashMap<String, BlockingQueue<Callable>> myInterests = new ConcurrentHashMap<String, BlockingQueue<Callable>>();
	
	private static ConcurrentHashMap<String, BlockingQueue<Callable>> agentsInterestsIndex = new ConcurrentHashMap<String, BlockingQueue<Callable>>();
	
	private static ConcurrentHashMap<String, REPAComm> repaCommMap = new ConcurrentHashMap<String, REPAComm>();
	
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
			
			@Override
			public ConcurrentHashMap<String, BlockingQueue<Callable>> getMyInterests() {
				return myInterests;
			}
		};
	}

	private static String dispatchRepaMessage(RepaMessageContent messageContent) {
		if (messageContent.isReply()) {
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
		
		String callbackResult = null;
		for (Callable callback : callbacks) {
			callbackResult = callback.call(raNSFrom, messageContent.getInterest(), messageContent.getContent());
		}
		
		return callbackResult;
	}
	
	@Override
	public void registerInterest(String interest) throws Exception {
		myInterests.put(interest, new LinkedBlockingQueue<Callable>());
		
		this.commREPAD.registerInterest(interest);
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
		Log.d("SmartAndroid", String.format("rans: %s interested in: %s", rans, interest));
		
		this.commREPAD.registerInterest(interest);
	}	

	@Override
	public void registerInterest(String interest, Callable callback) throws Exception {
		this.addInterest(interest, callback); // Adding to default interests map

		this.commREPAD.registerInterest(interest);
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
	public String sendMessage(ResourceAgentNS raNSTo, String interest, String message) throws Exception {
		String commId = UUID.randomUUID().toString();
		RepaMessageContent repaMessageContent = new RepaMessageContent(commId, getMyPrefix(), raNSTo, interest, message);
		
		return send(commId, repaMessageContent);
	}
	
	@Override
	public String sendMessage(Integer prefixTo, String interest, String message) throws Exception {
		String commId = UUID.randomUUID().toString();
		RepaMessageContent repaMessageContent = new RepaMessageContent(commId, getMyPrefix(), prefixTo, interest, message);
		
		return send(commId, repaMessageContent);
	}
	
	@Override
	public String sendMessage(String interest, String message) throws Exception {
		return sendMessage(-1, interest, message);
	}

	@Override
	public void sendAsyncMessage(ResourceAgentNS raNSfrom, ResourceAgentNS raNSto, String interest, String message, Callable callback) throws Exception {
	}

	@Override
	public void sendAsyncMessage(ResourceAgentNS raNSfrom, int prefixTo, String interest, String message, Callable callback) throws Exception {
	}

	@Override
	public void sendAsyncMessage(ResourceAgentNS raNSto, String interest, String message, Callable callback) throws Exception {
	}

	@Override
	public void sendAsyncMessage(int prefixTo, String interest, String message, Callable callback) throws Exception {
	}	

	
	@Override
	public List<ResourceAgentNS> getListOfResourceAgents() throws Exception {
		return null;
	}

	@Override
	public List<ResourceAgentNS> getListOfResourceAgentsInterestedIn(String interest) throws Exception {
		return null;
	}

	@Override
	public List<String> getListOfInterestedIn(String interest) throws Exception {
		return null;
	}
	
	

	
//	@Override
//	public void sendMessage(String interest, String value) throws Exception {
//		this.commREPAD.repaSendAsync(new RepaMessage(interest, value));
//	}
	
	
	public int getMyPrefix() throws Exception {
		return this.commREPAD.getRepaNodeAddress().getPrefix();
	}
	
	private void addInterest(String interest, Callable callback) throws Exception {
		if (myInterests.get(interest) == null) {
			myInterests.put(interest, new LinkedBlockingQueue<Callable>(Arrays.asList(callback))); // first callback
		} else {
			myInterests.get(interest).add(callback); // add new callback of the same interest
		}
	}

	private String send(String commId, RepaMessageContent repaMessageContent) throws Exception {
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		repaCommMap.put(commId, new REPAComm(countDownLatch));
		
		this.commREPAD.repaSend(repaMessageContent);
		
		countDownLatch.await();
		
		REPAComm repaComm = repaCommMap.get(commId);
		
		repaCommMap.remove(commId);
		
		return repaComm.getResponse();
	}
	
	
	
	
	
	
//	public void removeInterestCallback(String interest, Callable callback) throws Exception {
//		for (Callable c : myInterests.get(interest)) {
//			if (c.equals(callback)) {
//				myInterests.remove(c);
//				break;
//			}
//		}
//	}
}
