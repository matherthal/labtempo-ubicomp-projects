package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceAgentNS;

public interface IResourceRepository {
	
	public static final String rans = "resourcerepository.ra";

	public String get(String url);
	
	public ResourceAgentNS getRANS(String rans);

	public boolean add(String rans, String ip, int prefix);

	public boolean remove(String url);

	public ArrayList<String> getList();

	public ArrayList<String> getSubList(String url);
	
}
