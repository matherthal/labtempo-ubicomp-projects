package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface ILamp extends IResourceAgent {

	@ContextVariable(name = "Lampada ligada")
	public boolean isOn();

	@Service(name = "Acender lampada")
	public void turnOn();

	@Service(name = "Apagar lampada")
	public void turnOff();
}
