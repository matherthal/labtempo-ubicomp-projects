package br.uff.tempo.middleware.comm.common;

import br.uff.tempo.middleware.management.ResourceAgentNS;

public interface Callable {
	
	String call(ResourceAgentNS raNS, String interest, String message);

}
