package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IAlarmClock;

public class AlarmClockStub extends ResourceAgentStub implements IAlarmClock {
	
	private static final long serialVersionUID = 1L;

	public AlarmClockStub(String rai) {
		super(rai);
	}

	@Override
	public void scheduleAlarm(String name, Calendar start) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>("name", name));
		params.add(new Tuple<String, Object>("start", start));
		makeCall("turnOnBurner", params, void.class);
	}

	@Override
	public void setPeriodicAlarm(String name, Calendar start, int interval) {
		List<Tuple<String, Object>> params = new ArrayList<Tuple<String, Object>>();
		params.add(new Tuple<String, Object>(String.class.getName(), name));
		params.add(new Tuple<String, Object>(Calendar.class.getName(), start));
		params.add(new Tuple<String, Object>(Integer.class.getName(), interval));
		makeCall("turnOnBurner", params, void.class);
	}

	@Override
	public void goOff() {
		makeCall("turnOnBurner", new ArrayList<Tuple<String, Object>>(), void.class);
	}

	@Override
	public void snooze(int timeInSec) {
		makeCall("turnOnBurner", new ArrayList<Tuple<String, Object>>(), void.class);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void wentOff() {
		// TODO Auto-generated method stub

	}

}
