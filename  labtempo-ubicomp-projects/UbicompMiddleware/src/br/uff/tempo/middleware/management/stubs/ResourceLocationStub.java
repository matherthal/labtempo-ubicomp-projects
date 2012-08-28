package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

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

}
