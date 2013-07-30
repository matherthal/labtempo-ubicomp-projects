package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IStove;

public class StoveStub extends ResourceAgentStub implements IStove {
	
	private static final long serialVersionUID = 1L;

	public StoveStub(String rai) {
		super(rai);
	}

	@Override
	public boolean isOn() {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		// params.add(new Tuple<String, Object>("burnerIndex", burnerIndex));

		return (Boolean) makeCall("turnOnBurner", params, boolean.class);
	}

	@Override
	public void turnOnBurner(int burnerIndex) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), burnerIndex));

		makeCall("turnOnBurner", params, void.class);

	}

	@Override
	public void turnOffBurner(int burnerIndex) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), burnerIndex));

		makeCall("turnOffBurner", params, void.class);

	}

	@Override
	public boolean isOnBurner(int burnerIndex) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), burnerIndex));

		return (Boolean) makeCall("isOnBurner", params, boolean.class);

	}

	@Override
	public void setOvenTemperature(float newTemperature) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Float.class.getName(), newTemperature));

		makeCall("setOvenTemperature", params, void.class);
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
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		makeCall("turnOffOven", params, void.class);
	}

	@Override
	public void turnOnOven() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		makeCall("turnOnOven", params, void.class);
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
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		return (Boolean) makeCall("isOvenOn", params, boolean.class);
	}

}
