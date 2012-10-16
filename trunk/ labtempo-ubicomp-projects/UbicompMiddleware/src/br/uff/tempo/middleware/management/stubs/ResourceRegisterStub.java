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
	
	private static final long serialVersionUID = 1L;

	public ResourceRegisterStub(String calleeID) {
		super(calleeID);
	}

	public boolean register(String rans, String ip, int prefix, String rai) {

		// Set params
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		Type rAType = new TypeToken<ResourceAgent>() {
		}.getType();

		params.add(new Tuple<String, Object>(String.class.getName(), rans));
		params.add(new Tuple<String, Object>(String.class.getName(), ip));
		params.add(new Tuple<String, Object>(Integer.class.getName(), prefix));
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		return (Boolean) makeCall("register", params, Boolean.class);
	}

	public boolean unregister(String rai) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		return (Boolean) makeCall("unregister", params, Boolean.class);
	}

	@Override
	public boolean registerLocation(String rans, String ip, int prefix, String rai, Position position) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rans));
		params.add(new Tuple<String, Object>(String.class.getName(), ip));
		params.add(new Tuple<String, Object>(Integer.class.getName(), prefix));
		params.add(new Tuple<String, Object>(String.class.getName(), rai));
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		
		return (Boolean) makeCall("registerLocation", params, Boolean.class);		
	}

	@Override
	public boolean registerInPlace(String rans, String ip, int prefix, String rai, String placeName, Position position) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rans));
		params.add(new Tuple<String, Object>(String.class.getName(), ip));
		params.add(new Tuple<String, Object>(Integer.class.getName(), prefix));
		params.add(new Tuple<String, Object>(String.class.getName(), rai));
		params.add(new Tuple<String, Object>(String.class.getName(), placeName));
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		
		return (Boolean) makeCall("registerInPlace", params, Boolean.class);	
	}

}
