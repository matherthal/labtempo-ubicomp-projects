package br.uff.tempo.middleware.management.interfaces;

public interface IRuleInterpreter extends IResourceAgent {

	public void start();

	public void stop();
	
	public boolean isStarted();
}
