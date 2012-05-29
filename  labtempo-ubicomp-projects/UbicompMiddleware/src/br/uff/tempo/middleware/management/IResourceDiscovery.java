package br.uff.tempo.middleware.management;

import java.util.ArrayList;

public interface IResourceDiscovery {
	
	public ArrayList<String> search(String query);//return URI of resources
	public ResourceAgent get(String url);

}
