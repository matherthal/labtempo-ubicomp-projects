package br.uff.tempo.apps.simulators.stove;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import br.uff.tempo.R;

public class StoveBurnersPanel extends StovePanel {

	private static final String TAG = "SmartAndroid";
	private Bitmap mBitmap;
	private Bitmap mButtons;

	private Bitmap mFireOne;
	private Bitmap mFireTwo;
	private Bitmap mFireThree;
	private Bitmap mFireFour;

	private int pointX;
	private int pointY;

	public StoveBurnersPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public final void initialization() {
		
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

		// This will calculate the bitmap (x,y) coordinate, when its center is
		// on screen center
		pointX = getScreenCenterX() - mBitmap.getWidth() / 2;
		pointY = getScreenCenterY() - mBitmap.getHeight() / 2;
	}

	@Override
	public void onUpdate(String method, Object value) {}

	@Override
	public void touch(MotionEvent event) {
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// get the touch coordinates
			int x = (int) event.getX();
			int y = (int) event.getY();

			Log.d(TAG, "X = " + x + " and Y = " + y);

			int color;

			try {
				// Check which button has clicked

				// Get the pixel color of coordinate (x, y) (translated)
				color = mButtons.getPixel(x - pointX, y - pointY);

				int burnerIndex = -1;

				switch (color) {
				case Color.RED:

					burnerIndex = 0;
					Log.d(TAG, "Burner 1");

					break;
				case Color.BLUE:

					burnerIndex = 1;
					Log.d(TAG, "Burner 2");

					break;
				case Color.GREEN:

					burnerIndex = 2;
					Log.d(TAG, "Burner 3");

					break;
				case Color.YELLOW:

					burnerIndex = 3;
					Log.d(TAG, "Burner 4");

					break;
				default:
					Log.d(TAG, "Nothing...");
				}

				if (burnerIndex != -1) {

					Log.d(TAG, "A burner knob was clicled...");

					if (agent.isOnBurner(burnerIndex)) {
						agent.turnOffBurner(burnerIndex);
					} else {
						agent.turnOnBurner(burnerIndex);
					}
				}

				Log.d(TAG, "Color Clicked = " + Integer.toHexString(color));

				invalidate();

			} catch (IllegalArgumentException ex) {
				Log.d(TAG, "Exception... " + ex);
			}
		}
	}

	@Override
	public void drawCanvas(Canvas canvas) {

		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the stove bitmap
		canvas.drawBitmap(mBitmap, pointX, pointY, null);

		// draw the flame at burners, if needed

		if (agent.isOnBurner(0)) {
			canvas.drawBitmap(mFireOne, pointX, pointY, null);
		}
		if (agent.isOnBurner(1)) {
			canvas.drawBitmap(mFireTwo, pointX, pointY, null);
		}
		if (agent.isOnBurner(2)) {
			canvas.drawBitmap(mFireThree, pointX, pointY, null);
		}
		if (agent.isOnBurner(3)) {
			canvas.drawBitmap(mFireFour, pointX, pointY, null);
		}

		Log.d(TAG, "PanelBurners Screen repainted");
	}
}
