package br.uff.tempo.middleware.comm.interest.api;

import java.util.concurrent.CountDownLatch;

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