package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.Place;

public class ResourceLocationStub extends Stub implements IResourceLocation {

	public ResourceLocationStub(String calleeID) {
		super(calleeID);
	}

	public ArrayList<Place> search(ResourceAgent rA) {
		String method = "search";

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(ResourceAgent.class.getName(), rA));

		return (ArrayList<Place>) makeCall("search", params);

	}

	public Place get(ResourceAgent rA) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(ResourceAgent.class.getName(), rA));

		return (Place) makeCall("get", params);
	}
	
	public Place getLocal(Position position) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		
		return (Place) makeCall("getLocal", params);
	}

	@Override
	public ArrayList<String> search(String query) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(String.class.getName(), query));

		return (ArrayList<String>) makeCall("search", params);
	}

	@Override
	public Set<String> listLocations() {
		List<Tuple> params = new ArrayList<Tuple>();

		return (Set<String>) makeCall("listLocations", params, Set.class);
	}

	@Override
	public Position getPosition(String place, String rai) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(String.class.getName(), place));
		params.add(new Tuple<String, Object>(String.class.getName(), rai));
		
		return (Position) makeCall("getPosition", params, Position.class);
	}

	@Override
	public ArrayList<String> queryByLocal(Position position) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		
		return (ArrayList<String>) makeCall("queryByLocal", params);
	}

}
