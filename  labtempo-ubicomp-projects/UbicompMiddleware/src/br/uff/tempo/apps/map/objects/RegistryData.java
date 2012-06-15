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

	public RegistryData() {

	}

	public String getResourceName() {

		return this.resName;
	}

	public void setResourceName(String name) {
		this.resName = name;
	}

}
