package br.uff.tempo.middleware.comm.stubs;

import br.uff.tempo.middleware.comm.Caller;

public class Stub {
	
	private final String serverIP;
	public Caller caller;
	public Stub(String serverIP) {
		this.serverIP = serverIP;
		caller = new Caller(serverIP);
	}

}
