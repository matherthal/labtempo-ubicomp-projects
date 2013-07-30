package br.uff.tempo.middleware.management;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class TimerNode extends Formula {
	private Timer timer = null;
	private TimeoutTask timeTask = null;
	private Integer timeout = 0;
	
	public TimerNode() {
		super("0");
	}
	
	private void timerReset() {
		timerStop();
		Log.i("Timer", "Start");
		this.timer = new Timer();
		timeTask = new TimeoutTask();

		Log.i("TIME", new Date().toGMTString());

		long t = this.timeout * 1000;
		timer.schedule(timeTask, t);
	}

	private void timerStop() {
		if (timeTask != null) {
			Log.i("Timer", "Stop");
			timeTask.cancel();
			timeTask = null;
		}
	}

	class TimeoutTask extends TimerTask {

		public void run() {
			Log.i("TimeoutTask", "Timeout went off!");
			Log.i("TIME", new Date().toGMTString());
			//super.
//			if (evaluateExpr())
//				notifyActionPerformers();
		}
	}
}
