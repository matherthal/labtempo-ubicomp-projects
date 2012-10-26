package br.uff.tempo.middleware.comm.interest.api;

import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import ufrj.coppe.lcp.repa.RepaMessage;
import android.util.Log;
import br.uff.tempo.middleware.comm.common.Callable;
import br.uff.tempo.middleware.comm.common.InterestAPI;
import br.uff.tempo.middleware.comm.current.api.SocketService;
import br.uff.tempo.middleware.e.SmartAndroidException;

public class InterestAPIImpl implements InterestAPI {
	
	private static ConcurrentHashMap<String, BlockingQueue<Callable>> myInterests = new ConcurrentHashMap<String, BlockingQueue<Callable>>();
	
	private static InterestAPIImpl api;
	
	private CommREPAD commREPAD;
	
	public synchronized static InterestAPIImpl getInstance() {
		if (api == null) {
			api = new InterestAPIImpl(); 
		}
		return api;
	}
	
	public InterestAPIImpl() {
		try {
			commREPAD = new CommREPAD() {
				@Override
				public String serve(RepaMessage repaMessage) {
					return dispatchRepaMessage(repaMessage);
				}
			};
		} catch (SocketException e) {
			Log.d("SmartAndroid", "Erro criando CommREPAD: " + e);
		}
	}

	private static String dispatchRepaMessage(RepaMessage message) {
		String messageContent = new String(message.getData());
		
		if (messageContent.contains("jsonrpc\":\"2.0")) {
			return dispatchJSONRPC(messageContent);
		}
		
		BlockingQueue<Callable> callbacks = myInterests.get(message.getInterest());
		Log.d("SmartAndroid", "Vou chamar os callbacks");
		for (Callable callback : callbacks) {
			callback.call(null, message.getInterest(), messageContent);
		}
		Log.d("SmartAndroid", "Fim das chamadas aos callbacks");
		
		return null;
	}

	private static String dispatchJSONRPC(String messageContent) {
		String[] msgTokens = messageContent.split(SocketService.BUFFER_END);
		
		String raiFrom = msgTokens[0];
		String jsonRPCString = msgTokens[1];
		
		String response = null;
		try {
			response = NewDispatcher.getInstance().dispatch(raiFrom, jsonRPCString) + SocketService.BUFFER_END;
		} catch (SmartAndroidException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public void addInterest(String interest, Callable callback) throws Exception {
		if (myInterests.get(interest) == null) {
			Log.d("SmartAndroid", "Adicionando o primeiro callback do interesse: " + interest);
			// first callback
			myInterests.put(interest, new LinkedBlockingQueue<Callable>(Arrays.asList(callback)));
		} else {
			Log.d("SmartAndroid", "Adicionando um novo callback do interesse" + interest);
			// add new callback of the same interest
			myInterests.get(interest).add(callback);	
		}

		Log.d("SmartAndroid", "Chamando registro de interesse da repa para o interesse: " + interest);
		this.commREPAD.registerInterest(interest);
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
		this.commREPAD.repaSend(new RepaMessage(contextVariable, value));
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

	@Override
	public void registerInterest(String interest, String rai, Callable callback) throws Exception {
		// TODO Auto-generated method stub
		
	}

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
	public void registerInterest(String interest, Callable callback) throws Exception {
		if (myInterests.get(interest) == null) {
			Log.d("SmartAndroid", "Adicionando o primeiro callback do interesse: " + interest);
			// first callback
			myInterests.put(interest, new LinkedBlockingQueue<Callable>(Arrays.asList(callback)));
		} else {
			Log.d("SmartAndroid", "Adicionando um novo callback do interesse: " + interest);
			// add new callback of the same interest
			myInterests.get(interest).add(callback);	
		}

		Log.d("SmartAndroid", "Chamando registro de interesse da repa para o interesse: " + interest);
		this.commREPAD.registerInterest(interest);
	}

	@Override
	public void removeInterest(String contextVariable) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public int getPrefix() {
		return this.commREPAD.getRepaNodeAdress().getPrefix();
	}
}
