package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;

public interface IResourceLocation {

	public ArrayList<String> search(String query);
	public ArrayList<String> queryByLocal(Position position);	// return sorted list by
																// proximity
	

	
	public Place get(ResourceAgent rA);
	public Place getLocal(Position position);

	public Set<String> listLocations();
	
	public Position getPosition(String place, String rai);

}
