package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;

import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public interface IResourceDiscovery {
	public static final String RDS_ADDRESS = "rai:" + ResourceAgentIdentifier.getLocalIpAddress() + "//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";
	//public static final String RDS_ADDRESS = "rai:192.168.1.102//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";
	//public static final String RDS_ADDRESS = "rai:192.168.1.114//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";

	// public static final String RDS_ADDRESS =
	// "rai:192.168.1.74//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";
	//public static final String RDS_ADDRESS =  "rai:192.168.1.125//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";
	//public static final String RDS_ADDRESS =  "rai:192.168.1.111//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";
	
	 //public static final String RDS_ADDRESS =
	 //"rai:192.168.1.70//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";
	// public static final String RDS_ADDRESS =
	// "rai:192.168.1.111//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";

	// public static final String RDS_ADDRESS =
	// "rai:192.168.0.149//br.uff.tempo.middleware.management.ResourceDiscovery:ResourceDiscovery";

	public ArrayList<String> search(String query);// return URI of resources

	public ResourceAgent get(String rai);

}
