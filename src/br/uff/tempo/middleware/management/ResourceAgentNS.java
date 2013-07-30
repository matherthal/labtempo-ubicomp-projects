package br.uff.tempo.middleware.management;

import java.io.Serializable;

/**
 * Contains essential information to communicate with the ResourceAgent
 * 
 * Resource Agent Name System (RANS)
 * 
 * Represents the idea of Domain Name System (DNS) at SmartAndroid 
 */
public class ResourceAgentNS implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * rans is the effective name
	 * 
	 * Examples: "luzdasala.ra", "resourceregister.ra" 
	 */
	private String rans; 
    
    private String ip;
    
    private int prefix;
    
    public ResourceAgentNS(String rans, String ip, int prefix) {
		this.rans = rans;
		this.ip = ip;
		this.prefix = prefix;
	}

	public String getRans() {
		return rans;
	}

	public void setRans(String rans) {
		this.rans = rans;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPrefix() {
		return prefix;
	}

	public void setPrefix(int prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String toString() {
		return String.format("ip: %s prefix: %s rans: %s", ip, prefix, rans);
	}
}
