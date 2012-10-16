package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public interface IResourceLocation {
	
	public static final String rans = "resourcelocation.ra";

	void addPlace(Place place);

	void addPlace(String name, Position lower, Position upper);

	ArrayList<String> search(String query);

	Set<String> getPlacesNames();

	Position getPosition(String place, String rai);

	Place getPlace(String name);

	Collection<Place> getAllPlaces();

	Place get(ResourceAgent rA);

	Place getLocal(Position position);
	
	Space getMap();

	void insertMap(Space newSpace);

	void setMap(Map<String, Place> newMap);

	void registerInPlace(String url, Position position);

	void registerInPlaceRelative(String url, Place place, Position position);
	
	void registerInPlaceMiddlePos(String url, Place place);
	
	ArrayList<String> queryByLocal(Position position);
}
