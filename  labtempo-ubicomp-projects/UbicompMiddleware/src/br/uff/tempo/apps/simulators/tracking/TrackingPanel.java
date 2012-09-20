package br.uff.tempo.apps.simulators.tracking;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.interfaces.IPlace;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Space;
import br.uff.tempo.middleware.resources.interfaces.IPerson;

public class TrackingPanel extends AbstractPanel {

	private final String TAG = "Panel-TrackingView";
	private IResourceLocation rLocation;
	private IResourceDiscovery rds;
	private IPerson agent;
	private Space homeMap;
	private RectF[] rooms;

	public TrackingPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	protected final void init() {

		super.init();

		rds = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		List<String> result = rds.search("ResourceLocation");
		
		if (result != null) {
			rLocation = new ResourceLocationStub(result.get(0));
		}
		else {
			Log.i(TAG, "ResourceLocation doesn't exist...");
		}
		
		agent = (IPerson) ((TrackingView) getContext()).getAgent();
		
		setupRooms();
	}

	private void setupRooms() {
		
		homeMap = rLocation.getMap();
		rooms = new RectF[homeMap.getRoomsNumber()];
		
		float mapWidth = homeMap.getWidth();
		float mapHeight = homeMap.getHeight();

		int i = 0;
		for (IPlace place : homeMap.getAllPlaces()) {
			
			float left   = place.getLower().getX();
			float top    = homeMap.invertYcoordinate(place.getUpper().getY());
			float right  = place.getUpper().getX();
			float bottom = place.getLower().getY();

			rooms[i] = new RectF(left, top, right, bottom);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		
		for (RectF rect : rooms) {
			
			canvas.drawRect(rect, null);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			
			// get the touch coordinates
			int x = (int) event.getX();
			int y = (int) event.getY();

		
			invalidate();
		}

		return true;
	}
}
