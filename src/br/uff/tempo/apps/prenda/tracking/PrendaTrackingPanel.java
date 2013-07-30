package br.uff.tempo.apps.prenda.tracking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Space;

public class PrendaTrackingPanel extends View {//AbstractPanel {

	private static final int RADIUS = 10;
	private final String TAG = "Panel-TrackingView";
	
	private int screenCenterX;
	private int screenCenterY;
	
	private int screenWidth;
	private int screenHeight;
	
	private float scale;

	private IResourceLocation rLocation;
	private IResourceDiscovery rds;
	private Context context;

	private Space homeMap;
	private Map<String, Rect> rooms;
	private List<Avatar> users;

	private Paint paint;

	private int counter = 0;

	private Avatar currentUser;
	private boolean caught;

	private int[] colors = { Color.BLUE, Color.GREEN, Color.CYAN, Color.GRAY,
			Color.MAGENTA, Color.RED, Color.YELLOW };
	private int factor;

	public PrendaTrackingPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		this.context = context;
	}

	public final void init() {

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		//get the screen length
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		//get the screen center coordinates
		screenCenterX = screenWidth / 2;
		screenCenterY = screenHeight / 2;
		
		scale = getResources().getDisplayMetrics().density;
		
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(dpTopixel(10));

		rds = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		rLocation = new ResourceLocationStub(IResourceLocation.rans);

		setupRooms();
	}

	private void setupRooms() {

		homeMap = rLocation.getMap();

		rooms = new HashMap<String, Rect>();
		users = new LinkedList<Avatar>();
		updateRectangles();
	}

	public void updateRectangles() {

		float mapWidth = homeMap.getWidth();
		float mapHeight = homeMap.getHeight();

		float sWidth = getScreenWidth();
		float sHeight = getScreenHeight();

		int factorW = (int) (sWidth / mapWidth + 0.5f);
		int factorH = (int) (sHeight / mapHeight + 0.5f);

		factor = factorW < factorH ? factorW : factorH;

		for (Place place : homeMap.getAllPlaces()) {

			int left = Space.metersToPixel(place.getLower().getX(), factor);
			int top = Space.metersToPixel(
					homeMap.invertYcoordinate(place.getUpper().getY()), factor);
			int right = Space.metersToPixel(place.getUpper().getX(), factor);
			int bottom = Space.metersToPixel(
					homeMap.invertYcoordinate(place.getLower().getY()), factor);

			rooms.put(place.getName(), new Rect(left, top, right, bottom));
		}

		for (Avatar usr : users) {
			usr.setPixalFactor(factor);
		}
	}

	public void addPerson(String name) {

		Paint p = new Paint();
		p.setColor(colors[counter]);

		Avatar usr = new Avatar(context, name, getScreenCenterX(), getScreenCenterY(),
				dpTopixel(RADIUS), p);

		usr.setPixalFactor(factor);
		usr.setSpace(homeMap);

		users.add(usr);

		counter = (counter + 1) % colors.length;

		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// get the touch coordinates
		int x = (int) event.getX();
		int y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			Log.d("TrackingPanel", "Action DOWN");

			Iterator<Avatar> it = users.iterator();

			while (it.hasNext()) {

				Avatar c = it.next();

				if (c.contains(x, y)) {

					currentUser = c;
					caught = true;

					Log.d("TrackingPanel", c.getName() + " was caught");
					break;
				}
			}
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {

			Log.d("TrackingPanel", "Action MOVE");

			if (caught) {

				Log.d("TrackingPanel", "Moving " + currentUser.getName()
						+ " from [" + currentUser.getCenterX() + " "
						+ currentUser.getCenterY() + "] to [" + x + " " + y
						+ "]");

				currentUser.setCenter(x, y);
			}

		} else if (event.getAction() == MotionEvent.ACTION_UP) {

			Log.d("TrackingPanel", "Action UP");

			// for (Map.Entry<String, Rect> entry : rooms.entrySet()) {
			//
			// if (entry.getValue().contains(x, y)) {
			//
			// Toast.makeText(getContext(), entry.getKey(),
			// Toast.LENGTH_SHORT).show();
			// break;
			// }
			// }

			if (currentUser != null && caught) {
				currentUser.storePosition();
			}

			caught = false;
		}
		invalidate();
		
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {

		for (Rect rect : rooms.values()) {

			canvas.drawRect(rect, paint);
		}

		for (Avatar circ : users) {
			canvas.drawCircle(circ.getCenterX(), circ.getCenterY(),
					circ.getRadius(), circ.getPaint());
		}
	}
	
	public float getDensity() {
		return scale;
	}
	
	public int dpTopixel(float dp) {
		return (int) (dp * scale + 0.5f);
	}

	public int getScreenCenterX() {
		return screenCenterX;
	}

	public int getScreenCenterY() {
		return screenCenterY;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
}