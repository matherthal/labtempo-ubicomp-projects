package br.uff.tempo.middleware.management;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;

public class ResourceDiscovery extends ResourceAgent implements IResourceDiscovery {

	private static ResourceDiscovery instance;

	private ResourceDiscovery() {
		setId(2);
		setName("ResourceDiscovery");
		setType("br.uff.tempo.middleware.management.ResourceDiscovery");

		setURL(IResourceDiscovery.RDS_ADDRESS);

		ResourceContainer.getInstance().add(this);
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

	public static ResourceDiscovery getInstance() {
		if (instance == null)
			instance = new ResourceDiscovery();
		return instance;
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub

	}

	public ResourceAgent get(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}
