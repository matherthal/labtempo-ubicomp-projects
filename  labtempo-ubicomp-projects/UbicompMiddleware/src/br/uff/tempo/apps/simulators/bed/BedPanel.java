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
		init();
	}

	protected final void init() {

		super.init();
		
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bed);

		// This will calculate the bitmap (x,y) coordinate, when its center is
		// on screen center
		pointX = getScreenCenterX()- mBitmap.getWidth() / 2;
		pointY = getScreenCenterY() - mBitmap.getHeight() / 2;
		
		agent = (IBed) ((BedView) getContext()).getAgent();
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		// draw the background color
		canvas.drawColor(Color.BLACK);
		// draw the bed bitmap
		canvas.drawBitmap(mBitmap, pointX, pointY, null);
	}

	public boolean onTouchEvent(MotionEvent event) {
		
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

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

		return true;
	}
}
