package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent;

public class ResourceAgentStub extends Stub implements IResourceAgent {

	public ResourceAgentStub(String calleeID) {
		super(calleeID);
	}

	public void notificationHandler(String change) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("change", change));

		makeCall("notificationHandler", params);

	}

	public String getResourceClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerStakeholder(String method, String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("method", method));
		params.add(new Tuple<String, Object>("url", url));

		makeCall("registerStakeholder", params);
	}

}
