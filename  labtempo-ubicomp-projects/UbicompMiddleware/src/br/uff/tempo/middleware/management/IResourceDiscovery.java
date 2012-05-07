package br.uff.tempo.middleware.management;

import java.util.ArrayList;

public interface IResourceDiscovery extends IResourceAgent{
	
	public ArrayList<ResourceAgent> search(String query);

}
