package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceData;

public interface IResourceRepository {
	
	public static final String rans = "resourcerepository.ra";

	public ResourceData get(String rans);
	
	public ResourceAgentNS getRANS(String rans);

	public boolean add(ResourceData resourceData);

	public boolean remove(String rans);

	
	
}
