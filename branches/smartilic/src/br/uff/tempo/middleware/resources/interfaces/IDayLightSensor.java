package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IDayLightSensor extends IResourceAgent {

	@ContextVariable(name = "É dia", type = "IsDay")
	public boolean isDay();

	@ContextVariable(name = "Quantidade de luz", type = "LightAmout")
	public float lightAmount();

	@Service(name = "Definir se é dia", type = "SetIsDay")
	public void setDay(boolean d);

	@Service(name = "Definir quantidade de luz", type = "SetLightAmout")
	public void setLightAmount(float l);
}
