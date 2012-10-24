package br.uff.tempo.apps.simulators;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.os.DropBoxManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
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
	private float scale;
	
	private boolean thereIsAnAgent = true;

	public AbstractPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
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
		
		scale = getResources().getDisplayMetrics().density;
		
		IResourceAgent res = getAgentFromView();
		
		if (res == null) {
			thereIsNotAnAgent();
		} else {
			thereIsAnAgent();
			setAgent(res);
		}

		initialization();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	
		if (thereIsAnAgent) {
			drawCanvas(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	
		if (thereIsAnAgent) {
			touch(event);
		}
		
		return super.onTouchEvent(event);
	}
	
	public void thereIsAnAgent() {
		
		thereIsAnAgent = true;
	}
	
	public void thereIsNotAnAgent() {
		thereIsAnAgent = false;
	}
	
	public abstract void initialization();
	public abstract void setAgent(IResourceAgent agent);
	public abstract void touch(MotionEvent event);
	public abstract void drawCanvas(Canvas canvas);
	
	/**
	 * Called when occurs a change in any context variable from current Agent.
	 * @param method Context variable changed.
	 * @param value Current value from the context variable changed.
	 */
	public abstract void onUpdate(String method, Object value);
	
	public IResourceAgent getAgentFromView() {
		
		return ((AbstractView) getContext()).getAgent();
	}
	
	public void setupInterest() {
		
		// When the agent change its state, redraw the screen
		IResourceAgent res = getAgentFromView();
		
		new Generic(res.getName() + "Stakeholder" + getNextID(), res, "all") {
			
			private static final long serialVersionUID = 1L;

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
	
	public int getNextID() {
		return new Random().nextInt(100);
	}
	
	public float getDensity() {
		return scale;
	}
	
	public int dpTopixel(float dp) {
		return (int) (dp * scale + 0.5f);
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
	
	public int getScreenHeight() {
		return screenHeight;
	}
}
