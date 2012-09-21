package br.uff.tempo.middleware.management.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.management.Place;

public class Space {

	public static final int PIXEL_PER_METER = 96;
	
	private float width;
	private float height;

	private Map<String, Place> map;
	private int pixelFactor;

	public Space() {
		this(0f, 0f);
	}
	
	public Space(float width, float height) {
		this(width, height, PIXEL_PER_METER);
	}
	
	public Space(float width, float height, int pixelFactor) {
		
		map = new HashMap<String, Place>();
		
		this.width = width;
		this.height = height;
		
		this.pixelFactor = pixelFactor;
	}
	
	public float invertYcoordinate(float yCoordinate) {
		return height - yCoordinate;
	}
		
	public void addPlace(Place place) {
		map.put(place.getName(), place);
	}
	
	public void removePlace(Place place) {
		map.remove(place.getName());
	}
	
	public static float pixelToMeters(int pixel, int pixelFactor) {
		
		return ((float) pixel) / pixelFactor;
	}
	
	public float pixelToMeters(int pixel) {
		return ((float) pixel) / this.pixelFactor;
	}
	
	public static int metersToPixel(float meter, int pixelFactor) {
		return (int) meter * pixelFactor;
	}
	
	public int metersToPixel(float meter) {
		return (int) meter * this.pixelFactor;
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
