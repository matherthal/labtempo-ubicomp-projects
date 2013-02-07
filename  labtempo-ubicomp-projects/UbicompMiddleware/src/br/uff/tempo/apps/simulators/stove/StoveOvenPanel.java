package br.uff.tempo.apps.simulators.stove;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import br.uff.tempo.R;

public class StoveOvenPanel extends StovePanel {

	private final String TAG = "Stove-PanelOven";

	private Bitmap mBitmap;
	private Bitmap mButtons;

	private int pointX;
	private int pointY;

	public StoveOvenPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public final void initialization() {

		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.oven);

		mButtons = BitmapFactory.decodeResource(getResources(),
				R.drawable.oven_buttons);

		// This will calculate the bitmap (x,y) coordinate, when its center is
		// on screen center
		pointX = getScreenCenterX() - mBitmap.getWidth() / 2;
		pointY = getScreenCenterY() - mBitmap.getHeight() / 2;
	}

	@Override
	public void onUpdate(String method, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void touch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// get the touch coordinates
			int x = (int) event.getX();
			int y = (int) event.getY();

			Log.d(TAG, "X = " + x + " and Y = " + y);

			int color;
			String msg;

			try {
				// Check which button has clicked

				// get the pixel color of coordinate (x, y) (translated)
				color = mButtons.getPixel(x - pointX, y - pointY);

				switch (color) {

				case Color.RED:

					Log.d(TAG, "Left Button");

					if (agent.isOvenOn()) {
						msg = "off";
						agent.setOvenTemperature(0f);
					} else {
						msg = "on";
						agent.setOvenTemperature(100.0f);
					}

					Toast.makeText(getContext(), "Oven turned " + msg,
							Toast.LENGTH_SHORT).show();
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
		}
	}

	@Override
	public void drawCanvas(Canvas canvas) {
		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the stove bitmap
		canvas.drawBitmap(mBitmap, pointX, pointY, null);
	}
}
