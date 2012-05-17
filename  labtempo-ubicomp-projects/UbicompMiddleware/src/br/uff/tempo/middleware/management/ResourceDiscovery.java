package br.uff.tempo.middleware.management;

import java.util.ArrayList;

public class ResourceDiscovery extends ResourceAgent implements
		IResourceDiscovery {

	private static ResourceDiscovery instance;

	private ResourceDiscovery() {
		setId(2);
		setName("ResourceDiscovery");
		setType("management");
	}

	public ArrayList<ResourceAgent> search(String query) {
		ResourceRepository rR = ResourceRepository.getInstance();
		if (query == "")
			return rR.getList();
		return null;
	}

	private ArrayList<ResourceAgent> queryByLocal(String query) {
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

}
