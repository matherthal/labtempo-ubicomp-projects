package br.uff.tempo.apps.simulators.tracking;

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
import android.util.Log;
import android.view.MotionEvent;
import br.uff.tempo.apps.simulators.AbstractPanel;
import br.uff.tempo.middleware.management.Person;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public class TrackingPanel extends AbstractPanel {

	private static final int RADIUS = 10;
	private final String TAG = "Panel-TrackingView";

	private IResourceLocation rLocation;
	private IResourceDiscovery rds;

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

	public TrackingPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	public final void initialization() {

		super.init();

		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(dpTopixel(10));

		rds = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		List<String> result = rds.search("ResourceLocation");

		if (result != null) {
			rLocation = new ResourceLocationStub(result.get(0));

			setupRooms();

		} else {
			Log.e(TAG, "ResourceLocation doesn't exist...");
		}
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

		int i = 0;
		for (Place place : homeMap.getAllPlaces()) {

			int left = Space.metersToPixel(place.getLower().getX(), factor);
			int top = Space.metersToPixel(
					homeMap.invertYcoordinate(place.getUpper().getY()), factor);
			int right = Space.metersToPixel(place.getUpper().getX(), factor);
			int bottom = Space.metersToPixel(
					homeMap.invertYcoordinate(place.getLower().getY()), factor);

			rooms.put(place.getName(), new Rect(left, top, right, bottom));

			i++;
		}

		for (Avatar usr : users) {
			usr.setPixalFactor(factor);
		}
	}

	public void addPerson(String name) {

		Paint p = new Paint();
		p.setColor(colors[counter]);

		Avatar usr = new Avatar(name, getScreenCenterX(), getScreenCenterY(),
				dpTopixel(RADIUS), p);

		usr.setPixalFactor(factor);
		usr.setSpace(homeMap);

		users.add(usr);

		counter = (counter + 1) % colors.length;

		invalidate();
	}

	@Override
	public void onUpdate(String method, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touch(MotionEvent event) {
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
	}

	@Override
	public void drawCanvas(Canvas canvas) {
		
		for (Rect rect : rooms.values()) {

			canvas.drawRect(rect, paint);
		}

		for (Avatar circ : users) {
			canvas.drawCircle(circ.getCenterX(), circ.getCenterY(),
					circ.getRadius(), circ.getPaint());
		}
	}

	@Override
	public void setAgent(IResourceAgent agent) {
		// TODO Auto-generated method stub
	}
}

class Avatar {

	private int centerX;
	private int centerY;
	private int radius;
	private Paint paint;
	private Rect rect;
	private Person person;

	private int pixelFactor = 0;
	private Space houseMap;

	private final int DELTA = 10;
	private int len;

	private String name;

	public Avatar(int centerX, int centerY, int radius) {
		this(null, centerX, centerY, radius, null);
	}

	public Avatar(int centerX, int centerY, int radius, Paint paint) {
		this(null, centerX, centerY, radius, paint);
	}

	public Avatar(String name, int centerX, int centerY, int radius, Paint paint) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.paint = paint;
		this.name = name;

		// A rectangle that has the circle inside
		len = radius + DELTA;
		rect = new Rect(centerX - len, centerY - len, centerX + len, centerY
				+ len);

		this.person = new Person(this.name);
		this.person.identify();
	}

	public boolean contains(int x, int y) {

		return rect.contains(x, y);
	}

	public void storePosition() {

		float x = Space.pixelToMeters(centerX, pixelFactor);
		float y = houseMap.invertYcoordinate(Space.pixelToMeters(centerY,
				pixelFactor));

		Log.i("SmartAndroid", "[" + x + ", " + y + "]");

		this.person.addPosition(new Position(x, y));
	}

	// Getters and Setters
	public void setCenter(int centerX, int centerY) {

		this.centerX = centerX;
		this.centerY = centerY;

		rect.offsetTo(centerX - len, centerY - len);
	}

	public void setPixalFactor(int factor) {
		this.pixelFactor = factor;
	}

	public void setSpace(Space space) {
		this.houseMap = space;
	}

	public int getCenterY() {
		return centerY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
}