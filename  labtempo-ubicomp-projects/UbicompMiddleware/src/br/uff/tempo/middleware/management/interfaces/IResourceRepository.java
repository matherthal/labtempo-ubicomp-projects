package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

public interface IResourceRepository{
	
	public String get(String url);
	public boolean add(String url);
	public boolean remove(String url);
	public ArrayList<String> getList();
	public ArrayList<String> getSubList(String url);
	public boolean update(String url);
	
	
}
