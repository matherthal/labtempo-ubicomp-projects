package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.ITelevision;

public class TelevisionStub extends ResourceAgentStub implements ITelevision {
	
	private static final long serialVersionUID = 1L;

	public TelevisionStub(String calleeID) {
		super(calleeID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void showMessage(String text) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), text));

		makeCall("showMessage", params, void.class);
	}
	
	@Override
	public String getMessage() {
		
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (String) makeCall("getMessage", params, String.class);
	}

	@Override
	public int getChannel() {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		// params.add(new Tuple<String, Object>("text", text));

		return (Integer) makeCall("getChannel", params, int.class);
	}

	@Override
	public void setChannel(int channel) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), channel));

		makeCall("setChannel", params, void.class);
	}

	@Override
	public boolean isOn() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		// params.add(new Tuple<String, Object>("text", text));

		return (Boolean) makeCall("isOn", params, boolean.class);
	}

	@Override
	public void setOn(boolean on) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Boolean.class.getName(), on));

		makeCall("setOn", params, void.class);
	}

	@Override
	public int getVolume() {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		// params.add(new Tuple<String, Object>("text", text));

		return (Integer) makeCall("getVolume", params, int.class);
	}

	@Override
	public void setVolume(int volume) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), volume));

		makeCall("setVolume", params, void.class);
	}

	@Override
	public void incChannel() {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		// params.add(new Tuple<String, Object>("volume", volume));

		makeCall("incChannel", params, void.class);
	}

	@Override
	public void decChannel() {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		// params.add(new Tuple<String, Object>("volume", volume));

		makeCall("decChannel", params, void.class);
	}

	@Override
	public void incVolume(int inc) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), inc));

		makeCall("incVolume", params, void.class);
	}

	@Override
	public void decVolume(int dec) {

		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Integer.class.getName(), dec));

		makeCall("decVolume", params, void.class);
	}
}
