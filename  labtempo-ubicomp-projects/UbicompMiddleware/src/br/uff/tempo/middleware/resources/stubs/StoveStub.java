package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class StoveStub extends ResourceAgentStub implements IStove {

	public StoveStub(String calleeID) {
		super(calleeID);
	}

	@Override
	public boolean isOn() {

		List<Tuple> params = new ArrayList<Tuple>();
		//params.add(new Tuple<String, Object>("burnerIndex", burnerIndex));

		return (Boolean) makeCall("turnOnBurner", params);
	}

	@Override
	public void turnOnBurner(int burnerIndex) {
		
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("burnerIndex", burnerIndex));

		makeCall("turnOnBurner", params);

	}

	@Override
	public void turnOffBurner(int burnerIndex) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("burnerIndex", burnerIndex));

		makeCall("turnOffBurner", params);

	}

	@Override
	public boolean isOnBurner(int burnerIndex) {
		
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("burnerIndex", burnerIndex));

		return (Boolean) makeCall("isOnBurner", params);
		
	}

	@Override
	public void setOvenTemperature(float newTemperature) {
		
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("newTemperature", newTemperature));

		makeCall("setOvenTemperature", params);
	}

	@Override
	public float getOvenTemperature() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void turnOffOven() {
		// TODO Auto-generated method stub

	}

}
