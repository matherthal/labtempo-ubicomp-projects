package br.uff.tempo.middleware.comm.interest.api;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import ufrj.coppe.lcp.repa.RepaMessage;
import ufrj.coppe.lcp.repa.RepaSocket;
import android.util.Log;

public class InterestGateway {
	
	private RepaSocket repaSocket;
	
	private static InterestGateway interestGateway;
	
	private ConcurrentHashMap<String, BlockingQueue<Callable>> myInterests = new ConcurrentHashMap<String, BlockingQueue<Callable>>();
	
	protected InterestGateway(ConcurrentHashMap<String, BlockingQueue<Callable>> myInterests) {
		this.myInterests = myInterests;
		
		try {
			this.startDaemon();
		} catch (Exception e) {
			Log.d("interest.api", "Exception do startDaemon", e);
		}
	}
	
	public static InterestGateway getInstance(ConcurrentHashMap<String, BlockingQueue<Callable>> myInterests) {
		if (interestGateway == null) {
			interestGateway = new InterestGateway(myInterests);
		}
		
		return interestGateway;
	}
	
	void startDaemon() throws Exception {
		Log.d("interest.api", "Vou iniciar o daemon");
		new Thread(new Runnable() {
			boolean terminated = false;
			
			public void run()  {
				RepaMessage message = null;
				
				while (!terminated) {
					try {
						Log.d("interest.api", "Vou chamar o repaRecv");
						message = getRepaSocket().repaRecv();
						Log.d("interest.api", "Recebi message do repaRecv" + message.getData());
						
						if (message != null) {
							Log.d("interest.api", "Vou startar uma nova thread para tratar mensagem recebida");
							new Thread(new DealRepaMessage(message, myInterests.get(message.getInterest()))).start();
						}
					} catch (Exception e) {
						Log.d("interest.api", "Exception ", e);
						terminated = true;
						
						try {
							getRepaSocket().repaClose();
						} catch (Exception e2) {
							Log.d("interest.api", "Exception fazendo o close do socket", e2);
						}
					}
				}
				Log.d("interest.api", "Fim da execu��o do daemon de recebimento de mensagens repa.");
			}
		}).start();
		Log.d("interest.api", "Daemon inicializado");
	}
	
	public RepaSocket getRepaSocket() throws Exception {
		if (repaSocket == null) {
			repaSocket = RepaSocket.getRepaSocket();
			
			repaSocket.repaOpen();
		}
		return repaSocket;
	}
}
