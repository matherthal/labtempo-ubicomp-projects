package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;
import java.util.Set;

import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;

public interface IResourceLocation {

	public ArrayList<String> search(String query);

	// return sorted list by proximity
	public ArrayList<String> queryByLocal(Position position);

	public Place get(ResourceAgent rA);
	public Place getLocal(Position position);

	public Set<String> listLocations();
	public void addPlace(String name, Position lower, Position upper);
	public Position getPosition(String place, String rai);

}
