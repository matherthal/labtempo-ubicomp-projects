package br.uff.tempo.middleware.management;

import java.util.ArrayList;

public interface IResourceRepository{
	
	public ResourceAgent get(String url);
	public boolean add(ResourceAgent rA);
	public boolean remove(String url);
	public ArrayList<ResourceAgent> getList();
	public boolean update(ResourceAgent rA);
	
}
