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
		
		new Generic("Stakeholder", res, "all") {
			
			@Override
			public void notificationHandler(String rai, String method, Object value) {
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						AbstractPanel.this.invalidate();	
					}
				});
				
			}
		};
	}

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
