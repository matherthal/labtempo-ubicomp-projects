package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class ResourceDiscovery extends ResourceAgent implements IResourceDiscovery {
	
	private static final long serialVersionUID = 1L;
	
	private static ResourceDiscovery instance;

	private ResourceDiscovery() {
		setId(2);
		setName("ResourceDiscovery");
		setType("br.uff.tempo.middleware.management.ResourceDiscovery");

		setRAI(IResourceDiscovery.RDS_ADDRESS);
	}
	
	public static ResourceDiscovery getInstance() {
		if (instance == null)
			instance = new ResourceDiscovery();
		return instance;
	}

	@Override
	public boolean identify() {
		String ip = ResourceAgentIdentifier.getLocalIpAddress();
		int prefix = ResourceAgentIdentifier.getLocalPrefix();
		
		ResourceContainer.getInstance().add(this);
		ResourceNSContainer.getInstance().add(new ResourceAgentNS(this.getRAI(), ip, prefix));
		ResourceRepository.getInstance().add(this.getRAI(), ip, prefix, this.getRAI());
		
		return true;
	}
	
	public ArrayList<String> search(String query) {
		ResourceRepository rR = ResourceRepository.getInstance();
		if (query == "")
			return rR.getList();
		else
			return rR.getSubList(query);

	}

	public ArrayList<String> queryByLocal(String query) {
		
		return null;
	}

	private ArrayList<ResourceAgent> queryByProximity(String query) {
		return null;
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
	}

	public ResourceAgent get(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceAgentNS getRANS(String rans) {
		return ResourceRepository.getInstance().getRANS(rans);
	}

	@Override
	public List<ResourceData> searchForAttribute(int attribute, String query) {
		return ResourceDirectory.getInstance().read(attribute, query);		
	}
}
