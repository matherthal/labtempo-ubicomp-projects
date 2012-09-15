package br.uff.tempo.apps.simulators.lamp;

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
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.middleware.resources.interfaces.ILamp;

public class LampPanel extends AbstractPanel {

	private final String TAG = "Panel-LampView";

	private ILamp agent;
	private Bitmap mBitmapOn;
	private Bitmap mBitmapOff;
	
	private int pointX;
	private int pointY;

	public LampPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	protected final void init() {

		super.init();
		
		mBitmapOn = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_on);
		mBitmapOff = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_off);

		// This will calculate the bitmap (x,y) coordinate, when its center is
		// on screen center
		pointX = getScreenCenterX()- mBitmapOn.getWidth() / 2;
		pointY = getScreenCenterY() - mBitmapOn.getHeight() / 2;
		
		agent = (ILamp) ((LampView) getContext()).getAgent();
	}

	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// draw the background color
		canvas.drawColor(Color.WHITE);
		// draw the lamp bitmap

		if (agent.isOn()) {
			canvas.drawBitmap(mBitmapOn, pointX, pointY, null);
		}
		else {
			canvas.drawBitmap(mBitmapOff, pointX, pointY, null);
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			Log.d(TAG, "X = " + x + " and Y = " + y);

			String msg;

			if (agent.isOn()) {
				agent.turnOff();
				msg = "The lamp is off";
			} else {
				agent.turnOn();
				msg = "The lamp is on";
			}

			Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
			invalidate();
		}

		return true;
	}

}
