package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.SmartObject;
import br.uff.tempo.middleware.management.interfaces.IRuleInterpreter;

public class RuleInterpreterStub extends ResourceAgentStub implements IRuleInterpreter {

	public RuleInterpreterStub(String rans) {
		super(rans);
	}

	@Override
	public void start() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		makeCall("start", params, void.class);
	}

	@Override
	public void stop() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		makeCall("stop", params, void.class);
	}

	@Override
	public boolean isStarted() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Boolean) makeCall("isStarted", params, boolean.class);
	}
}
