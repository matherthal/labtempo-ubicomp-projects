package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceData;

/**
 * Resource Discovery Service (RDS) interface
 * <br /><br />
 * It enables use of RDS in SmartServer  
 */
public interface IResourceDiscovery {

	/**
	 * Unique system rans of RDS
	 */
	public static final String rans = "resourcediscovery.ra";

	/**
	 * Realize a search for Registered Resource Agent instances
	 * @param attribute indicates attribute target
	 * It could be ResourceData.TYPE, ResourceData.PLACE, ResourceData.NAME or ResouceData.RANS  
	 * @param query value of query
	 * @return Representative data list of Resource Agents found
	 */
	public List<ResourceData> search(int attribute, String query);// return URI of resources

	/**
	 * Get ResourceAgentNS object from string reference
	 * @param rans string reference
	 * @return RANS object
	 */
	public ResourceAgentNS getRANS(String rans);  

}
