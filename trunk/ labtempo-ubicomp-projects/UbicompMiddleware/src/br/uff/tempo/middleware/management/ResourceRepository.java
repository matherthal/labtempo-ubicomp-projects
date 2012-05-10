package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class ResourceRepository extends ResourceAgent implements IResourceRepository {
	
	HashMap<String,ResourceAgent> repository;
	private static ResourceRepository instance;
	
	private ResourceRepository()
	{
		setId(0);
		setName("ResourceDiscovery");
		setType("management");	
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
		return  new ArrayList<ResourceAgent>(repository.values());
	}
	
	public void add(ResourceAgent rA) {
		repository.put(rA.getURL(), rA);
		rA.registerStakeholder("all", this);//all methods of IAR are stakeholders
	}

	public void remove(String url) {
		repository.remove(url);		
	}

	public void update(ResourceAgent rA)
	{
		repository.put(rA.getURL(),rA);
	}

	@Override
	public void notificationHandler(String change) throws JSONException {
		ResourceAgent rA = (ResourceAgent)new JSONObject(change).get("value");
		update(rA);
	}

	
	

}
