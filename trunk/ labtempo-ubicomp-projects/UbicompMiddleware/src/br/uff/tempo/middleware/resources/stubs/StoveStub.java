package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class StoveStub extends ResourceAgentStub implements IStove {

	public StoveStub(String url) {
		super(url);
	}

	@Override
	public boolean isOn() {

		List<Tuple> params = new ArrayList<Tuple>();
		// params.add(new Tuple<String, Object>("burnerIndex", burnerIndex));

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
		// The remote method to call
		// String method = "getIsOn";
		//
		// try {
		// // Create message
		// String msg = JSONHelper.createMethodCall(method, null);
		// // Get answer from remote method call
		// return (Boolean) JSONHelper.getMessage(caller.sendMessage(msg));
		// } catch (JSONException e) {
		// e.printStackTrace();
		// return false;
		// }
		return 0f;
	}

	@Override
	public void turnOffOven() {
		// TODO Auto-generated method stub

	}

	@Override
	public float getBurnerTemperature(int burnerIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getGasLeak() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isOvenOn() {
		List<Tuple> params = new ArrayList<Tuple>();
		return (Boolean) makeCall("isOvenOn", params);
	}

}
