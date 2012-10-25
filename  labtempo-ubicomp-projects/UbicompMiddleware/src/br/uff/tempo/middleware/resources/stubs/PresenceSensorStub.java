package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IPresenceSensor;

public class PresenceSensorStub extends ResourceAgentStub implements IPresenceSensor {
	
	private static final long serialVersionUID = 1L;

	public PresenceSensorStub(String rai) {
		super(rai);
	}

	@Override
	public boolean getPresence() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Boolean) makeCall("getPresence", params, boolean.class);
	}

	@Override
	public void setPresence(boolean p) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Boolean.class.getName(), p));
		makeCall("setPresence", params, void.class);
	}

}
