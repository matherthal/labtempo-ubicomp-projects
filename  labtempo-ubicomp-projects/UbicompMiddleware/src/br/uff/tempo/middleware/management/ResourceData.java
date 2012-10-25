package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.Position;

public class ResourceData {

	public static final int RAI = 0;
	public static final int NAME = 1;  
	public static final int TYPE = 2;
	public static final int POSITION = 3;
	public static final int PLACE = 4;
	
	private String rai;
	private String name;
	private String type;
	private Position position;
	private Place place;
	private String tag;
	
	public ResourceData(String rai, String name, String type, Position position, Place place) {
		this.rai = rai;
		this.name = name;
		this.type = type;
		this.position = position;
		this.place = place;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	public String getRai() {
		return rai;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
