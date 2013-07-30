package br.uff.tempo.middleware.resources;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IAlarmClock;

public class AlarmClock extends ResourceAgent implements IAlarmClock {
	
	private static final long serialVersionUID = 1L;
	
	private String TAG = "AlarmClock";
	private Context context;
	private AlarmManager alarm;
	private PendingIntent sender;
	// private Set<Map<String, Calendar, Integer>> alarmSet;
	private String id;

	public AlarmClock() {
		super("AlarmClock", AlarmClock.class.getName(), "AlarmClock");
	}
	
	@Override
	public void scheduleAlarm(String id, Calendar start) {
		// get a Calendar object with current time
		// Calendar cal = Calendar.getInstance();
		// add 5 minutes to the calendar object
		// cal.add(Calendar.SECOND, 15);
		Intent intent = new Intent(context, AlarmReceiver.class);
		// intent.putExtra("alarm_message", "O'Doyle Rules!");
		// In reality, you would want to have a static variable for the request
		// code instead of 192837
		sender = PendingIntent.getBroadcast(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Get the AlarmManager service
		alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
		alarm.set(AlarmManager.RTC_WAKEUP, start.getTimeInMillis(), sender);
		this.id = id;
	}

	@Override
	public void setPeriodicAlarm(String id, Calendar start, int interval) {
		// get a Calendar object with current time
		// Calendar cal = Calendar.getInstance();
		// add 5 minutes to the calendar object
		// cal.add(Calendar.SECOND, 15);
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra("cv", "Programa despertador periódico");
		// In reality, you would want to have a static variable for the request
		// code instead of 192837
		sender = PendingIntent.getBroadcast(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Get the AlarmManager service
		alarm = (AlarmManager) context.getSystemService(ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, start.getTimeInMillis(), interval, sender);
		this.id = id;
	}

	@Override
	public void goOff() {

	}

	@Override
	public void stop() {
		alarm.cancel(sender);
	}

	@Override
	public void snooze(int timeInSec) {
		// alarm.
	}

	@Override
	public void wentOff() {
		notifyStakeholders("Alarme disparou", "");
	}

	@Override
	public void notificationHandler(String rai, String method, Object value) {
	}
}
