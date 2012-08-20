package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.utils.Position;
import br.uff.tempo.middleware.management.Local;

public class ResourceLocationStub extends Stub implements IResourceLocation {

	public ResourceLocationStub(String calleeID) {
		super(calleeID);
	}

	public ArrayList<Local> search(ResourceAgent rA) {
		String method = "search";

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(ResourceAgent.class.getName(), rA));

		return (ArrayList<Local>) makeCall("search", params);

	}

	public Local get(ResourceAgent rA) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(ResourceAgent.class.getName(), rA));

		return (Local) makeCall("get", params);
	}
	
	public Local getLocal(Position position) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		
		return (Local) makeCall("getLocal", params);
	}

}
