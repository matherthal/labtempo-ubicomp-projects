package br.uff.tempo.apps.simulators.tracking.surface;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class UpdateThread extends Thread {

	private boolean isRunning;
	private AbstractTrackingPanel panel;

	public UpdateThread(AbstractTrackingPanel panel) {
		this.panel = panel;
	}

	@Override
	public void run() {

		while (isRunning) {

			Canvas c = null;
			SurfaceHolder holder = panel.getHolder();
			
			try {
				c = holder.lockCanvas(null);
				synchronized (holder) {
					panel.onDraw(c);
				}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
