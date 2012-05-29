package br.uff.tempo.middleware.management;

public interface IResourceRegister {
	//boolean return due connectivity issues
	public boolean register(String url);
	public boolean unregister(String url);
}
