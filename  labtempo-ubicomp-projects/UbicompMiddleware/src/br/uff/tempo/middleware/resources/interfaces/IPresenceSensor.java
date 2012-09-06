package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IPresenceSensor extends IResourceAgent {

	@ContextVariable(name = "Presença")
	public boolean getPresence();

	@Service(name = "Detectar Presença")
	public void setPresence(boolean p);

}
