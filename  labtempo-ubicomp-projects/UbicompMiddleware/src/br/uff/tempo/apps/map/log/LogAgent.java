package br.uff.tempo.apps.map.log;

import br.uff.tempo.middleware.management.ResourceAgent;

public class LogAgent extends ResourceAgent {

	public static LogAgent obj; 
	
	private LogAgent() {}
	
	public static LogAgent getInstance() {
	
		if (obj == null) {
			obj = new LogAgent();
		}
		
		return obj;
	}
	
	@Override
	public void notificationHandler(String rai, String method, Object value) {
		
		
	}

}
