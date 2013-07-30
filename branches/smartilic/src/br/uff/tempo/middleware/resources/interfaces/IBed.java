package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IBed extends IResourceAgent {

	@ContextVariable(name = "Cama ocupada", type = "Occupied")
	public boolean occupied();

	@Service(name = "Ocupar cama", type = "Occupy")
	public void lieDown();

	@Service(name = "Desocupar cama", type = "Occupy")
	public void getOut();
}
