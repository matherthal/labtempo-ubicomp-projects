package br.uff.tempo.apps.tv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

	private final String TAG = "Panel-BedView";

	private int mX = 20;
	private int mY = 20;

	private int centerX;
	private int centerY;

	private String msg = "Teste";
	Paint msgPaint;

	private ITelevision tv;
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

		centerX = metrics.widthPixels / 2;
		centerY = metrics.heightPixels / 2;

		tv = ((TvView) getContext()).getTvState();
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tv);

		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		setFocusable(true);

		// subtract a half of bitmap width and height to draw centralized
		mX = centerX - mBitmap.getWidth() / 2;
		mY = centerY - mBitmap.getHeight() / 2;

		msgPaint = new Paint();
	}

	public void doDraw(Canvas canvas) {

		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the tv bitmap
		canvas.drawBitmap(mBitmap, mX, mY, null);

		// Draw the TV message
		canvas.drawText(msg, centerX, centerY, msgPaint);

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
	}

	public boolean onTouchEvent(MotionEvent event) {

		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

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
