package br.uff.tempo.middleware.management.utils;

import java.io.Serializable;

import br.uff.tempo.middleware.management.ResourceAgent;

public class Stakeholder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String method;
	private String rans;

	public Stakeholder(String method, String rans) {
		this.method = method;
		this.rans = rans;
	}

	public Stakeholder(String method, ResourceAgent rA) {
		this(method, rA.getRANS());
	}

	public String getContextVariable() {
		return method;
	}

	public String getRANS() {
		return rans;
	}
	
	@Override
	public boolean equals(Object obj) {
	
		if (this == obj)
			return true;
		
		if (!(obj instanceof Stakeholder))
			return false;
		
		Stakeholder sh = (Stakeholder) obj;
		
		return this.rans.equals(sh.rans) && this.method.equals(sh.method);
	}
	
	@Override
	public String toString() {
		return this.rans + " wants: " + this.method;
	}
}
