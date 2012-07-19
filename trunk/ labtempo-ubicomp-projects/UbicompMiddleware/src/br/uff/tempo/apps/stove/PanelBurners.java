package br.uff.tempo.apps.stove;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class PanelBurners extends Panel implements SurfaceHolder.Callback {

	private final String TAG = "Panel-StoveView";

	// private StoveData stove;
	private IStove stove;
	private IStove cache;

	private Bitmap mBitmap;
	private Bitmap mButtons;

	private Bitmap mFireOne;
	private Bitmap mFireTwo;
	private Bitmap mFireThree;
	private Bitmap mFireFour;

	private ViewThread mThread;
	private List<Boolean> isOnBurners;

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
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stove);
		mButtons = BitmapFactory.decodeResource(getResources(), R.drawable.stove_buttons);

		// flames
		mFireOne = BitmapFactory.decodeResource(getResources(), R.drawable.fire_burner_one);
		mFireTwo = BitmapFactory.decodeResource(getResources(), R.drawable.fire_burner_two);
		mFireThree = BitmapFactory.decodeResource(getResources(), R.drawable.fire_burner_three);
		mFireFour = BitmapFactory.decodeResource(getResources(), R.drawable.fire_burner_four);

		mX -= mBitmap.getWidth() / 2;
		mY -= mBitmap.getHeight() / 2;

		isOnBurners = new ArrayList<Boolean>(4);
		for (int i = 0; i < 4; i++)
			isOnBurners.add(false);

		stove = ((StoveView) getContext()).getStoveState();
	}

	public void doDraw(Canvas canvas) {

		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the stove bitmap
		canvas.drawBitmap(mBitmap, mX, mY, null);

		// draw the flame at burners, if needed

		/*
		 * int[] flame = stove.getBurners();
		 * 
		 * if (flame[0] > 0) canvas.drawBitmap(mFireOne, mX, mY, null); if
		 * (flame[1] > 0) canvas.drawBitmap(mFireTwo, mX, mY, null); if
		 * (flame[2] > 0) canvas.drawBitmap(mFireThree, mX, mY, null); if
		 * (flame[3] > 0) canvas.drawBitmap(mFireFour, mX, mY, null);
		 */
		// if (touched)
		// {
		for (int i = 0; i < 4; i++) {
			isOnBurners.set(i, stove.isOnBurner(new Integer(i)));
		}
		// }
		if (isOnBurners.get(0))
			canvas.drawBitmap(mFireOne, mX, mY, null);
		if (isOnBurners.get(1))
			canvas.drawBitmap(mFireTwo, mX, mY, null);
		if (isOnBurners.get(2))
			canvas.drawBitmap(mFireThree, mX, mY, null);
		if (isOnBurners.get(3))
			canvas.drawBitmap(mFireFour, mX, mY, null);
		touched = false;
	}

	public boolean onTouchEvent(MotionEvent event) {

		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		Log.d(TAG, "X = " + x + " and Y = " + y);

		int color;

		try {
			// Check which button has clicked

			// Get the pixel color of coordinate (x, y) (translated)
			color = mButtons.getPixel(x - mX, y - mY);

			int burnerIndex = -1;

			switch (color) {
			case Color.RED: // 0xfffa0000:
				// ((StoveView) getContext()).showPopup(0);
				// data.setBurnerIntensity(0, 100 - data.getBurnerIntensity(0));
				burnerIndex = 0;

				Log.d(TAG, "Burner 1");

				break;
			case Color.BLUE:
				// ((StoveView) getContext()).showPopup(1);
				// data.setBurnerIntensity(1, 100 - data.getBurnerIntensity(1));
				burnerIndex = 1;

				Log.d(TAG, "Burner 2");

				break;
			case Color.GREEN:
				// ((StoveView) getContext()).showPopup(2);
				// data.setBurnerIntensity(2, 100 - data.getBurnerIntensity(2));
				burnerIndex = 2;

				Log.d(TAG, "Burner 3");

				break;
			case Color.YELLOW:
				// ((StoveView) getContext()).showPopup(3);
				// data.setBurnerIntensity(3, 100 - data.getBurnerIntensity(3));
				burnerIndex = 3;

				Log.d(TAG, "Burner 4");

				break;
			default:
				Log.d(TAG, "Nothing...");
			}

			if (burnerIndex != -1) {
				touched = false;
				if (stove.isOnBurner(burnerIndex))
					stove.turnOffBurner(burnerIndex);
				else
					stove.turnOnBurner(burnerIndex);
				touched = true;
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
