package br.uff.tempo.middleware.management;

import java.util.ArrayList;

public class ResourceDiscovery extends ResourceAgent implements IResourceDiscovery {

	private static ResourceDiscovery instance;
	private ResourceDiscovery()
	{
		
	}
	
	public ArrayList<ResourceAgent> search(String query) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ArrayList<ResourceAgent> queryByLocal(String query)
	{
		return null;
	}
	
	private ArrayList<ResourceAgent> queryByProximity(String query)
	{
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

	public void notifyStakeholders() {
		// TODO Auto-generated method stub
		
	}

	public boolean registerStakeholder(ResourceAgent rA) {
		// TODO Auto-generated method stub
		return false;
	}

	public void notificationHandler(ResourceAgent rA) {
		// TODO Auto-generated method stub
		
	}

}
