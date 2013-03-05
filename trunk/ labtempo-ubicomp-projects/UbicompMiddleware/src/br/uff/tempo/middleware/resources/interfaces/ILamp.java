package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface ILamp extends IResourceAgent {

	@ContextVariable(name = "Lampada ligada", type = "OnOff")
	public boolean isOn();

	@Service(name = "Acender lampada", type = "TurnOnOff")
	public void turnOn();

	@Service(name = "Apagar lampada", type = "TurnOnOff")
	public void turnOff();
}
