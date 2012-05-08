package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourceRepository extends ResourceAgent implements IResourceRepository {
	
	HashMap<String,ResourceAgent> repository;
	private static ResourceRepository instance;
	
	private ResourceRepository()
	{
		repository = new HashMap<String,ResourceAgent>();

	}
	
	public static ResourceRepository getInstance()
	{
		if (instance == null)
			instance = new ResourceRepository();
		return instance;
	}

	public ResourceAgent get(String url) {
		return repository.get(url);
	}

	public ArrayList<ResourceAgent> getList(){
		return (ArrayList<ResourceAgent>) repository.values();
	}
	
	public void add(ResourceAgent rA) {
		repository.put(rA.getURL(), rA);		
	}

	public void remove(String url) {
		repository.remove(url);		
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

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub
		
	}

	
	

}
