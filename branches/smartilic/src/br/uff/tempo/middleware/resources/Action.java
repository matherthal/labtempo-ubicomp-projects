package br.uff.tempo.middleware.resources;

import br.uff.tempo.middleware.management.ResourceAgent;

public class Action {
	public ResourceAgent ra;
	public String method;
	public Object[] params;

	public Action(ResourceAgent ra, String method, Object[] params) {
		this.ra = ra;
		this.method = method;
		this.params = params;
	}

	@Override
	public String toString() {
		String p = "";
		for (int i = 0; i < params.length; i++)
			p = params[i] + ", ";
		p = p.substring(0, p.length() - 3);
		return this.ra.getType() + this.ra.getName() + this.method + "(" + p + ")";
	}
}
