package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceAgent;

public interface IResourceDiscovery {
	

	public ArrayList<String> search(String query);//return URI of resources
	public ResourceAgent get(String url);

}
