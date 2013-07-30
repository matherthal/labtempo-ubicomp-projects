package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.ResourceAgentNS;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;

import com.google.gson.reflect.TypeToken;

public class ResourceDiscoveryStub extends ResourceAgentStub implements IResourceDiscovery {
	
	private static final long serialVersionUID = 1L;

	public ResourceDiscoveryStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public ResourceAgentNS getRANS(String rans) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rans));
		
		return (ResourceAgentNS) makeCall("getRANS", params, ResourceAgentNS.class);
	}
	
	@Override
	public List<ResourceData> search(int attribute, String query) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), attribute));
		params.add(new Tuple<String, Object>(String.class.getName(), query));

		return (List<ResourceData>) makeCall("search", params, new TypeToken<List<ResourceData>>() {}.getType());
	}

}
