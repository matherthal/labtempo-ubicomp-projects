package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;

public class ResourceDiscoveryStub extends ResourceAgentStub implements IResourceDiscovery {
	
	private static final long serialVersionUID = 1L;

	public ResourceDiscoveryStub(String calleeID) {
		super(calleeID);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<String> search(String query) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>("java.lang.String", query));

		return (ArrayList<String>) makeCall("search", params);
	}

	public ResourceAgent get(String rai) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPath(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
