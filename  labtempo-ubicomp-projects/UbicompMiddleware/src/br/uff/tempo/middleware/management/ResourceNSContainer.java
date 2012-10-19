package br.uff.tempo.middleware.management;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;

public class ResourceNSContainer {

	private static ResourceNSContainer instance;

	private Map<String, ResourceAgentNS> container;
	
	private IResourceDiscovery rDS;

	private ResourceNSContainer() {
		container = new HashMap<String, ResourceAgentNS>();
	}

	public synchronized static ResourceNSContainer getInstance() {
		if (instance == null) {
			instance = new ResourceNSContainer();
		}
		return instance;
	}

	public void add(ResourceAgentNS rA) {
		if (container.get(rA.getRans()) == null) { 
			container.put(rA.getRans(), rA);
		}
	}
	
	public void addAll(ResourceAgentNS... rAs) {
		for (ResourceAgentNS resourceAgent : rAs) {
			add(resourceAgent);
		}
	}

	public void remove(String rans) {
		container.remove(rans);
	}

	public ResourceAgentNS get(String rans) {
		ResourceAgentNS raNS = container.get(rans);
		
		if (raNS == null) {
			raNS = findRANS(rans);
			container.put(rans, raNS);
		}
		return raNS;
	}
	
	private ResourceAgentNS findRANS(String rans) {
		if (rDS == null) {
			rDS = new ResourceDiscoveryStub(IResourceDiscovery.RDS_ADDRESS);
		}
		return rDS.getRANS(rans);
	}

	public Set<String> getRANS() {
		return container.keySet();
	}
}
