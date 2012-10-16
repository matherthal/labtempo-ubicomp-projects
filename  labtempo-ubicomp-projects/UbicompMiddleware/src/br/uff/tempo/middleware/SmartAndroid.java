package br.uff.tempo.middleware;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.SocketService;
import br.uff.tempo.middleware.management.ResourceRepository;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class SmartAndroid {
	protected static final long TIME_TO_FILL_IP_AND_PREFIX = 5 * 60 * 1000; // 5 min
	
	private static Thread ipPrefixDaemon;
	
	private static Thread commDaemon;
	
	private static SmartAndroid instance;
	
	public synchronized static void newInstance() {
		if (instance == null) {
			instance = new SmartAndroid();
		}
	}

	public SmartAndroid() {
		initializeIpAndPrefixDaemon();
		initializeResourceRepository();
		initializeCommunicationDaemon();			
	}

	private void initializeIpAndPrefixDaemon() {
		ResourceAgentIdentifier.fillLocalIpAddress();     // Initializing local IP address for communication...
		ResourceAgentIdentifier.fillLocalPrefixAddress(); // Initializing local PREFIX address for communication...
		
		ipPrefixDaemon = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(TIME_TO_FILL_IP_AND_PREFIX);
						
						ResourceAgentIdentifier.fillLocalIpAddress(); // Updating IP
						ResourceAgentIdentifier.fillLocalPrefixAddress(); // Updating PREFIX
					} catch (Exception e) {
						Log.d("SmartAndroid", String.format("Exception: %s", e.getMessage()));
					}
				}
			}
		}, "ipPrefixDaemon");
		ipPrefixDaemon.setDaemon(true);
		ipPrefixDaemon.start();
	}

	private void initializeResourceRepository() {
		String rdsHost = new ResourceAgentIdentifier(IResourceDiscovery.RDS_ADDRESS).getPath();
		if (rdsHost.equals(ResourceAgentIdentifier.getLocalIpAddress())) {
			ResourceRepository.getInstance(); // ResourceRepository should be initialized before everything
		}
	}
	
	private void initializeCommunicationDaemon() {
		commDaemon = new Thread(new Runnable() {
			private SocketService socket = new SocketService();;
			
			@Override
			public void run() {
				while (true) {
					try {
						socket.receiveSend();
					} catch (Exception e) {
						Log.d("SmartAndroid", String.format("Exception: %s", e.getMessage()));
					}
				}
			}
		}, "commDaemon");
		commDaemon.setDaemon(true);
		commDaemon.start();
	}
}
