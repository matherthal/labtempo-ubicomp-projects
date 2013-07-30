package br.uff.tempo.middleware.management;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
/**
 * This class is used to save local instances of any SmartAndroid device
 */
public class ResourceNSContainer {

	private static ResourceNSContainer instance;

	private Map<String, ResourceAgentNS> container;
	
	private IResourceDiscovery rDS;

	private ResourceNSContainer() {
		container = new HashMap<String, ResourceAgentNS>();
	}

	/**
	 * Singleton instance that any SmartAndroid device instantiate
	 * @return reference to ResourceNSContainer
	 */
	public synchronized static ResourceNSContainer getInstance() {
		if (instance == null) {
			instance = new ResourceNSContainer();
		}
		return instance;
	}

	/**
	 * Add a ResourceAgentNS instance
	 * @param rA ResourceAgentNS instance
	 */
	public void add(ResourceAgentNS rA) {
		if (container.get(rA.getRans()) == null) { 
			container.put(rA.getRans(), rA);
		}
	}
	
	/**
	 * Add a collection of ResourceAgentNS instances
	 * @param rAs collection of ResourceAgentNS instances
	 */
	public void addAll(ResourceAgentNS... rAs) {
		for (ResourceAgentNS resourceAgent : rAs) {
			add(resourceAgent);
		}
	}

	/**
	 * Remove a ResourceAgentNS instance referred by rans
	 * @param rans ResourceAgentNS reference
	 */
	public void remove(String rans) {
		container.remove(rans);
	}

	/**
	 * Get access of a ResourceAgentNS instance referred by rans
	 * @param rans ResourceAgentNS reference
	 * @return ResourceAgentNS instance
	 */
	public ResourceAgentNS get(String rans) {
		ResourceAgentNS raNS = container.get(rans);
		
		if (raNS == null) {
			raNS = findRANS(rans);
			
			if (raNS != null) {
				container.put(rans, raNS);
			}
		}
		return raNS;
	}
	
	
	private ResourceAgentNS findRANS(String rans) {
		if (rDS == null) {
			rDS = new ResourceDiscoveryStub(IResourceDiscovery.rans);
		}
		return rDS.getRANS(rans);
	}
	
	/**
	 * Get a complete set of rans string representing SmartAndroid unit instances 
	 * @return rans of all instances from a device
	 */
	public Set<String> getRANS() {
		return container.keySet();
	}
}
