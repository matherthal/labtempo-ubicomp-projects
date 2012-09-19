package br.uff.tempo.apps.simulators.tv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import br.uff.tempo.R;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class TvPanel extends AbstractPanel {

	private final String TAG = "Panel-BedView";

	private String msg = "Teste";
	Paint msgPaint;

	private ITelevision agent;
	private Bitmap mBitmap;

	private float centerY;
	private float centerX;

	private int pointX;
	private int pointY;

	public TvPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected final void init() {

		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tv);

		// This will calculate the bitmap (x,y) coordinate, when its center is
		// on screen center
		pointX = getScreenCenterX()- mBitmap.getWidth() / 2;
		pointY = getScreenCenterY() - mBitmap.getHeight() / 2;
		
		agent = (ITelevision) ((TvView) getContext()).getAgent();
		
		msgPaint = new Paint();
	}

	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		// draw the background color
		canvas.drawColor(Color.BLACK);

		// draw the tv bitmap
		canvas.drawBitmap(mBitmap, pointX, pointY, null);

		// Draw the TV message
		canvas.drawText(msg, centerX, centerY, msgPaint);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			invalidate();
		}

		return true;
	}
}