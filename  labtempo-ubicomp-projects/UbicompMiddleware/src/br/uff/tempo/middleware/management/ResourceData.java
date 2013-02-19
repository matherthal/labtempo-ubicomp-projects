package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.Position;

public class ResourceData {

	public static final int RANS = 0;
	public static final int NAME = 1;  
	public static final int TYPE = 2;
	public static final int POSITION = 3;
	public static final int PLACE = 4;
	
	private String rans;
	private String name;
	private String type;
	private Position position;
	private Place place;
	private String tag;
	
	//(rans, ip, prefix)
	private ResourceAgentNS resourceAgentNS; 
	
	public ResourceData(String rans, String name, String type, Position position, Place place,
			ResourceAgentNS resourceAgentNS) {
		this(rans, name, type, position, place);
		this.resourceAgentNS = resourceAgentNS;
	}

	public ResourceData(String rans, String name, String type, Position position, Place place) {
		this.rans = rans;
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
	
	public String getRans() {
		return rans;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public ResourceAgentNS getResourceAgentNS() {
		return resourceAgentNS;
	}

	public void setResourceAgentNS(ResourceAgentNS resourceAgentNS) {
		this.resourceAgentNS = resourceAgentNS;
	}
}
