package br.uff.tempo.middleware.resources.interfaces;

public interface ITeapot {
	public float temperature = 0.0f;

	public float getTemperature();

	public void setTemperature(float temp);
}
