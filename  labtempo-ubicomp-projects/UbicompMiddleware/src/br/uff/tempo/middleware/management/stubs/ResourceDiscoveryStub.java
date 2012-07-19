package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;

public class ResourceDiscoveryStub extends ResourceAgentStub implements
		IResourceDiscovery {

	public ResourceDiscoveryStub(String calleeID) {
		super(calleeID);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<String> search(String query) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("query", query));

		return (ArrayList<String>) makeCall("search", params);
	}

	public ResourceAgent get(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}
