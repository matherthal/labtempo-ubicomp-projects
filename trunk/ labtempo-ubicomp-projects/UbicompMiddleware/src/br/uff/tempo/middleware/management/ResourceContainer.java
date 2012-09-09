package br.uff.tempo.middleware.management;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.management.interfaces.IResourceContainer;

public class ResourceContainer implements IResourceContainer {

	private static ResourceContainer instance;

	private Map<String, ResourceAgent> container;

	private ResourceContainer() {
		container = new HashMap<String, ResourceAgent>();
	}

	public static ResourceContainer getInstance() {
		if (instance == null)
			instance = new ResourceContainer();
		return instance;
	}

	public void add(ResourceAgent rA) {
		container.put(rA.getRAI(), rA);
	}

	public void remove(String url) {
		container.remove(url);
	}

	public ResourceAgent get(String url) {
		return container.get(url);
	}

	public Set<String> getRAI() {
		return container.keySet();
	}

}
