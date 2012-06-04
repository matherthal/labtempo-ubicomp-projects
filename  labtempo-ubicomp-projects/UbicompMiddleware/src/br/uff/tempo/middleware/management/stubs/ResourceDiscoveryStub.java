package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;

public class ResourceDiscoveryStub extends ResourceAgentStub implements IResourceDiscovery{
	
	

	public ResourceDiscoveryStub(String calleeID) {
		super(calleeID);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<String> search(String query) {
		String method = "search";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("query", query));
			// Create message
			String msg = JSONHelper.createMethodCall(method, params);
			// Get answer from remote method call

			ArrayList<String> result = (ArrayList<String>)JSONHelper.getMessage(caller.sendMessage(msg));
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ResourceAgent get(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}
