package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface ICounter extends IResourceAgent{
	
	public int getCount();

}
