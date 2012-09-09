package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IPresenceSensor;

public class PresenceSensorStub extends ResourceAgentStub implements IPresenceSensor {

	public PresenceSensorStub(String url) {
		super(url);
	}

	@Override
	public boolean getPresence() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Boolean) makeCall("getPresence", params);
	}

	@Override
	public void setPresence(boolean p) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Boolean.class.getName(), p));
		makeCall("setPresence", params);
	}

}
