package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceData;

public interface IResourceDiscovery {
	
	public static final String RDS_IP = "192.168.1.118";

	public static final String RDS_NAME = "br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";
	public static final String RDS_ADDRESS = "rai:" + RDS_IP + "//" + RDS_NAME;
	
	public static final String rans = "resourcediscovery.ra";
	
	public ArrayList<String> search(String query);// return URI of resources

	public List<ResourceData> searchForAttribute(int attribute, String query);// return URI of resources
	
	public String getPath(String id);
	
	public ResourceAgent get(String rai);

	// it's here rather than ResourceRegister because at the beginning ResourceDiscovery is known by all, otherwise ResourceRegister should also be known
	public ResourceAgentNS getRANS(String rans);  

}
