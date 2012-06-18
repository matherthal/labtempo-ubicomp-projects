package br.uff.tempo.apps.map.objects;

/**
 * This class aims to wrap the necessary information to register a resource and
 * enable this information to be passed as parameter easily. Initially, it's just
 * a helper class to MapActivity.
 * 
 * @author dbarreto
 */
public class RegistryData {

	private String resName;

	public RegistryData(String name) {

		this.resName = name;
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
	 * @param name The user-defined resource name
	 */
	public void setResourceName(String name) {
		this.resName = name;
	}

}
