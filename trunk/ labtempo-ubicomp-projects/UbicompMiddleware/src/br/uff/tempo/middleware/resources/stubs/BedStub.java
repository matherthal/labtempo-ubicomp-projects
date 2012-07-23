package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class BedStub extends ResourceAgentStub implements IBed {

	public BedStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public void notificationHandler(String change) {
	}

	@Override
	public boolean occupied() {
		List<Tuple> params = new ArrayList<Tuple>();
		return (Boolean) makeCall("hasSomeoneIn", params);
	}

	@Override
	public void lieDown() {
		List<Tuple> params = new ArrayList<Tuple>();
		makeCall("lieDown", params);
	}

	@Override
	public void getOut() {
		List<Tuple> params = new ArrayList<Tuple>();
		makeCall("getOut", params);
	}
}
