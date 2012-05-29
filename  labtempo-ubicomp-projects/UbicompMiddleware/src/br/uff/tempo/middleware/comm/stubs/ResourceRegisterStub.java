package br.uff.tempo.middleware.comm.stubs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.IResourceRegister;
import br.uff.tempo.middleware.management.ResourceAgent;

public class ResourceRegisterStub extends Stub implements IResourceRegister{
	
	public ResourceRegisterStub(String calleeID) {
		super(calleeID);
	}

	public boolean register(String url) {
		// The remote method to call
		String method = "register";

		try {
			// Set params
			List<Tuple> params = new ArrayList<Tuple>();
			Type rAType = new TypeToken<ResourceAgent>() {}.getType();

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
