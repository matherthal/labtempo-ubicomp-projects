package br.uff.tempo.middleware.resources.interfaces;

import java.util.Calendar;

import br.uff.tempo.middleware.management.interfaces.IResourceAgent.ContextVariable;
import br.uff.tempo.middleware.management.interfaces.IResourceAgent.Service;

public interface IAlarmClock {

	@Service(name="Programa alarme")
	void scheduleAlarm(String name, Calendar start);

	@Service(name="Programa despertador periódico")
	void setPeriodicAlarm(String name, Calendar start, int interval);

	@Service(name="Disparar alarme")
	void goOff();

	@Service(name="Soneca")
	void snooze(int timeInSec);

	@Service(name="Parar alarme")
	void stop();

	@ContextVariable(name="Alarme disparou")
	void wentOff();

}
