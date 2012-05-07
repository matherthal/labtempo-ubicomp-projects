package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.HashMap;

public class ResourceRepository extends ResourceAgent implements IResourceRepository {
	
	HashMap<String,ResourceAgent> repository;
	private static ResourceRepository instance;
	
	private ResourceRepository()
	{
		repository = new HashMap<String,ResourceAgent>();
		instance = this;
	}
	
	public static ResourceRepository getInstance()
	{
		if (instance == null)
			return new ResourceRepository();
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
	
	

}
