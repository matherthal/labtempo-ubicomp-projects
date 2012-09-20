package br.uff.tempo.middleware.management.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public interface IResourceLocation {

	void addPlace(IPlace place);

	void addPlace(String name, Position lower, Position upper);

	ArrayList<String> search(String query);

	Set<String> getPlacesNames();

	Position getPosition(String place, String rai);

	IPlace getPlace(String name);

	Collection<IPlace> getAllPlaces();

	IPlace get(ResourceAgent rA);

	IPlace getLocal(Position position);
	
	Space getMap();

	void setMap(Space newSpace);

	void setMap(Map<String, IPlace> newMap);

	void registerInPlace(String url, Position position);

	void registerInPlaceRelative(String url, IPlace place, Position position);
	
	void registerInPlaceMiddlePos(String url, IPlace place);
	
	ArrayList<String> queryByLocal(Position position);

}
