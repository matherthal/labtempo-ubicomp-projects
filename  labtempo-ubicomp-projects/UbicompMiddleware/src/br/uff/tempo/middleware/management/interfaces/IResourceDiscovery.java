package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceData;

public interface IResourceDiscovery {

	public static final String rans = "resourcediscovery.ra";

	public List<ResourceData> search(int attribute, String query);// return URI of resources

	// it's here rather than ResourceRegister because at the beginning ResourceDiscovery is known by all, otherwise ResourceRegister should also be known
	public ResourceAgentNS getRANS(String rans);  

}
