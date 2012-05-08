package br.uff.tempo.middleware.management;

import java.util.ArrayList;

public interface IResourceDiscovery {
	
	public ArrayList<ResourceAgent> search(String query);

}
