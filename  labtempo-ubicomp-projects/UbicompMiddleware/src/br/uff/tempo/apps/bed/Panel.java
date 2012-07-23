package br.uff.tempo.apps.bed;

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

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

	private final String TAG = "Panel-BedView";

	private int mX = 20;
	private int mY = 20;

	private boolean occuped = false;

	private BedData bed;
	private Bitmap mBitmap;

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

		bed = ((BedView) getContext()).getStoveState();
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bed);

		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		setFocusable(true);

		// subtract a half of bitmap width and height to draw centralized
		mX -= mBitmap.getWidth() / 2;
		mY -= mBitmap.getHeight() / 2;
	}

	public void doDraw(Canvas canvas) {
		// draw the background color
		canvas.drawColor(Color.BLACK);
		// draw the bed bitmap
		canvas.drawBitmap(mBitmap, mX, mY, null);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public boolean onTouchEvent(MotionEvent event) {
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			occuped = !occuped;

			// BedData data = ((BedView) getContext()).getStoveState();

			Log.d(TAG, "X = " + x + " and Y = " + y);

			String msg;

			if (occuped) {
				msg = "There is someone in the bed";
			} else {
				msg = "There is no one in the bed";
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
