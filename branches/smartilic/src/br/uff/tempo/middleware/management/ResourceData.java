package br.uff.tempo.middleware.management;

import br.uff.tempo.middleware.management.utils.Position;

/**
 * Resource Agent data class 
 * <br /><br />
 * It contains rans, type, position and place
 */
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
	
	/**
	 * Constructor that additionally involves resourceAgentNS for communication issues
	 * @param rans reference of Resource Agent
	 * @param name public defined name
	 * @param type hierachical type in string format
	 * @param position
	 * @param place subspace localization
	 * @param resourceAgentNS contains (rans, ip, prefix) for communication access 
	 */
	public ResourceData(String rans, String name, String type, Position position, Place place,
			ResourceAgentNS resourceAgentNS) {
		this(rans, name, type, position, place);
		this.resourceAgentNS = resourceAgentNS;
	}

	/**
	 * Constructor without resourceAgentNS
	 * @param rans reference of Resource Agent
	 * @param name public defined name
	 * @param type hierachical type in string format
	 * @param position
	 * @param place subspace localization
	 */
	public ResourceData(String rans, String name, String type, Position position, Place place) {
		this.rans = rans;
		this.name = name;
		this.type = type;
		this.position = position;
		this.place = place;	
	}
	
	/**
	 * Get access method for position
	 * @return position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Set method for position
	 * @param position
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Get access method for place
	 * @return place
	 */
	public Place getPlace() {
		return place;
	}

	/**
	 * Set method for place
	 * @param place
	 */
	public void setPlace(Place place) {
		this.place = place;
	}

	/**
	 * Get access method for name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get access method for type
	 * @return type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Get access method for rans
	 * @return rans
	 */
	public String getRans() {
		return rans;
	}

	/**
	 * Get access method for tag
	 * @return tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Set method for tag
	 * @param tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Get access method for resourceAgentNS
	 * @return resourceAgentNS
	 */
	ResourceAgentNS getResourceAgentNS() {
		return resourceAgentNS;
	}

	/**
	 * Set method for resourceAgentNS
	 * @param resourceAgentNS
	 */
	void setResourceAgentNS(ResourceAgentNS resourceAgentNS) {
		this.resourceAgentNS = resourceAgentNS;
	}
}
