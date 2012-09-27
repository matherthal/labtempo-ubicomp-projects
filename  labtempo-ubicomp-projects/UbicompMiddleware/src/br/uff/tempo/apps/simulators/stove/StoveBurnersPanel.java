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
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class StoveBurnersPanel extends AbstractPanel {

	private static final String TAG = "SmartAndroid";
	private Bitmap mBitmap;
	private Bitmap mButtons;

	private Bitmap mFireOne;
	private Bitmap mFireTwo;
	private Bitmap mFireThree;
	private Bitmap mFireFour;

	private int pointX;
	private int pointY;

	private IStove stove;

	public StoveBurnersPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected final void init() {

		super.init();

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

		stove = (IStove) ((StoveView) getContext()).getAgent();
	}

	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the stove bitmap
		canvas.drawBitmap(mBitmap, pointX, pointY, null);

		// draw the flame at burners, if needed

		if (stove.isOnBurner(0)) {
			canvas.drawBitmap(mFireOne, pointX, pointY, null);
		}
		if (stove.isOnBurner(1)) {
			canvas.drawBitmap(mFireTwo, pointX, pointY, null);
		}
		if (stove.isOnBurner(2)) {
			canvas.drawBitmap(mFireThree, pointX, pointY, null);
		}
		if (stove.isOnBurner(3)) {
			canvas.drawBitmap(mFireFour, pointX, pointY, null);
		}

		Log.d(TAG, "PanelBurners Screen repainted");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

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

					if (stove.isOnBurner(burnerIndex)) {
						stove.turnOffBurner(burnerIndex);
					} else {
						stove.turnOnBurner(burnerIndex);
					}
				}

				Log.d(TAG, "Color Clicked = " + Integer.toHexString(color));
				
				invalidate();

			} catch (IllegalArgumentException ex) {
				Log.d(TAG, "Exception... " + ex);
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onUpdate(String method, Object value) {
		// TODO Auto-generated method stub
		
	}
}
