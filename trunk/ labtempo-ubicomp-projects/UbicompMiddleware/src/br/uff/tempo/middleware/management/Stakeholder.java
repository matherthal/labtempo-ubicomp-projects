package br.uff.tempo.middleware.management;

public class Stakeholder {
	
	String method;
	String url;

	public Stakeholder(String method, String url) {
		
		this.method = method;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

}
