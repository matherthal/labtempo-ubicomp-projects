package br.uff.tempo.middleware.comm.common;

public interface Callable {
	
	Object call(String rai, String contextVariable, String value);

}
