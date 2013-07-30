package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.SmartAndroid;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;

/**
 * Resource Discovery Service class.
 * 
 * This class is responsible for search ResourceAgent instances from RespourceRepository
 */
public class ResourceDiscovery extends ResourceAgent implements IResourceDiscovery {
	
	private static final long serialVersionUID = 1L;
	
	private static ResourceDiscovery instance;

	private ResourceDiscovery() {
		super("ResourceDiscovery", ResourceDiscovery.class.getName(), IResourceDiscovery.rans);
	}
	
	/**
	 * Singleton instance that only SmartServer components can use
	 * @return reference to ResourceDiscovery
	 */
	public static ResourceDiscovery getInstance() {
		if (instance == null)
			instance = new ResourceDiscovery();
		return instance;
	}

	@Override
	public boolean identify() {
		String ip = SmartAndroid.getLocalIpAddress();
		int prefix = SmartAndroid.getLocalPrefix();
		
		ResourceAgentNS raNS = new ResourceAgentNS(this.getRANS(), ip, prefix);
		ResourceContainer.getInstance().add(this);
		ResourceNSContainer.getInstance().add(raNS);
		ResourceRepository.getInstance().add(new ResourceData(this.getRANS(), this.getName(), this.getType(), null, null, raNS));		
		return true;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public ResourceAgentNS getRANS(String rans) {
		return ResourceRepository.getInstance().getRANS(rans);
	}

	@Override
	public List<ResourceData> search(int attribute, String query) {
		return ResourceDirectory.getInstance().read(attribute, query);		
	}
}
