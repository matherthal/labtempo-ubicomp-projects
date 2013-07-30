package br.uff.tempo.middleware.comm.interest.api;

public interface Callable {
	
	void call(String interest, String message, int address);
	
}
