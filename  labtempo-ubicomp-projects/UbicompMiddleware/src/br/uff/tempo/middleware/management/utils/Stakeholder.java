package br.uff.tempo.middleware.management.utils;

import br.uff.tempo.middleware.management.ResourceAgent;

public class Stakeholder {

	private String method;
	private String url;
	
	public Stakeholder(String method, String url)
	{
		this.method = method;
		this.url = url;
	}
	
	public Stakeholder(String method, ResourceAgent rA)
	{
		this.method = method;
		this.url = rA.getURL();
	}
	
	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}
	
}
