package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.utils.Position;

public interface IResourceRegister {
	// boolean return due connectivity issues
	public boolean register(String url);
	
	public boolean registerLocation(String url, Position position);

	public boolean unregister(String url);
}
