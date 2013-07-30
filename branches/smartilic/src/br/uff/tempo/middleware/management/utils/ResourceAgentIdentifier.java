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
}
