package br.uff.tempo.middleware.management.stubs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;

import com.google.gson.reflect.TypeToken;

public class ResourceRegisterStub extends ResourceAgentStub implements IResourceRegister {

	public ResourceRegisterStub(String calleeID) {
		super(calleeID);
	}

	public boolean register(String url) {

		// Set params
		List<Tuple> params = new ArrayList<Tuple>();

		Type rAType = new TypeToken<ResourceAgent>() {
		}.getType();

		params.add(new Tuple<String, Object>(String.class.getName(), url));

		return (Boolean) makeCall("register", params);
	}

	public boolean unregister(String url) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));

		return (Boolean) makeCall("register", params);
	}

}
