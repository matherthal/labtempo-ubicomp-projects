package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IPresenceSensor extends IResourceAgent {

	@ContextVariable(name = "Presença", type = "Presence")
	public boolean getPresence();

	@Service(name = "Detectar Presença", type = "SetPresence")
	public void setPresence(boolean p);

}
