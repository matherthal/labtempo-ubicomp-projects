package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IPlace;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.utils.Space;

public class ResourceLocationStub extends Stub implements IResourceLocation {

	private static final long serialVersionUID = 1L;

	public ResourceLocationStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public void addPlace(IPlace place) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		params.add(new Tuple<String, Object>(String.class.getName(), place));
		
		makeCall("addPlace", params);
	}

	@Override
	public void addPlace(String name, Position lower, Position upper) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		params.add(new Tuple<String, Object>(String.class.getName(), name));
		params.add(new Tuple<String, Object>(Position.class.getName(), lower));
		params.add(new Tuple<String, Object>(Position.class.getName(), upper));

		makeCall("addPlace", params);
	}

	@Override
	public ArrayList<String> search(String query) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), query));

		return (ArrayList<String>) makeCall("search", params);
	}

	@Override
	public Set<String> getPlacesNames() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Set<String>) makeCall("getPlacesNames", params, Set.class);
	}

	@Override
	public Position getPosition(String place, String rai) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), place));
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		return (Position) makeCall("getPosition", params, Position.class);
	}

	@Override
	public IPlace getPlace(String name) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		params.add(new Tuple<String, Object>(String.class.getName(), name));

		return (IPlace) makeCall("getPlace", params, IPlace.class);
	}

	@Override
	public Collection<IPlace> getAllPlaces() {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Collection<IPlace>) makeCall("getPlaces", params, Collection.class);
	}

	@Override
	public IPlace get(ResourceAgent rA) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(ResourceAgent.class.getName(), rA));

		return (IPlace) makeCall("get", params, IPlace.class);
	}

	@Override
	public IPlace getLocal(Position position) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));

		return (IPlace) makeCall("getLocal", params, IPlace.class);
	}

	@Override
	public Space getMap() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		
		return (Space) makeCall("getLocal", params, Space.class);
	}
	
	@Override
	public void setMap(Space newSpace) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), newSpace));

		makeCall("setMap", params);
	}

	@Override
	public void setMap(Map<String, IPlace> newMap) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), newMap));

		makeCall("setMap", params);
	}

	@Override
	public void registerInPlace(String url, Position position) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), url));
		params.add(new Tuple<String, Object>(Position.class.getName(), position));

		makeCall("registerInPlace", params);
	}

	@Override
	public void registerInPlaceRelative(String url, IPlace place,
			Position position) {
		
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), url));
		params.add(new Tuple<String, Object>(Position.class.getName(), place));
		params.add(new Tuple<String, Object>(Position.class.getName(), position));

		makeCall("registerInPlace", params);
	}

	@Override
	public void registerInPlaceMiddlePos(String url, IPlace place) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), url));
		params.add(new Tuple<String, Object>(Position.class.getName(), place));

		makeCall("registerInPlaceMiddlePos", params);
	}

	@Override
	public ArrayList<String> queryByLocal(Position position) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));

		return (ArrayList<String>) makeCall("queryByLocal", params);
	}

}
