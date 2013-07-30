package br.uff.tempo.apps.map.objects.persistence;

import java.io.Serializable;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

/**
 * This class aims to wrap the necessary information to register a resource and
 * enable this information to be passed as parameter easily. Initially, it's
 * just a helper class to MapActivity.
 * 
 * @author dbarreto
 */
public class RegistryData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static float INVALID_POSITION = -931.3f;
	
	private String resourceName;
	private float positionX = INVALID_POSITION;
	private float positionY = INVALID_POSITION;
	private IResourceAgent agent;
	private IResourceAgent stub;

	// fields used as map information
	private boolean fake;
	private int iconType;
	
	public RegistryData() {	}
	
	public RegistryData(String name) {
		this.resourceName = name;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float posX) {
		this.positionX = posX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float posY) {
		this.positionY = posY;
	}

	/**
	 * Get the name that will be the resource name
	 * 
	 * @return The user-defined resource name
	 */
	public String getResourceName() {

		return this.resourceName;
	}

	/**
	 * Set the name the will be used as resource name (user-defined)
	 * 
	 * @param name
	 *            The user-defined resource name
	 */
	public void setResourceName(String name) {
		this.resourceName = name;
	}

	public IResourceAgent getAgent() {
		return agent;
	}


	public IResourceAgent getStub() {
		return stub;
	}

	public void setStub(IResourceAgent stub) {
		this.stub = stub;
	}
	
	public void setAgent(IResourceAgent agent) {
		this.agent = agent;
	}

	public boolean isFake() {
		return fake;
	}

	public void setFake(boolean simulated) {
		this.fake = simulated;
	}

	public int getIconType() {
		return iconType;
	}

	public void setIconType(int iconType) {
		this.iconType = iconType;
	}
}
