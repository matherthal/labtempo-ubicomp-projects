package br.uff.tempo.apps.prenda.tracking;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;
import br.uff.tempo.apps.prenda.IPrenda;
import br.uff.tempo.apps.prenda.PrendaAgent;
import br.uff.tempo.apps.prenda.PrendaStub;
import br.uff.tempo.middleware.management.Person;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.management.stubs.ResourceLocationStub;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

class Avatar {
	
	private static final double FLAG = 1.0d;

	private int centerX;
	private int centerY;
	private int radius;
	private Paint paint;
	private Rect rect;
	private Person person;
	private Position prendaPosition;
	private Context context;

	private int pixelFactor = 0;
	private Space houseMap;

	private final int DELTA = 10;
	private int len;

	private String name;

	public Avatar(int centerX, int centerY, int radius) {
		this(null, null, centerX, centerY, radius, null);
	}

	public Avatar(int centerX, int centerY, int radius, Paint paint) {
		this(null, null, centerX, centerY, radius, paint);
	}

	public Avatar(Context context, String name, int centerX, int centerY, int radius, Paint paint) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.paint = paint;
		this.name = name;
		this.context = context;

		// A rectangle that has the circle inside
		len = radius + DELTA;
		rect = new Rect(centerX - len, centerY - len, centerX + len, centerY
				+ len);
		
		IResourceDiscovery resourceDiscovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		List<ResourceData> listResource = resourceDiscovery.search(ResourceData.TYPE, PrendaAgent.class.getSimpleName());
		
		if (listResource != null) {
			IPrenda prenda = new PrendaStub(listResource.get(0).getRans());
			prendaPosition = prenda.getPosition();
		} else {
			// definir prenda
			IResourceLocation resourceLocation = new ResourceLocationStub(IResourceLocation.rans);
			Space map = resourceLocation.getMap();
			float height = map.getHeight();
			float width = map.getWidth();
			prendaPosition = new Position((float) (width * Math.random()), (float) (height * Math.random()));
			IPrenda novaPrenda = new PrendaAgent("Prenda", "PrendaAgent", "prenda.ra", prendaPosition);
			novaPrenda.identify();
		}
		

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
		
		double distance = prendaPosition.getDistance(new Position(x, y));
		
		if (distance < FLAG) {
			// FAZER O CARA VENCER
			Toast.makeText(context, "Venceu!!!", Toast.LENGTH_LONG).show();
		} else {
			// FAZER UM BIP SOAR
			int intDistance = (int) Math.round(distance);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < intDistance; ++i)
				sb.append("o");
			String message = sb.toString();
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
		
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
