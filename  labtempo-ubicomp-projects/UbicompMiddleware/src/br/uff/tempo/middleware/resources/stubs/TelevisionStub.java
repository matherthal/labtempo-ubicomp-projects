package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class TelevisionStub extends ResourceAgentStub implements ITelevision {

	public TelevisionStub(String calleeID) {
		super(calleeID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void showMessage(String text) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(String.class.getName(), text));

		makeCall("showMessage", params);
	}

	@Override
	public int getChannel() {

		List<Tuple> params = new ArrayList<Tuple>();
		// params.add(new Tuple<String, Object>("text", text));

		return (Integer) makeCall("getChannel", params);
	}

	@Override
	public void setChannel(int channel) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), channel));

		makeCall("setChannel", params);
	}

	@Override
	public boolean isOn() {
		List<Tuple> params = new ArrayList<Tuple>();
		// params.add(new Tuple<String, Object>("text", text));

		return (Boolean) makeCall("isOn", params);
	}

	@Override
	public void setOn(boolean on) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Boolean.class.getName(), on));

		makeCall("setOn", params);
	}

	@Override
	public int getVolume() {

		List<Tuple> params = new ArrayList<Tuple>();
		// params.add(new Tuple<String, Object>("text", text));

		return (Integer) makeCall("getVolume", params);
	}

	@Override
	public void setVolume(int volume) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), volume));

		makeCall("setVolume", params);
	}

	@Override
	public void incChannel() {

		List<Tuple> params = new ArrayList<Tuple>();
		// params.add(new Tuple<String, Object>("volume", volume));

		makeCall("incChannel", params);
	}

	@Override
	public void decChannel() {

		List<Tuple> params = new ArrayList<Tuple>();
		// params.add(new Tuple<String, Object>("volume", volume));

		makeCall("decChannel", params);
	}

	@Override
	public void incVolume(int inc) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), inc));

		makeCall("incVolume", params);
	}

	@Override
	public void decVolume(int dec) {

		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), dec));

		makeCall("decVolume", params);
	}

}