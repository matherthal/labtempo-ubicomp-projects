package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import br.uff.tempo.middleware.management.Place;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

/**
 * Resource Location Service (RLS) interface
 * <br /><br />
 * It enables use of RLS in SmartServer  
 */
public interface IResourceLocation {
	
	/**
	 * Unique system rans of RLS
	 */
	public static final String rans = "resourcelocation.ra";

	void addPlace(Place place);

	void addPlace(String name, Position lower, Position upper);

	ArrayList<String> search(String query);

	Set<String> getPlacesNames();

	Position getPosition(String place, String rai);

	Place getPlace(String name);

	Collection<Place> getAllPlaces();

	Place getLocal(Position position);
	
	String getLocalReference(Position position);
	
	Space getMap();

	void setMap(Space houseMap);

	void registerInPlace(String url, Position position);

	void registerInPlaceRelative(String url, Place place, Position position);
	
	void registerInPlaceMiddlePos(String url, Place place);
	
	ArrayList<String> queryByLocal(Position position);

	/**
	 * Update an specified Resource Agent location indexed in Map structure
	 * @param resource update target
	 */
	public void updateLocation(ResourceData resource);
}
