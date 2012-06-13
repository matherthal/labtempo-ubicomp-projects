package br.uff.tempo.apps.stove;

import br.uff.tempo.R;
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

public class PanelBurners extends Panel implements SurfaceHolder.Callback {

	private final String TAG = "Panel-StoveView";

	private StoveData stove;

	private Bitmap mBitmap;
	private Bitmap mButtons;

	private Bitmap mFireOne;
	private Bitmap mFireTwo;
	private Bitmap mFireThree;
	private Bitmap mFireFour;

	private ViewThread mThread;

	public PanelBurners(Context context) {
		super(context);
		init();
	}

	public PanelBurners(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	protected void init() {

		super.init();
		
		stove = ((StoveView) getContext()).getStoveState();
		mBitmap = BitmapFactory
				.decodeResource(getResources(), R.drawable.stove);
		mButtons = BitmapFactory.decodeResource(getResources(),
				R.drawable.stove_buttons);

		// flames
		mFireOne = BitmapFactory.decodeResource(getResources(),
				R.drawable.fire_burner_one);
		mFireTwo = BitmapFactory.decodeResource(getResources(),
				R.drawable.fire_burner_two);
		mFireThree = BitmapFactory.decodeResource(getResources(),
				R.drawable.fire_burner_three);
		mFireFour = BitmapFactory.decodeResource(getResources(),
				R.drawable.fire_burner_four);

		mX -= mBitmap.getWidth() / 2;
		mY -= mBitmap.getHeight() / 2;
	}

	public void doDraw(Canvas canvas) {

		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the stove bitmap
		canvas.drawBitmap(mBitmap, mX, mY, null);

		// draw the flame at burners, if needed
		
		int[] flame = stove.getBurners();

		if (flame[0] > 0)
			canvas.drawBitmap(mFireOne, mX, mY, null);
		if (flame[1] > 0)
			canvas.drawBitmap(mFireTwo, mX, mY, null);
		if (flame[2] > 0)
			canvas.drawBitmap(mFireThree, mX, mY, null);
		if (flame[3] > 0)
			canvas.drawBitmap(mFireFour, mX, mY, null);
			
	}

	public boolean onTouchEvent(MotionEvent event) {

		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		StoveData data = ((StoveView) getContext()).getStoveState();

		Log.d(TAG, "X = " + x + " and Y = " + y);

		int color;

		try {
			// Check which button has clicked

			// get the pixel color of coordinate (x, y) (translated)
			color = mButtons.getPixel(x - mX, y - mY);

			switch (color) {
			case Color.RED: // 0xfffa0000:
				// ((StoveView) getContext()).showPopup(0);
				data.setBurnerIntensity(0, 100 - data.getBurnerIntensity(0));
				Log.d(TAG, "Burner 1");
				break;
			case Color.BLUE:
				// ((StoveView) getContext()).showPopup(1);
				data.setBurnerIntensity(1, 100 - data.getBurnerIntensity(1));
				Log.d(TAG, "Burner 2");
				break;
			case Color.GREEN:
				Log.d(TAG, "Burner 3");
				// ((StoveView) getContext()).showPopup(2);
				data.setBurnerIntensity(2, 100 - data.getBurnerIntensity(2));
				break;
			case Color.YELLOW:
				Log.d(TAG, "Burner 4");
				// ((StoveView) getContext()).showPopup(3);
				data.setBurnerIntensity(3, 100 - data.getBurnerIntensity(3));
				break;
			default:
				Log.d(TAG, "Nothing...");
			}

			Log.d(TAG, "Color Clicked = " + Integer.toHexString(color));

		} catch (IllegalArgumentException ex) {
			Log.d(TAG, "Exception... " + ex);
		}

		// mX = x - mBitmap.getWidth() / 2;
		// mY = y - mBitmap.getHeight() / 2;

		return super.onTouchEvent(event);
	}

}
