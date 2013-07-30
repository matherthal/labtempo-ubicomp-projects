package br.uff.tempo.middleware.resources.interfaces;

import java.util.Calendar;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent.ContextVariable;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent.Service;

public interface IAlarmClock {

	@Service(name = "Programa alarme", type = "ScheduleAlarm")
	void scheduleAlarm(String name, Calendar start);

	@Service(name = "Programa despertador periódico", type = "ScheduleAlarm")
	void setPeriodicAlarm(String name, Calendar start, int interval);

	@Service(name = "Disparar alarme", type = "ScheduleAlarm")
	void goOff();

	@Service(name = "Soneca", type = "ScheduleAlarm")
	void snooze(int timeInSec);

	@Service(name = "Parar alarme", type = "Stop")
	void stop();

	@ContextVariable(name = "Alarme disparou", type = "WentOff")
	void wentOff();

}
