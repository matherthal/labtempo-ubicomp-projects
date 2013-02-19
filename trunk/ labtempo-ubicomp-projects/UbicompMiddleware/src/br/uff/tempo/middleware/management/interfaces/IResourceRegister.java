package br.uff.tempo.middleware.management.interfaces;

import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.utils.Position;

public interface IResourceRegister {
	
	public static final String rans = "resourceregister.ra";
	
	// boolean return due connectivity issues
	public boolean register(ResourceData resourceData);
	
	public boolean registerLocation(Position position, ResourceData resourceData);
	
	public boolean registerInPlace(String placeName, Position position, ResourceData resourceData);
	
	public boolean unregister(String rai);

}
