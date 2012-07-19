package br.uff.tempo.middleware.comm.interest.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import ufrj.coppe.lcp.repa.PrefixAddress;
import ufrj.coppe.lcp.repa.RepaMessage;
import android.util.Log;

public class InterestAPIImpl implements InterestAPI {
	
	private ConcurrentHashMap<String, BlockingQueue<Callable>> myInterests = new ConcurrentHashMap<String, BlockingQueue<Callable>>();
	
	// interesse: temperatura -> callback: agente ar condicionado, callback: agente sensor de temperatura etc..
	
	private InterestGateway gateway;
	
	public InterestAPIImpl() {
		this.gateway = InterestGateway.getInstance(myInterests);
	}

	public int getMyAddress() throws Exception {
		return this.gateway.getRepaSocket().getRepaNodeAdress().getPrefix();
	}

	public void sendMessage(String interest, String value) throws Exception {
		this.gateway.getRepaSocket().repaSend(new RepaMessage(interest, value));
	}
	
	public void sendMessageTo(int address, String interest, String value) throws Exception {
		this.gateway.getRepaSocket().repaSend(new RepaMessage(interest, value, new PrefixAddress(address)));
	}
	
	public void addInterest(String interest, Callable callback) throws Exception {
		if (this.myInterests.get(interest) == null) {
			Log.d("interest.api", "Adicionando o primeiro callback do interesse: " + interest);
			// first callback
			this.myInterests.put(interest, new LinkedBlockingQueue<Callable>(Arrays.asList(callback)));
		} else {
			Log.d("interest.api", "Adicionando um novo callback do interesse" + interest);
			// add new callback of the same interest
			this.myInterests.get(interest).add(callback);	
		}

		Log.d("interest.api", "Chamando registro de interesse da repa para o interesse: " + interest);
		this.gateway.getRepaSocket().registerInterest(interest);
	}

	public void removeInterest(String interest) throws Exception {
		this.myInterests.remove(interest);
		
		this.gateway.getRepaSocket().unregisterInterest(interest);
	}
	
	public void removeInterestCallback(String interest, Callable callback) throws Exception {
		for (Callable c : this.myInterests.get(interest)) {
			if (c.equals(callback)) {
				this.myInterests.remove(c);
				break;
			}
		}
	}
	
	public void removeAllInterests() throws Exception {
		this.myInterests.clear();
		
		this.gateway.getRepaSocket().unregisterAll();
	}

	public List<String> getListInterests() throws Exception {
		return this.gateway.getRepaSocket().getListInterests();
	}

	public List<Integer> getListNodes() throws Exception {
		List<Integer> listNodes = new ArrayList<Integer>();
		
		List<PrefixAddress> listPrefixAddressNodes = this.gateway.getRepaSocket().getListNodes();
		
		if (listPrefixAddressNodes != null) {
			for (PrefixAddress prefixAddress : listPrefixAddressNodes) {
				listNodes.add(prefixAddress.getPrefix());
			}
		}
		
		return listNodes;
	}
}
