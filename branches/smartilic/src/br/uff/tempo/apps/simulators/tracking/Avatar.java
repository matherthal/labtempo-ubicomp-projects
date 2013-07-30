package br.uff.tempo.apps.simulators.tracking;

import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import br.uff.tempo.middleware.management.Person;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public class Avatar {

	private int centerX;
	private int centerY;
	private int radius;
	private Paint paint;
	private Rect rect;
	private Person person;

	private static int pixelFactor = 0;
	private static Space houseMap;

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

		this.person = new Person(this.name, this.name);
		this.person.identify();
	}

	public boolean contains(int x, int y) {

		return rect.contains(x, y);
	}

	public void storePosition() {

		float x = Space.pixelToMeters(centerX, pixelFactor);
		float y = houseMap.invertYcoordinate(Space.pixelToMeters(centerY,
				pixelFactor));

		Log.i("TrackingPanel", "[" + x + ", " + y + "]");

		this.person.addPosition(new Position(x, y));
	}

	// Getters and Setters
	public void setCenter(int centerX, int centerY) {

		this.centerX = centerX;
		this.centerY = centerY;

		rect.offsetTo(centerX - len, centerY - len);
	}
	
	public void setAndStorePosition(int x, int y) {
		setCenter(x, y);
		storePosition();
	}

	public static void setPixelFactor(int factor) {
		pixelFactor = factor;
	}

	public static void setSpace(Space space) {
		houseMap = space;
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
