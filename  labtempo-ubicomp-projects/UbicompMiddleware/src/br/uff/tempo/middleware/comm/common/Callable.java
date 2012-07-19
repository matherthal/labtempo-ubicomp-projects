package br.uff.tempo.middleware.comm.common;

public interface Callable {
	
	void call(String contextVariable, String value, String address);
	
}
