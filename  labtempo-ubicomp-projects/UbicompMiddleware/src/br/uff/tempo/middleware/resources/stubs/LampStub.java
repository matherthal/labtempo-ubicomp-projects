package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.ILamp;

public class LampStub extends ResourceAgentStub implements ILamp {

	public LampStub(String url) {
		super(url);
	}

	@Override
	@ContextVariable(name = "Lampada ligada")
	public boolean isOn() {
		List<Tuple> params = new ArrayList<Tuple>();
		return (Boolean) makeCall("turnOnBurner", params);
	}

	@Override
	@Service(name = "Acender lampada")
	public void turnOn() {
		List<Tuple> params = new ArrayList<Tuple>();
		makeCall("turnOn", params);
	}

	@Override
	@Service(name = "Apagar lampada")
	public void turnOff() {
		List<Tuple> params = new ArrayList<Tuple>();
		makeCall("turnOff", params);
	}

}