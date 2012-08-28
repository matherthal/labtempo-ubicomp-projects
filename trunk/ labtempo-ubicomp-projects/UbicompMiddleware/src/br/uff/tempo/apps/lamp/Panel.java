package br.uff.tempo.apps.lamp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.Bed;
import br.uff.tempo.middleware.resources.Lamp;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

	private final String TAG = "Panel-LampView";

	private int mX = 20;
	private int mY = 20;

	private boolean on = false;

	// private BedData bed;
	private Lamp lamp;
	private Bitmap mBitmapOn;
	private Bitmap mBitmapOff;

	private ViewThread mThread;

	public Panel(Context context) {
		super(context);
		init();
	}

	public Panel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		mX = metrics.widthPixels / 2;
		mY = metrics.heightPixels / 2;

		lamp = ((LampView) getContext()).getLampState();
		
		mBitmapOn = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_on);
		mBitmapOff = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_off);

		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		setFocusable(true);

		// subtract a half of bitmap width and height to draw centralized
		mX -= mBitmapOn.getWidth() / 2;
		mY -= mBitmapOn.getHeight() / 2;
	}

	public void doDraw(Canvas canvas) {
		// draw the background color
		canvas.drawColor(Color.WHITE);
		// draw the lamp bitmap
		
		if (on) {
			canvas.drawBitmap(mBitmapOn, mX, mY, null);
		}
		else {
			canvas.drawBitmap(mBitmapOff, mX, mY, null);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public boolean onTouchEvent(MotionEvent event) {
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			on = !on;

			Log.d(TAG, "X = " + x + " and Y = " + y);

			String msg;

			if (on) {
				msg = "The lamp is on";
				lamp.turnOn();
			} else {
				msg = "The lamp is off";
				lamp.turnOff();
			}

			Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
		}

		return true;
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
