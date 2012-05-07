package br.uff.tempo.middleware.management;

import java.util.ArrayList;

public interface IResourceRepository extends IResourceAgent{
	
	public ResourceAgent get(String url);
	public void add(ResourceAgent rA);
	public void remove(String url);
	public ArrayList<ResourceAgent> getList();
	
}
