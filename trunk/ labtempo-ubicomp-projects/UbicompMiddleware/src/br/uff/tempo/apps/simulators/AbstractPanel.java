package br.uff.tempo.apps.simulators;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.resources.Generic;

public abstract class AbstractPanel extends View {	

	private static int count = 0;
	private int screenCenterX;
	private int screenCenterY;
	
	private int screenWidth;
	private int screenHeight;
	
	private Handler handler = new Handler();

	public AbstractPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void init() {

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		//get the screen length
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		//get the screen center coordinates
		screenCenterX = screenWidth / 2;
		screenCenterY = screenHeight / 2;
		
		setupInterest();
	}
	
	public void setupInterest() {
		
		// When the agent change its state, redraw the screen
		IResourceAgent res = ((AbstractView) getContext()).getAgent();
		
		new Generic("StakeholderFromView" + (++count), res, "all") {
			
			@Override
			public void notificationHandler(final String rai, final String method, final Object value) {
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						AbstractPanel.this.onUpdate(method, value);
						AbstractPanel.this.invalidate();	
					}
				});
				
			}
		};
	}
	
	/**
	 * Called when occurs a change in any context variable from current Agent.
	 * @param method Context variable changed.
	 * @param value Current value from the context variable changed.
	 */
	public abstract void onUpdate(String method, Object value);
	
	public int getScreenCenterX() {
		return screenCenterX;
	}

	public int getScreenCenterY() {
		return screenCenterY;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeiht() {
		return screenHeight;
	}
}
