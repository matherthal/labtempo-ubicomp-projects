package br.uff.tempo.middleware.comm.interest.api;

import java.util.concurrent.BlockingQueue;

import ufrj.coppe.lcp.repa.RepaMessage;
import android.util.Log;

public class DealRepaMessage implements Runnable {
	
	private BlockingQueue<Callable> callbacks;
	
	private RepaMessage message;

	public DealRepaMessage(RepaMessage message, BlockingQueue<Callable> callbacks) {
		this.message = message;
		this.callbacks = callbacks;
	}
	
	public void run() {
		Log.d("interest.api", "Vou chamar os callbacks");
		for (Callable callback : this.callbacks) {
			callback.call(this.message.getInterest(), new String(this.message.getData()), this.message.getPrefix().getPrefix());
		}
		Log.d("interest.api", "Fim das chamadas aos callbacks");
	}
}
