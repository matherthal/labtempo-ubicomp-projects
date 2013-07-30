package br.uff.tempo.middleware.management.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.management.Place;

public class Space implements Serializable {

	// Constants

	public static final int PIXEL_PER_METER = 96;
	private static final int EPSOLON = 32;

	// Fields

	private float width;
	private float height;

	private Map<String, Place> map;
	private int pixelFactor;

	// Constructors

	public Space() {
		this(0f, 0f);
	}

	public Space(float width, float height) {
		this(width, height, PIXEL_PER_METER);
	}

	public Space(int pWidth, int pHeight) {

		this(pWidth, pHeight, PIXEL_PER_METER);
	}

	public Space(int pWidth, int pHeight, int pixelFactor) {

		this(pixelToMeters(pWidth, pixelFactor), pixelToMeters(pHeight, pixelFactor), pixelFactor);
	}

	public Space(float width, float height, int pixelFactor) {

		map = new HashMap<String, Place>();

		this.width = width;
		this.height = height;

		this.pixelFactor = pixelFactor;
	}

	// Helper methods

	public static float pixelToMeters(int pixel, int pixelFactor) {

		float ret = pixel;
		ret /= pixelFactor;

		return Position.roundTo2(ret);
	}

	public float pixelToMeters(int pixel) {

		return pixelToMeters(pixel, this.pixelFactor);
	}

	public static int metersToPixel(float meter, int pixelFactor) {
		return (int) (meter * pixelFactor);
	}

	public int metersToPixel(float meter) {
		return metersToPixel(meter, this.pixelFactor);
	}

	public float invertYcoordinate(float yCoordinate) {
		return height - yCoordinate;
	}

	// Add / Remove Places (rooms)

	public void addPlace(Place place) {
		map.put(place.getName(), place);
	}

	public void removePlace(Place place) {
		map.remove(place.getName());
	}

	// Getters and Setters

	public int getPixelFactor() {
		return this.pixelFactor;
	}

	public void setPixelFactor(int pixelFactor) {
		this.pixelFactor = pixelFactor;
	}

	public int getRoomsNumber() {
		return map.size();
	}

	public Place getPlace(String name) {

		return map.get(name);
	}

	public Collection<Place> getAllPlaces() {
		return map.values();
	}

	public Set<String> getPlacesNames() {
		return map.keySet();
	}

	public Map<String, Place> getPlaceMap() {
		return map;
	}

	public void setPlaceMap(Map<String, Place> newMap) {
		this.map = newMap;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
