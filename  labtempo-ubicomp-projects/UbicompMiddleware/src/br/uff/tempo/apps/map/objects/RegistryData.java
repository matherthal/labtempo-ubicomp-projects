package br.uff.tempo.apps.map.objects;

import java.io.Serializable;

/**
 * This class aims to wrap the necessary information to register a resource and
 * enable this information to be passed as parameter easily. Initially, it's
 * just a helper class to MapActivity.
 * 
 * @author dbarreto
 */
public class RegistryData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String resName;
	private float posX;
	private float posY;
	
	public RegistryData() {	}
	
	public RegistryData(String name) {
		this.resName = name;
	}

	public float getPositionX() {
		return posX;
	}

	/**
	 * 
	 * @param posX
	 */
	public void setPositionX(float posX) {
		this.posX = posX;
	}

	public float getPositionY() {
		return posY;
	}

	public void setPositonY(float posY) {
		this.posY = posY;
	}



	/**
	 * Get the name that will be the resource name
	 * 
	 * @return The user-defined resource name
	 */
	public String getResourceName() {

		return this.resName;
	}

	/**
	 * Set the name the will be used as resource name (user-defined)
	 * 
	 * @param name
	 *            The user-defined resource name
	 */
	public void setResourceName(String name) {
		this.resName = name;
	}

}
