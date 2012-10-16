package br.uff.tempo.middleware.management.utils;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.util.Log;

public class ResourceAgentIdentifier implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// rai-uri = �rai:� rai-path [�//� rai-type *(�/� rai-type)]
	// �:� rai-name
	private String rai;

	private String path;
	private ArrayList<String> type;
	private String name;

	private int pointer;
	
	private static String defaultMyIp = "127.0.0.1";
	private static String myIp = defaultMyIp;
	private static int myLocalPrefix = 0;

	public ResourceAgentIdentifier(String rai) {
		this.rai = rai;
		pointer = 0;
		path = extractPath();
		type = extractType();
		name = extractName();
	}

	private String extractPath() {
		while (rai.charAt(pointer++) != ':')
			;
		int begin = pointer;
		while (!(rai.charAt(pointer) == '/' && rai.charAt(pointer + 1) == '/'))
			pointer++;
		return rai.substring(begin, pointer);
	}

	private ArrayList<String> extractType() {
		ArrayList<String> result = new ArrayList<String>();
		pointer++;
		while (rai.charAt(pointer) != ':') {
			pointer++;
			int begin = pointer;
			while (rai.charAt(pointer) != '/' && rai.charAt(pointer) != ':')
				pointer++;
			result.add(rai.substring(begin, pointer));
		}
		return result;
	}

	private String extractName() {
		return rai.substring(pointer + 1);
	}

	public String getRai() {
		return rai;
	}

	public String getPath() {
		return path;
	}

	public ArrayList<String> getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static String generateRAI(String path, String type, String name) {
		return "rai:" + path + "//" + type + ":" + name;
	}

	public static String getLocalIpAddress() {
		return myIp;
	}
	
	public static int getLocalPrefix() {
		return myLocalPrefix;
	}
	
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
	
	public static void fillLocalPrefixAddress() {
		myLocalPrefix = 0; //to keep compatibility for now 
		//myLocalPrefix = InterestAPIImpl.getInstance().getPrefix();		
	}
	
}
