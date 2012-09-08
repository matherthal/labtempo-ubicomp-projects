package br.uff.tempo.middleware.comm.interest.api;

import java.net.SocketException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import ufrj.coppe.lcp.repa.PrefixAddress;
import ufrj.coppe.lcp.repa.RepaMessage;
import android.util.Log;

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
		BlockingQueue<Callable> callbacks = myInterests.get(message.getInterest());
		
		Log.d("SmartAndroid", "Vou chamar os callbacks");
		for (Callable callback : callbacks) {
			callback.call(message.getInterest(), new String(message.getData()), message.getPrefix().getPrefix());
		}
		Log.d("SmartAndroid", "Fim das chamadas aos callbacks");
		
		return null;
	}
	
	public int getMyAddress() throws Exception {
		return this.commREPAD.getRepaNodeAdress().getPrefix();
	}

	public void sendMessage(String interest, String value) throws Exception {
		this.commREPAD.repaSend(new RepaMessage(interest, value));
	}
	
	public void sendMessageTo(int address, String interest, String value) throws Exception {
		this.commREPAD.repaSend(new RepaMessage(interest, value, new PrefixAddress(address)));
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

	public void removeInterest(String interest) throws Exception {
		// TODO: remover a minha referencia e se não tiver mais ninguem remover de fato o interesse
		
//		myInterests.remove(interest);
//		this.commREPAD.unregisterInterest(interest);
	}
	
	public void removeInterestCallback(String interest, Callable callback) throws Exception {
		for (Callable c : myInterests.get(interest)) {
			if (c.equals(callback)) {
				myInterests.remove(c);
				break;
			}
		}
	}
	
	public void removeAllInterests() throws Exception {
		// TODO: remover todos os interesses da minha referência
//		myInterests.clear();
//		this.gateway.getRepaSocket().unregisterAll();
	}

	public List<String> getListInterests() throws Exception {
		// TODO: listar todos os interesses da minha instancia
//		return this.gateway.getRepaSocket().getListInterests();
		return null;
	}

	public List<Integer> getListNodes() throws Exception {
//		List<Integer> listNodes = new ArrayList<Integer>();
//		
//		List<PrefixAddress> listPrefixAddressNodes = this.gateway.getRepaSocket().getListNodes();
//		
//		if (listPrefixAddressNodes != null) {
//			for (PrefixAddress prefixAddress : listPrefixAddressNodes) {
//				listNodes.add(prefixAddress.getPrefix());
//			}
//		}
//		
		return null;
	}
}
