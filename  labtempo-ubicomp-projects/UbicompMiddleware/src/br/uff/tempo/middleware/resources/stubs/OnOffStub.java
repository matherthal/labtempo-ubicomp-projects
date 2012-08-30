package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IOnOff;

public class OnOffStub extends ResourceAgentStub implements IOnOff{

	public OnOffStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public void setStatus(boolean isOn) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple(boolean.class.getName(), isOn));
		makeCall("setStatus", params);
	}

	@Override
	public boolean isOn() {
		List<Tuple> params = new ArrayList<Tuple>();
		return (Boolean) makeCall("isOn", params);
	}
	
}
