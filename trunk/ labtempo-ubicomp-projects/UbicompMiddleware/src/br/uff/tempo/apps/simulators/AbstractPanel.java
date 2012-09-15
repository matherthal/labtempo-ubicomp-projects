package br.uff.tempo.apps.simulators;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public abstract class AbstractPanel extends View {	

	private int screenCenterX;
	private int screenCenterY;

	public AbstractPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void init() {

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		//get the screen center coordinates
		screenCenterX = metrics.widthPixels / 2;
		screenCenterY = metrics.heightPixels / 2;
	}

	public int getScreenCenterX() {
		return screenCenterX;
	}

	public int getScreenCenterY() {
		return screenCenterY;
	}

	

}
