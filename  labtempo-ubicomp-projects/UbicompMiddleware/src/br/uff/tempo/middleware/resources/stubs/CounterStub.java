package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.ICounter;

public class CounterStub extends ResourceAgentStub implements ICounter{
	
	public CounterStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public int getCount() {
		List<Tuple> params = new ArrayList<Tuple>();
		return (Integer) makeCall("getCount", params);
	}
	
}
