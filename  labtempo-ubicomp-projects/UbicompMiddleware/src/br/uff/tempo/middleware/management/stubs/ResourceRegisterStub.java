package br.uff.tempo.middleware.management.stubs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.utils.Position;

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

	@Override
	public boolean registerLocation(String url, Position position) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		
		return (Boolean) makeCall("registerLocation", params);		
	}

	@Override
	public boolean registerInPlace(String url, String placeName, Position position) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(String.class.getName(), url));
		params.add(new Tuple<String, Object>(String.class.getName(), placeName));
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		
		return (Boolean) makeCall("registerInPlace", params);	
	}

}
