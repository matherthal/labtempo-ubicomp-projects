package br.uff.tempo.middleware.management.utils;

import java.io.Serializable;

import br.uff.tempo.middleware.management.ResourceAgent;

public class Stakeholder implements Serializable {

	private String method;
	private String rai;
	private String name;

	public Stakeholder(String method, String rai) {
		this.method = method;
		this.rai = rai;
		
		this.name = new ResourceAgentIdentifier(rai).getName();
	}

	public Stakeholder(String method, ResourceAgent rA) {
		this(method, rA.getRAI());
	}

	public String getMethod() {
		return method;
	}

	public String getRAI() {
		return rai;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
	
		if (this == obj)
			return true;
		
		if (!(obj instanceof Stakeholder))
			return false;
		
		Stakeholder sh = (Stakeholder) obj;
		
		return this.rai.equals(sh.rai) && this.method.equals(sh.method);
	}
	
	@Override
	public String toString() {
		
		return this.name + " wants: " + this.method;
	}
}
