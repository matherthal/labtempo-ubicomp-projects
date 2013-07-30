package br.uff.tempo.apps.simulators.bed;

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
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class BedPanel extends AbstractPanel {

	private final String TAG = "Panel-BedView";

	private boolean occuped = false;

	// private BedData bed;
	private IBed agent;
	private Bitmap mBitmap;

	private int pointX;
	private int pointY;

	public BedPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public final void initialization() {

		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bed);

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

			occuped = !occuped;

			Log.d(TAG, "X = " + x + " and Y = " + y);

			String msg;

			if (occuped) {
				msg = "There is someone in the bed";
				agent.lieDown();
			} else {
				msg = "There is no one in the bed";
				agent.getOut();
			}

			Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
			invalidate();
		}
	}

	@Override
	public void drawCanvas(Canvas canvas) {
		// draw the background color
		canvas.drawColor(Color.BLACK);
		// draw the bed bitmap
		canvas.drawBitmap(mBitmap, pointX, pointY, null);
	}

	@Override
	public void setAgent(IResourceAgent agent) {
		this.agent = (IBed) agent;
	}
	
	@Override
	public IResourceAgent getAgent() {
		return this.agent;
	}

}
