package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IBed extends IResourceAgent {
	
	public Integer numUsers = 0;
	
	boolean hasSomeoneIn();
	
	void setSomeoneIn(boolean someone);
}
