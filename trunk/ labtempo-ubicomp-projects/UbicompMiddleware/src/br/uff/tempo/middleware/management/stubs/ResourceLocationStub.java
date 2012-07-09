package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
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