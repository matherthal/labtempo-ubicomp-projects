package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.utils.Position;

public interface IResourceRegister {
	
	public static final String rans = "resourceregister.ra";
	
	// boolean return due connectivity issues
	public boolean register(String rans, String ip, int prefix, ResourceData resourceData);
	
	public boolean registerLocation(String rans, String ip, int prefix, Position position, ResourceData resourceData);
	
	public boolean registerInPlace(String rans, String ip, int prefix, String placeName, Position position, ResourceData resourceData);
	
	public boolean unregister(String rai);

}
