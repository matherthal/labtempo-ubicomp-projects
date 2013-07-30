package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.ILamp;

public class LampStub extends ResourceAgentStub implements ILamp {
	
	private static final long serialVersionUID = 1L;

	public LampStub(String rai) {
		super(rai);
	}

	@Override
	@ContextVariable(name = "Lampada ligada", type = "OnOff")
	public boolean isOn() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Boolean) makeCall("isOn", params, boolean.class);
	}

	@Override
	@Service(name = "Acender lampada", type = "TurnOnOff")
	public void turnOn() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		makeCall("turnOn", params, void.class);
	}

	@Override
	@Service(name = "Apagar lampada", type = "TurnOnOff")
	public void turnOff() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		makeCall("turnOff", params, void.class);
	}

}
