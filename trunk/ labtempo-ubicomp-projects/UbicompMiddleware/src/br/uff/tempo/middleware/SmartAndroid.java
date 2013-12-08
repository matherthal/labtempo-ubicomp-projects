package br.uff.tempo.middleware;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import android.content.Context;
import android.util.Log;
import br.uff.tempo.middleware.comm.common.Callable;
import br.uff.tempo.middleware.comm.common.InterestAPI;
import br.uff.tempo.middleware.comm.current.api.SocketService;
import br.uff.tempo.middleware.comm.interest.api.InterestAPIImpl;
import br.uff.tempo.middleware.e.SmartAndroidRuntimeException;
import br.uff.tempo.middleware.management.LogOpenHelper;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceDiscovery;
import br.uff.tempo.middleware.management.ResourceLocation;
import br.uff.tempo.middleware.management.ResourceNSContainer;
import br.uff.tempo.middleware.management.ResourceRegister;
import br.uff.tempo.middleware.management.ResourceRepository;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;


/**
 * Source Class where SmartServer is defined in terms of communication address.
 * The SmartServer is composed of services: Resource Discovery (RDS), Resource Register (RRS) and Resource Localization (RLS);
 * and a Resource Repository.
 *
 */
public class SmartAndroid {
	
	private static String defaultMyIp = "127.0.0.1";
	private static String myIp = defaultMyIp;
	private static int myLocalPrefix = 0;

	public static String resourceDiscoveryIP = "192.168.1.72"; //getLocalIpAddressForRds();

	public static Integer resourceDiscoveryPREFIX = 0;
	public static boolean interestAPIEnable = false;
	
	protected static final long TIME_TO_FILL_IP_AND_PREFIX = 5 * 60 * 1000; // 5 min

	//TODO: find a small sufficiently number because this is extremely big
	public static final String DEVICE_ID = UUID.randomUUID().toString();
	
	private static Thread ipPrefixDaemon;
	private static Thread commDaemon;
	
	private static SmartAndroid instance;
	
	private static String myLocalIp;
	
	/**
	 * Following Sigleton Design Patter it is the constructor that initialize the SmartServer
	 */
	public synchronized static void newInstance(Context context) {
		if (instance == null) {
			instance = new SmartAndroid(context);
		}
	}	
	
	private SmartAndroid(Context context) {
		SocketService soServ = new SocketService("Multicast");
		try {
			soServ.initiateMulticastSocket(context);
			soServ.sendMessage("hello");
			Log.d("Multicast", soServ.receiveMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initializeIpAndPrefixDaemon();
		initializeMiddlewareResources();
		initializeCommunicationDaemon();
	}

	private void initializeIpAndPrefixDaemon() {
		fillLocalIpAddress();     // Initializing local IP address for communication...
		fillLocalPrefixAddress(); // Initializing local PREFIX address for communication...
		
		ipPrefixDaemon = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(TIME_TO_FILL_IP_AND_PREFIX);
						
						fillLocalIpAddress(); // Updating IP
						fillLocalPrefixAddress(); // Updating PREFIX
					} catch (Exception e) {
						Log.d("SmartAndroid", String.format("Exception: %s", e.getMessage()));
					}
				}
			}
		}, "ipPrefixDaemon");
		ipPrefixDaemon.setDaemon(true);
		ipPrefixDaemon.start();
	}
	
	private void initializeMiddlewareResources() {
		if (resourceDiscoveryIP.equals(getLocalIpAddress())) {
			resourceDiscoveryPREFIX = myLocalPrefix;
			
			// ResourceRepository should be initialized before everything
			ResourceRepository.getInstance().identify();
			ResourceDiscovery.getInstance().identify();
			ResourceRegister.getInstance().identify();
			ResourceLocation.getInstance().identify();
			
			if (interestAPIEnable) {
				InterestAPI ia = InterestAPIImpl.getInstance();
				try {
					ia.registerInterest("fetch-resource-discovery-prefix", new Callable() {
						@Override
						public String call(ResourceAgentNS raNS, String interest, String message) {
							if ("fetch-prefix".equals(message)) {
								return String.valueOf(resourceDiscoveryPREFIX);
							}
							return null;
						}
					});
				} catch (Exception e) {
					throw new SmartAndroidRuntimeException("Exception informing ResourceDiscovery prefix. Impossible to initialize communication in others devices.", e);
				}
			}
		} else {
			if (interestAPIEnable) {
				// Find ResourceDiscovery prefix to initialize communication...
				InterestAPI ia = InterestAPIImpl.getInstance();
				
				try {
					ia.registerInterest("fetch-resource-discovery-prefix");
					String fetchPrefixResult = ia.sendMessage("fetch-resource-discovery-prefix" , "fetch-prefix");
					if (fetchPrefixResult != null) {
						resourceDiscoveryPREFIX = Integer.valueOf(fetchPrefixResult); 
					} else {
						throw new SmartAndroidRuntimeException("Unable to find ResourceDiscovery prefix. Impossible to initialize communication.");
					}
				} catch (Exception e) {
					throw new SmartAndroidRuntimeException("Exception finding ResourceDiscovery prefix. Impossible to initialize communication.", e);
				}
			}			
			// Should be initialized with ResourceDiscovery IP and PREFIX at beginning to allow communication...
			ResourceNSContainer.getInstance().add(new ResourceAgentNS(IResourceDiscovery.rans, resourceDiscoveryIP, resourceDiscoveryPREFIX));			
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
	
	/**
	 * Set local ip address in order to verify if itself is as SmartServer
	 */
	public static void fillLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) {
						myIp = inetAddress.getHostAddress();
						return;
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("ResourceAgent", ex.getMessage());
		}
		myIp = defaultMyIp; //just to keep compatibility
	}
	
	/**
	 * Set local Prefix in order to verify if itself is as SmartServer
	 * @throws Exception 
	 */
	public static void fillLocalPrefixAddress() {
		if (interestAPIEnable) {
			try {
				myLocalPrefix = InterestAPIImpl.getInstance().getMyPrefix();
			} catch (Exception e) {
				throw new SmartAndroidRuntimeException("Exception getting PrefixAddress. Impossible to initialize communication in others devices.", e);
			}
		}
	}

	/**
	 * Get device local address
	 * @return device local address
	 */
	public static String getLocalIpAddress(){
		return myIp;
	}
	
	/**
	 * Utility function for get local ip address to be used as SmartServer Address
	 * ex: resourceDiscoveryIP = getLocalIpAddressForRds();
	 * This function differ from getLocalIpAddres, because this function get right address before execution of fillLocalIpAddress
	 * @return local ip address
	 */
	public static String getLocalIpAddressForRds() {
		if ( myLocalIp == null) {	
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) {
							myLocalIp = inetAddress.getHostAddress();
							return myLocalIp;
						}
					}
				}
			} catch (SocketException ex) {
				Log.e("ResourceAgent", ex.getMessage());
			}
		} 
		return myLocalIp;
	}
	
	/**
	 * Get device local prefix
	 * @return device local prefix
	 */
	public static int getLocalPrefix() {
		return myLocalPrefix;
	}
}
