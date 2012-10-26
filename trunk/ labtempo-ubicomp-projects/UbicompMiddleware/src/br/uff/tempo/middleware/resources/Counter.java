package br.uff.tempo.middleware.resources;

import br.uff.tempo.middleware.management.ResourceAgent;

public class Counter extends ResourceAgent{
	
	private static final long serialVersionUID = 1L;

	int count;
	String onOffRai;
	
	
	public Counter(String name) {
		super(name, "br.uff.tempo.middleware.resources.Counter");
		count =0;
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		count++;
	}
	
	public int getCount() {
		return count;
	}
	
	

}
