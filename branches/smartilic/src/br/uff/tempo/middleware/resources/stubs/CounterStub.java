package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.ICounter;

public class CounterStub extends ResourceAgentStub implements ICounter {
	
	private static final long serialVersionUID = 1L;
	
	public CounterStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public int getCount() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Integer) makeCall("getCount", params, int.class);
	}
	
}
