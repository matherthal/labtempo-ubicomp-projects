package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceLocation;
import br.uff.tempo.middleware.management.utils.Local;

public class ResourceLocationStub extends Stub implements IResourceLocation {

	public ResourceLocationStub(String calleeID) {
		super(calleeID);
	}

	public ArrayList<Local> search(ResourceAgent rA) {
		String method = "search";

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("rA", rA));

		return (ArrayList<Local>) makeCall("search", params);

	}

	public Local get(ResourceAgent rA) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("rA", rA));

		return (Local) makeCall("get", params);
	}

}
