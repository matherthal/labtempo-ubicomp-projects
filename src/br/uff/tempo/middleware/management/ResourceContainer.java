package br.uff.tempo.middleware.management;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.util.ReflectionUtils;

import br.uff.tempo.middleware.management.interfaces.IResourceContainer;

public class ResourceContainer implements IResourceContainer {

	private static ResourceContainer instance;

	private Map<String, ResourceAgent> container;
	
	private Map<String, ArrayList<Method>> rAMethods;

	private ResourceContainer() {
		container = new HashMap<String, ResourceAgent>();
		rAMethods = new HashMap<String, ArrayList<Method>>();
	}
	/**Singleton instance that only SmartServer components can use
	 * @return reference to ResourceLocation
	 */
	public synchronized static ResourceContainer getInstance() {
		if (instance == null) {
			instance = new ResourceContainer();
		}
		return instance;
	}

	public void add(ResourceAgent rA) {
		container.put(rA.getRANS(), rA);
		rAMethods.put(rA.getRANS(), new ArrayList<Method>(Arrays.asList(ReflectionUtils.getAllDeclaredMethods(rA.getClass()))));
	}
	
	public void addAll(ResourceAgent... rAs) {
		for (ResourceAgent resourceAgent : rAs) {
			add(resourceAgent);
		}
	}

	public void remove(String rai) {
		container.remove(rai);
		rAMethods.remove(rai);
	}

	public ResourceAgent get(String rai) {
		return container.get(rai);
	}
	
	public ArrayList<Method> getMethods(String rai) {
		return rAMethods.get(rai);
	}

	public Set<String> getRAI() {
		return container.keySet();
	}
	
}
