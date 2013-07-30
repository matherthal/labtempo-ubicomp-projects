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
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
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
	}

	@Override
	public final void initialization() {

		mBitmapOn = BitmapFactory.decodeResource(getResources(),
				R.drawable.lamp_on);
		mBitmapOff = BitmapFactory.decodeResource(getResources(),
				R.drawable.lamp_off);

		// This will calculate the bitmap (x,y) coordinate, when its center is
		// on screen center
		pointX = getScreenCenterX() - mBitmapOn.getWidth() / 2;
		pointY = getScreenCenterY() - mBitmapOn.getHeight() / 2;
	}
	
	@Override
	public void drawCanvas(Canvas canvas) {
		
		// draw the background color
		canvas.drawColor(Color.WHITE);
		// draw the lamp bitmap

		if (agent.isOn()) {
			canvas.drawBitmap(mBitmapOn, pointX, pointY, null);
		} else {
			canvas.drawBitmap(mBitmapOff, pointX, pointY, null);
		}

		Log.i(TAG, "onDraw from LampPanel called");
	}
	
	@Override
	public void setAgent(IResourceAgent agent) {
		this.agent = (ILamp) agent;
	}
	
	@Override
	public IResourceAgent getAgent() {
		return this.agent;
	}

	@Override
	public void touch(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// get the touch coordinates
			int x = (int) event.getX();
			int y = (int) event.getY();

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
	}

	@Override
	public void onUpdate(String method, Object value) {
		// TODO Auto-generated method stub
	}
}
