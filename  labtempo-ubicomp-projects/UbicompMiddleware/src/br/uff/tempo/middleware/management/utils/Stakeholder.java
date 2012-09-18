package br.uff.tempo.middleware.management.utils;

import java.io.Serializable;

import br.uff.tempo.middleware.management.ResourceAgent;

public class Stakeholder implements Serializable {

	private String method;
	private String rai;

	public Stakeholder(String method, String rai) {
		this.method = method;
		this.rai = rai;
	}

	public Stakeholder(String method, ResourceAgent rA) {
		this.method = method;
		this.rai = rA.getRAI();
	}

	public String getMethod() {
		return method;
	}

	public String getRAI() {
		return rai;
	}
}
