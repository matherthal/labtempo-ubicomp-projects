package br.uff.tempo.middleware.comm.stubs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.IResourceRegister;
import br.uff.tempo.middleware.management.ResourceAgent;

public class ResourceRegisterStub extends Stub implements IResourceRegister{
	
	public ResourceRegisterStub(String serverIP) {
		super(serverIP);
	}

	public boolean register(ResourceAgent rA) {
		// The remote method to call
		String method = "register";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("rA", rA));
			// Create message
			String msg = JSONHelper.createMethodCall(method, params);
			// Get answer from remote method call
			return (Boolean)JSONHelper.getMessage(caller.sendMessage(msg));
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean unregister(String url) {
		// The remote method to call
		String method = "unregister";
		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			params.add(new Tuple<String, Object>("url", url));
			// Create message
			String msg = JSONHelper.createMethodCall(method, params);
			// Get answer from remote method call
			return (Boolean)JSONHelper.getMessage(caller.sendMessage(msg));
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

}
