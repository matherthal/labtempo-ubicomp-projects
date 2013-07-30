package br.uff.tempo.middleware.comm.interest.api;

import java.net.SocketException;

import ufrj.coppe.lcp.repa.PrefixAddress;
import ufrj.coppe.lcp.repa.RepaMessage;
import ufrj.coppe.lcp.repa.RepaSocket;
import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.comm.current.api.JSONHelper;
import br.uff.tempo.middleware.comm.current.api.SocketService;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;

public class CommREPAD {

	private Thread myThread;

	private RepaSocket repaSocket;

	public CommREPAD() {
		try {
			repaSocket = RepaSocket.getRepaSocket();
			repaSocket.repaOpen();
		} catch (SocketException e) {
			throw new SmartAndroidRuntimeException("Error creating CommREPAD. Problem getting or opening RepaSocket", e);
		}
		
		myThread = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						new REPASession(repaSocket.repaRecv());
					}
				} catch (Exception e) {
					throw new SmartAndroidRuntimeException("Error in repaSocket.repaRecv()", e);
				}
			}
		}, "CommREPAD");
		myThread.setDaemon(true);
		myThread.start();
	}
	
	public String serve(RepaMessageContent repaMessageContent) {
		return null;
	}

	private class REPASession implements Runnable {

		private RepaMessage repaMessage;
		
		private long timeToStart;

		public REPASession(RepaMessage repaMessage) {
			this.repaMessage = repaMessage;
			
			Thread t = new Thread(this);
			t.setDaemon(true);
			t.start();
		}
		
		public REPASession(RepaMessage repaMessage, long timeToStart) {
			this.repaMessage = repaMessage;
			this.timeToStart = timeToStart;
			
			Thread t = new Thread(this);
			t.setDaemon(true);
			t.start();			
		}

		public void run() {
			if (this.timeToStart > 0) {
				try {
					Thread.sleep(this.timeToStart);
				} catch (InterruptedException e) {
					throw new SmartAndroidRuntimeException("Unable sleep REPASession.thread", e);
				}
			}
			
			if (this.repaMessage == null) {
				return;
			}
			
			RepaMessageContent repaMessageContent = messageDataToRepaMessageContent();
			
			String response = serve(repaMessageContent);
			
			if (response != null) {
				repaMessageContent.setReply(true);
				
				repaMessageContent.swapRaNS();
				repaMessageContent.swapPrefix();
				
				repaMessageContent.setContent(response);
				
				try {
					repaSend(repaMessageContent);
				} catch (Exception e) {
					throw new SmartAndroidRuntimeException("Unable to send response: " + repaMessageContent, e);
				}
			}
		}

		private RepaMessageContent messageDataToRepaMessageContent() {
			String repaMessageContentJSON = SocketService.decompress(repaMessage.getData());
			RepaMessageContent repaMessageContent = (RepaMessageContent) JSONHelper.fromJson(repaMessageContentJSON, RepaMessageContent.class);
			return repaMessageContent;
		}
	}

	public void repaSend(RepaMessageContent messageContent) throws Exception {
		RepaMessage repaMessage = new RepaMessage();
		
		Integer prefix = messageContent.getPrefixTo();
		String interest = messageContent.getInterest();
		if (messageContent.getRaNSTo() != null) {
			prefix = messageContent.getRaNSTo().getPrefix();
			interest = messageContent.getRaNSTo().getRans();
		}
		repaMessage.setPrefix(prefix == -1 ? new PrefixAddress() : new PrefixAddress(prefix));
		repaMessage.setInterest(interest);
		repaMessage.setData(messageContentToMessageData(messageContent));
		
		// Loopback
		if (repaMessage.getPrefix().getPrefix() == SmartAndroid.getLocalPrefix()) {
			new REPASession(repaMessage, 30);
		} else {
			this.repaSocket.repaSend(repaMessage);			
		}
	}
	
	
	
	
	
	public PrefixAddress getRepaNodeAdress() {
		return this.repaSocket.getRepaNodeAdress();
	}
	
	public void repaSendAsync(RepaMessage repaMessage) throws Exception {
		this.repaSocket.repaSend(repaMessage);
	}

	public void registerInterest(String interest) throws Exception {
		this.repaSocket.registerInterest(interest);
	}

	public void unregisterInterest(String interest) throws Exception {
		this.repaSocket.unregisterInterest(interest);
	}
	
	private byte[] messageContentToMessageData(RepaMessageContent messageContent) {
		String repaMessageContentJSON = JSONHelper.toJson(messageContent);
		byte[] messageData = SocketService.compress(repaMessageContentJSON);
		return messageData;
	}
}
