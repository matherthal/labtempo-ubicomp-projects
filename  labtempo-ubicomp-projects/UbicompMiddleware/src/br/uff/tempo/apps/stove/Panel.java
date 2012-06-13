package br.uff.tempo.apps.stove;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public abstract class Panel extends SurfaceView implements SurfaceHolder.Callback {

	private final String TAG = "Panel-StoveView";
	
	protected int mX = 20;
	protected int mY = 20;

	private ViewThread mThread;

	public Panel(Context context) {
		super(context);
	}

	public Panel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void init() {

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		mX = metrics.widthPixels / 2;
		mY = metrics.heightPixels / 2;

		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		setFocusable(true);
	}

	public abstract void doDraw(Canvas canvas);

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!mThread.isAlive()) {
			mThread = new ViewThread(this);
			mThread.setRunning(true);
			mThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mThread.isAlive()) {
			mThread.setRunning(false);
		}
	}

}
