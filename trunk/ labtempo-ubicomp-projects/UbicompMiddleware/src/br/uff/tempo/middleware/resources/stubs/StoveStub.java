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
		//params.add(new Tuple<String, Object>("query", query));

		//return (ArrayList<String>) makeCall("isOn", params);	

		return false;
	}

	@Override
	public void turnOnBurner(int burnerIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void turnOffBurner(int burnerIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOnBurner(int burnerIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setOvenTemperature(float newTemperature) {
		// TODO Auto-generated method stub

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
