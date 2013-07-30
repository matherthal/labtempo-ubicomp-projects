package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IOnOff extends IResourceAgent {
	
	public void setStatus(boolean isOn);
	public boolean isOn();

}
