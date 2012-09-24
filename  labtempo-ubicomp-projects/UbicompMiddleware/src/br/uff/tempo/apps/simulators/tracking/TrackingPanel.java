package br.uff.tempo.apps.simulators.tracking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import br.uff.tempo.apps.map.objects.ResourceObject;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.middleware.management.Place;
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
	private Map<String, Rect> rooms;
	private Paint paint;
	
	private int mode;

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
		
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.rgb(0, 255, 0));
		paint.setStrokeWidth(10);
		
		setupRooms();
	}

	private void setupRooms() {
		
		homeMap = rLocation.getMap();
		
		rooms = new HashMap<String, Rect>();
		updateRectangles();
	}
	
	public void updateRectangles() {
	
		float mapWidth = homeMap.getWidth();
		float mapHeight = homeMap.getHeight();
		
		int factor = (int) (getScreenWidth() / mapWidth);

		int i = 0;
		for (Place place : homeMap.getAllPlaces()) {
			
			int left   = Space.metersToPixel(place.getLower().getX(), factor);
			int top    = Space.metersToPixel(homeMap.invertYcoordinate(place.getUpper().getY()), factor);
			int right  = Space.metersToPixel(place.getUpper().getX(), factor);
			int bottom = Space.metersToPixel(homeMap.invertYcoordinate(place.getLower().getY()), factor);

			rooms.put(place.getName(), new Rect(left, top, right, bottom));
			
			i++;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		
		for (Rect rect : rooms.values()) {
			
			canvas.drawRect(rect, paint);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			
			// get the touch coordinates
			int x = (int) event.getX();
			int y = (int) event.getY();
		
			for (Map.Entry<String, Rect> entry : rooms.entrySet()) {
			
				if (entry.getValue().contains(x, y)) {
					
					Toast.makeText(getContext(), entry.getKey(), Toast.LENGTH_SHORT).show();
				}
			}
			
			invalidate();
		}

		return true;
	}

	public void addPerson() {
				
	}
}
