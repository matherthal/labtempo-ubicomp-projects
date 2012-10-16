package br.uff.tempo.middleware.comm.interest.api;

import java.net.SocketException;

import ufrj.coppe.lcp.repa.PrefixAddress;
import ufrj.coppe.lcp.repa.RepaMessage;
import ufrj.coppe.lcp.repa.RepaSocket;

public class CommREPAD {

	private Thread myThread;

	private RepaSocket repaSocket;

	public CommREPAD() throws SocketException {
		repaSocket = RepaSocket.getRepaSocket();
		repaSocket.repaOpen();

		myThread = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						new REPASession(repaSocket.repaRecv());
					}
				} catch (Exception e) {
				}
			}
		}, "CommREPAD");
		myThread.setDaemon(true);
		myThread.start();
	}
	
	public String serve(RepaMessage repaMessage) {
		return null;
	}

	private class REPASession implements Runnable {

		private RepaMessage repaMessage;

		public REPASession(RepaMessage repaMessage) {
			this.repaMessage = repaMessage;
			
			Thread t = new Thread(this);
			t.setDaemon(true);
			t.start();
		}

		public void run() {
			if (this.repaMessage == null) {
				return;
			}

			String r = serve(this.repaMessage);
			if (r != null) {
				//sendResponse(r.status, r.mimeType, r.header, r.data);
			}
		}
	}

	public PrefixAddress getRepaNodeAdress() {
		return this.repaSocket.getRepaNodeAdress();
	}

	public void repaSend(RepaMessage repaMessage) throws Exception {
		this.repaSocket.repaSend(repaMessage);
	}

	public void registerInterest(String interest) throws Exception {
		this.repaSocket.registerInterest(interest);
	}

	public void unregisterInterest(String interest) throws Exception {
		this.repaSocket.unregisterInterest(interest);
	}
}
