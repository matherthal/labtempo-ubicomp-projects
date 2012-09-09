package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IBed;

public class BedStub extends ResourceAgentStub implements IBed {

	public BedStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}

	@Override
	public boolean occupied() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Boolean) makeCall("hasSomeoneIn", params);
	}

	@Override
	public void lieDown() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		makeCall("lieDown", params);
	}

	@Override
	public void getOut() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		makeCall("getOut", params);
	}
}
