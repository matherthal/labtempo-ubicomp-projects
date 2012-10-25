package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IOnOff;

public class OnOffStub extends ResourceAgentStub implements IOnOff{
	
	private static final long serialVersionUID = 1L;

	public OnOffStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public void setStatus(boolean isOn) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(boolean.class.getName(), isOn));
		makeCall("setStatus", params, void.class);
	}

	@Override
	public boolean isOn() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Boolean) makeCall("isOn", params, boolean.class);
	}
	
}
