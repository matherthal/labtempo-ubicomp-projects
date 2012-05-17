package br.uff.tempo.middleware.comm.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.IResourceLocation;
import br.uff.tempo.middleware.management.Local;
import br.uff.tempo.middleware.management.ResourceAgent;

public class ResourceLocationStub extends Stub implements IResourceLocation{


	
	public ResourceLocationStub(String serverIP) {
		super(serverIP);
	}

	public ArrayList<Local> search(ResourceAgent rA) {
		String method = "search";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("rA", rA));
			// Create message
			String msg = JSONHelper.createMethodCall(method, params);
			// Get answer from remote method call
			return (ArrayList<Local>)JSONHelper.getMessage(caller.sendMessage(msg));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Local get(ResourceAgent rA) {
		String method = "get";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("rA", rA));
			// Create message
			String msg = JSONHelper.createMethodCall(method, params);
			// Get answer from remote method call
			return (Local)JSONHelper.getMessage(caller.sendMessage(msg));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
