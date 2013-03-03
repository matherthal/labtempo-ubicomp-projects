package br.uff.tempo.apps.simulators.tracking.surface;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import br.uff.tempo.apps.simulators.tracking.Avatar;
import br.uff.tempo.apps.simulators.tracking.mode.MovementsHandler;
import br.uff.tempo.apps.simulators.tracking.mode.TrackingMode;
import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Space;

public abstract class AbstractTrackingPanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private TrackingMode mode = TrackingMode.MANUAL_MOVE;
	public static final int RADIUS = 20;
	public static final long STEP_TIME = 500;

	private int screenCenterX;
	private int screenCenterY;

	private int screenWidth;
	private int screenHeight;

	private float scale;
	private IResourceLocation rLocation;

	private Space homeMap;
	private Map<String, Rect> rooms;
	private List<Avatar> users;
	private LinkedList<Point> pathPoints;

	private Paint paintRooms;

	private int factor;
	private MovementsHandler movementsHandler;
	private Paint paintPath;
	private UpdateThread updateThread;

	public AbstractTrackingPanel(Context context) {
		super(context);
		init();
	}

	public AbstractTrackingPanel(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}

	public final void init() {

		getHolder().addCallback(this);
		updateThread = new UpdateThread(this);

		setFocusable(true); // make sure we get key events

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		// get the screen length
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;

		// get the screen center coordinates
		screenCenterX = screenWidth / 2;
		screenCenterY = screenHeight / 2;

		scale = getResources().getDisplayMetrics().density;

		paintRooms = new Paint();
		paintRooms.setStyle(Paint.Style.STROKE);
		paintRooms.setColor(Color.WHITE);
		paintRooms.setStrokeWidth(dpTopixel(10));

		paintPath = new Paint();
		paintPath.setStyle(Paint.Style.STROKE);
		paintPath.setColor(Color.WHITE);
		paintPath.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));

		rLocation = new ResourceLocationStub(IResourceLocation.rans);
		movementsHandler = new MovementsHandler();

		setupRooms();
	}

	@Override
	protected abstract void onDraw(Canvas canvas);

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

		// Defines a conversion factor between Pixels and meters
		// so, get the greater factor between screen width and screen height
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

		Avatar.setPixelFactor(factor);
		Avatar.setSpace(homeMap);
	}

	public void setMode(TrackingMode mode) {

		if (mode == TrackingMode.DEFINE_TRACK
				|| mode == TrackingMode.MANUAL_MOVE) {
			pathPoints = new LinkedList<Point>();
		}

		this.mode = mode;
	}

	public TrackingMode getMode() {
		return this.mode;
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

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		updateThread.setRunning(true);
		updateThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		updateThread.setRunning(false);
		while (retry) {
			try {
				updateThread.join();
				retry = false;
			} catch (InterruptedException e) {}
		}
	}

	public List<Avatar> getUsers() {
		return users;
	}

	public LinkedList<Point> getPathPoints() {
		return pathPoints;
	}

	public MovementsHandler getMovementsHandler() {
		return movementsHandler;
	}

	public Paint getPaintRooms() {
		return paintRooms;
	}

	public Paint getPaintPath() {
		return paintPath;
	}

	public Map<String, Rect> getRooms() {
		return rooms;
	}
}
