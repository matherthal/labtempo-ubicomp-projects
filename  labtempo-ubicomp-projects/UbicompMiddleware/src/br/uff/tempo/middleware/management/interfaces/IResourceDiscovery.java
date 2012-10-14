package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceAgent;

public interface IResourceDiscovery {
	
	public static final String RDS_IP = "192.168.1.105";
	public static final String RDS_NAME = "br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";
	public static final String RDS_ADDRESS = "rai:" + RDS_IP + "//" + RDS_NAME;
	
	public ArrayList<String> search(String query);// return URI of resources

	public String getPath(String id);
	
	public ResourceAgent get(String rai);

}
