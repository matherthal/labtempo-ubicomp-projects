package br.uff.tempo.middleware;

import android.util.Log;
import br.uff.tempo.middleware.comm.current.api.SocketService;
import br.uff.tempo.middleware.management.ResourceRepository;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.utils.ResourceAgentIdentifier;

public class SmartAndroid {
	private static Thread commDaemon;
	
	private static SmartAndroid instance;
	
	public synchronized static void newInstance() {
		if (instance == null) {
			instance = new SmartAndroid();
		}
	}

	public SmartAndroid() {
		String rdsHost = new ResourceAgentIdentifier(IResourceDiscovery.RDS_ADDRESS).getPath();
		
		if (rdsHost.equals(ResourceAgentIdentifier.getLocalIpAddress())) {
			ResourceRepository.getInstance(); // ResourceRepository should be initialized before everything
		}
		
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
		});
		commDaemon.setDaemon(true);
		commDaemon.start();			
	}
}
