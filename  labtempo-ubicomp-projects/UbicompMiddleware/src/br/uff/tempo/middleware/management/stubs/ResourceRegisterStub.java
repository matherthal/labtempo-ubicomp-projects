package br.uff.tempo.middleware.management.stubs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.uff.tempo.middleware.comm.Caller;
import br.uff.tempo.middleware.comm.JSONHelper;
import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;

public class ResourceRegisterStub extends ResourceAgentStub implements
		IResourceRegister {

	public ResourceRegisterStub(String calleeID) {
		super(calleeID);
	}

	public boolean register(String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();

		Type rAType = new TypeToken<ResourceAgent>() {
		}.getType();

		params.add(new Tuple<String, Object>("url", url));

		return (Boolean) makeCall("register", params);
	}

	public boolean unregister(String url) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("url", url));

		return (Boolean) makeCall("register", params);
	}

}
