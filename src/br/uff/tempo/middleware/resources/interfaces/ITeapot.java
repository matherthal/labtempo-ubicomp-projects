package br.uff.tempo.middleware.resources.interfaces;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public interface ITeapot extends IResourceAgent{
	public float temperature = 0.0f;

	public float getTemperature();

	public void setTemperature(float temp);
}
