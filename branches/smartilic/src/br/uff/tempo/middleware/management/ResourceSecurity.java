package br.uff.tempo.middleware.management;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.management.interfaces.IResourceSecurity;

public class ResourceSecurity extends ResourceAgent implements IResourceSecurity{

	List<String> domainList;
	
	private static ResourceSecurity instance;
	
	ResourceDirectory resDirectory;
	
	private ResourceSecurity(String name, String type, String rans) {
		super(name, type, rans);
		domainList = new ArrayList<String>();
		domainList.add(ResourceDirectory.GENERAL_DOMAIN);
	}

	public static ResourceSecurity getInstance(){
		if (instance == null){
			instance = new ResourceSecurity("ResourceSecurity", ResourceSecurity.class.getName(), IResourceSecurity.rans);
		}
		return instance;
	}
	
	public void createDomain(String domain){
		resDirectory.createDomain(domain);
		domainList.add(domain);
	}
	
	public void copy(ResourceData resource, String domain){
		resDirectory.setDomain(domain);
		resDirectory.create(resource);
		resDirectory.setDomain(ResourceDirectory.GENERAL_DOMAIN);
	}
	
	public void delete(ResourceData resource, String domain){
		resDirectory.setDomain(domain);
		resDirectory.delete(resource);
		resDirectory.setDomain(ResourceDirectory.GENERAL_DOMAIN);
	}
	
	public void move(ResourceData resource, String sourceDomain, String newDomain){
		delete(resource,sourceDomain);
		copy(resource,newDomain);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notificationHandler(String rai, String method, Object value) {
		// TODO Auto-generated method stub
		
	}

}
