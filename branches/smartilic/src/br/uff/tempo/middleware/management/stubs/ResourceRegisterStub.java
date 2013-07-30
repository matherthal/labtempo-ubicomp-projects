package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceRegister;
import br.uff.tempo.middleware.management.utils.Position;

public class ResourceRegisterStub extends ResourceAgentStub implements IResourceRegister {
	
	private static final long serialVersionUID = 1L;

	public ResourceRegisterStub(String calleeID) {
		super(calleeID);
	}
	
	@Override
	public boolean register(ResourceData resourceData) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(ResourceData.class.getName(), resourceData));

		return (Boolean) makeCall("register", params, Boolean.class);
	}

	public boolean unregister(String rai) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), rai));

		return (Boolean) makeCall("unregister", params, Boolean.class);
	}

	@Override
	public boolean registerLocation(Position position, ResourceData resourceData) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		params.add(new Tuple<String, Object>(ResourceData.class.getName(), resourceData));
		
		return (Boolean) makeCall("registerLocation", params, Boolean.class);		
	}

	@Override
	public boolean registerInPlace(String placeName, Position position, ResourceData resourceData) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), placeName));
		params.add(new Tuple<String, Object>(Position.class.getName(), position));
		params.add(new Tuple<String, Object>(ResourceData.class.getName(), resourceData));
		
		return (Boolean) makeCall("registerInPlace", params, Boolean.class);	
	}

}
