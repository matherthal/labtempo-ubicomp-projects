package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.utils.Position;

public interface IResourceRegister {
	
	public static final String rans = "resourceregister.ra";
	
	// boolean return due connectivity issues
	public boolean register(String rans, String ip, int prefix, String rai);
	
	public boolean registerLocation(String rans, String ip, int prefix, String rai, Position position);
	
	public boolean registerInPlace(String rans, String ip, int prefix, String rai, String placeName, Position position);
	
	public boolean unregister(String rai);
}
