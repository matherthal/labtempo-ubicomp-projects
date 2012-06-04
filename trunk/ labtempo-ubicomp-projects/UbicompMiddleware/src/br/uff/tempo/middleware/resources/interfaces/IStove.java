package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IStove extends IResourceAgent{
	public boolean getIsOn();
}
