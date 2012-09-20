package br.uff.tempo.middleware.management.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.management.interfaces.IPlace;

public class Space {

	public static final int PIXEL_PER_METER = 96;
	
	private float width;
	private float height;

	private Map<String, IPlace> map;
	private int pixelFactor;

	public Space() {
		this(0f, 0f);
	}
	
	public Space(float width, float height) {
		this(width, height, PIXEL_PER_METER);
	}
	
	public Space(float width, float height, int pixelFactor) {
		
		map = new HashMap<String, IPlace>();
		
		this.width = width;
		this.height = height;
		
		this.pixelFactor = pixelFactor;
	}
	
	public float invertYcoordinate(float yCoordinate) {
		return height - yCoordinate;
	}
		
	public void addPlace(IPlace place) {
		map.put(place.getName(), place);
	}
	
	public void removePlace(IPlace place) {
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
	
	public IPlace getPlace(String name) {
		
		return map.get(name);
	}
	
	public Collection<IPlace> getAllPlaces() {
		return map.values();
	}
	
	public Set<String> getPlacesNames() {
		return map.keySet();
	}

	public Map<String, IPlace> getPlaceMap() {
		return map;
	}
	
	public void setPlaceMap(Map<String, IPlace> newMap) {
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
