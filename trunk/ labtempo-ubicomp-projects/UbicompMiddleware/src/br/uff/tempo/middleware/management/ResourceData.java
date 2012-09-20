package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.Position;

public class ResourceData {

	String name;
	String type;
	Position position;
	Place place;
	
	public ResourceData (String name, String type,	Position position,	Place place){
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

}
