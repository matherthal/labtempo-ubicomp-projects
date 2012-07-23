package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IBed extends IResourceAgent {

	@ContextVariable(name = "Cama ocupada")
	public boolean occupied();

	@Service(name = "Ocupar cama")
	public void lieDown();

	@Service(name = "Desocupar cama")
	public void getOut();
}
