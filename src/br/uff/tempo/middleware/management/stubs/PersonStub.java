package br.uff.tempo.middleware.management.stubs;

import java.util.ArrayList;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.SmartObject;
import br.uff.tempo.middleware.management.interfaces.IPerson;
import br.uff.tempo.middleware.management.utils.Position;

public class PersonStub extends ResourceAgentStub implements IPerson {

	private static final long serialVersionUID = 1L;

	public PersonStub (String rai) {
		super(rai);
	}

	@Override
	public SmartObject setSensor(int i, SmartObject sensor) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(int.class.getName(), i));
		params.add(new Tuple<String, Object>(SmartObject.class.getName(), sensor));

		return (SmartObject) makeCall("setSensor", params, SmartObject.class);
	}

	@Override
	public void addSensor(SmartObject sensor) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(SmartObject.class.getName(), sensor));

		makeCall("addSensor", params, void.class);
	}

	@Override
	public boolean removeSensor(SmartObject sensor) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(SmartObject.class.getName(), sensor));

		return (Boolean) makeCall("removeSensor", params, Boolean.class);
	}

	@Override
	public Position getPositionIndex(int i) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(int.class.getName(), i));

		return (Position) makeCall("getPositionIndex", params, Position.class);
	}
	
	@Override
	public Position getPosition() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Position) makeCall("getPosition", params, Position.class);
	}

	@Override
	public Position getCurrentPosition() {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();

		return (Position) makeCall("getCurrentPosition", params, Position.class);
	}

	@Override
	public void addPosition(Position position) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));

		makeCall("addPosition", params, void.class);
	}

	@Override
	public boolean removePosition(Position position) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(Position.class.getName(), position));

		return (Boolean) makeCall("removePosition", params, Boolean.class);
	}

}
