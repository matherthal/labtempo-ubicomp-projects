package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

public interface IResourceRepository {
	
	public static final String rans = "resourcerepository.ra";

	public String get(String url);

	public boolean add(String rans, String ip, int prefix, String rai);

	public boolean remove(String url);

	public ArrayList<String> getList();

	public ArrayList<String> getSubList(String url);
	
}
