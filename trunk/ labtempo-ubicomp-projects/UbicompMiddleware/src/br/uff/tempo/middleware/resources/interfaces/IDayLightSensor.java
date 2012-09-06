package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface IDayLightSensor extends IResourceAgent {

	@ContextVariable(name = "É dia")
	public boolean isDay();

	@ContextVariable(name = "Quantidade de luz")
	public float lightAmount();

	@Service(name = "Definir se é dia")
	public void setDay(boolean d);

	@Service(name = "Definir quantidade de luz")
	public void setLightAmount(float l);
}
