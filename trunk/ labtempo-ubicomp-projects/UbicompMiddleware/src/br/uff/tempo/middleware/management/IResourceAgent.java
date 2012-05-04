package br.uff.tempo.middleware.management;

public interface IResourceAgent {
	public boolean identify();
	public void notifyStakeholders();
	public boolean registerStakeholder(ResourceAgent rA);
	public void notificationHandler(ResourceAgent rA);
}
