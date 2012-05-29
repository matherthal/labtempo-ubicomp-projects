package br.uff.tempo.middleware.comm.stubs;

import br.uff.tempo.middleware.comm.Caller;

public class Stub {
	
	public Caller caller;
	public Stub(String calleeAgent) {
		caller = new Caller(calleeAgent);
	}

}
