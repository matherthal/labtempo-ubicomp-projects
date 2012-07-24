package br.uff.tempo.middleware.resources.stubs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.uff.tempo.middleware.comm.current.api.Tuple;
import br.uff.tempo.middleware.management.stubs.ResourceAgentStub;
import br.uff.tempo.middleware.resources.interfaces.IAlarmClock;

public class AlarmClockStub extends ResourceAgentStub implements IAlarmClock {

	public AlarmClockStub(String url) {
		super(url);
	}

	@Override
	public void scheduleAlarm(String name, Calendar start) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("name", name));
		params.add(new Tuple<String, Object>("start", start));
		makeCall("turnOnBurner", params);
	}

	@Override
	public void setPeriodicAlarm(String name, Calendar start, int interval) {
		List<Tuple> params = new ArrayList<Tuple>();
		params.add(new Tuple<String, Object>("name", name));
		params.add(new Tuple<String, Object>("start", start));
		params.add(new Tuple<String, Object>("interval", interval));
		makeCall("turnOnBurner", params);
	}

	@Override
	public void goOff() {
		makeCall("turnOnBurner", new ArrayList<Tuple>());
	}

	@Override
	public void snooze(int timeInSec) {
		makeCall("turnOnBurner", new ArrayList<Tuple>());
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
