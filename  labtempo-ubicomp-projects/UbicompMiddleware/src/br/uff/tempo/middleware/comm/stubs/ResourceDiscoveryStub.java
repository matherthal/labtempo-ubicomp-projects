package br.uff.tempo.middleware.comm.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.IResourceDiscovery;
import br.uff.tempo.middleware.management.ResourceAgent;

public class ResourceDiscoveryStub extends Stub implements IResourceDiscovery{
	
	

	public ResourceDiscoveryStub(String serverIP) {
		super(serverIP);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<ResourceAgent> search(String query) {
		String method = "search";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("query", query));
			// Create message
			String msg = JSONHelper.createMethodCall(method, params);
			// Get answer from remote method call
			return (ArrayList<ResourceAgent>)JSONHelper.getMessage(caller.sendMessage(msg));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
