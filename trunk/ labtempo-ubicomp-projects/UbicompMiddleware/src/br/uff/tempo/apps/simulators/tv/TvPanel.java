package br.uff.tempo.apps.simulators.tv;

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
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class TvPanel extends AbstractPanel {

	private final String TAG = "Panel-TvView";

	private ITelevision agent;
	private Bitmap mBitmap;

	private int pointX;
	private int pointY;

	public TvPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public final void initialization() {

		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tv);

		// This will calculate the bitmap (x,y) coordinate, when its center is
		// on screen center
		pointX = getScreenCenterX() - mBitmap.getWidth() / 2;
		pointY = getScreenCenterY() - mBitmap.getHeight() / 2;

		agent = (ITelevision) ((TvView) getContext()).getAgent();
	}

	@Override
	public void onUpdate(String method, Object value) {
		Log.i(TAG, "Method = " + method + " and value = " + value);

		if (method.equalsIgnoreCase("showMessage")) {

			Log.i(TAG, "showMessage called");
			Toast.makeText(getContext(), value.toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void touch(MotionEvent event) {
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			agent.showMessage("Teste");
		}
	}

	@Override
	public void drawCanvas(Canvas canvas) {
		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the tv bitmap
		canvas.drawBitmap(mBitmap, pointX, pointY, null);
	}

	@Override
	public void setAgent(IResourceAgent agent) {
		this.agent = (ITelevision) agent;
	}
	
	@Override
	public IResourceAgent getAgent() {
		return this.agent;
	}
}
