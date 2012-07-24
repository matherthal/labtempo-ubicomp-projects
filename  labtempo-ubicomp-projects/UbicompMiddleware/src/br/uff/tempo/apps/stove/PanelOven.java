package br.uff.tempo.apps.stove;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;
import br.uff.tempo.R;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class PanelOven extends Panel implements SurfaceHolder.Callback {

	private final String TAG = "Panel-StoveView";

	private IStove stove;

	private Bitmap mBitmap;
	private Bitmap mButtons;

	private ViewThread mThread;

	public PanelOven(Context context) {
		super(context);
		init();
	}

	public PanelOven(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	protected void init() {

		super.init();

		stove = ((StoveView) getContext()).getStoveState();
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.oven);

		mButtons = BitmapFactory.decodeResource(getResources(), R.drawable.oven_buttons);

		mX -= mBitmap.getWidth() / 2;
		mY -= mBitmap.getHeight() / 2;
	}

	public void doDraw(Canvas canvas) {

		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the stove bitmap
		canvas.drawBitmap(mBitmap, mX, mY, null);
	}

	public boolean onTouchEvent(MotionEvent event) {

		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		stove = ((StoveView) getContext()).getStoveState();

		Log.d(TAG, "X = " + x + " and Y = " + y);

		int color;
		String msg;

		try {
			// Check which button has clicked

			// get the pixel color of coordinate (x, y) (translated)
			color = mButtons.getPixel(x - mX, y - mY);

			switch (color) {

			case Color.RED:

				Log.d(TAG, "Left Button");

					if (stove.isOvenOn()) {
						msg = "off";
						stove.setOvenTemperature(0f);
					} else {
						msg = "on";
						stove.setOvenTemperature(100.0f);
					}

				Toast.makeText(getContext(), "Oven turned " + msg, Toast.LENGTH_SHORT).show();
				break;

			case Color.YELLOW:

				Log.d(TAG, "Right Button");

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
