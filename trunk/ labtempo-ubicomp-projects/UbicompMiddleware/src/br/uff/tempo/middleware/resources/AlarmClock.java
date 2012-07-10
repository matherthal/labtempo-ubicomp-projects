package br.uff.tempo.middleware.resources;

import android.content.Intent;
import br.uff.tempo.middleware.management.ResourceAgent;
import br.uff.tempo.middleware.resources.interfaces.IAlarmClock;

public class AlarmClock extends ResourceAgent implements IAlarmClock {
	
	public void setAlarm() {
//		Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
//	    i.putExtra(AlarmClock.EXTRA_MESSAGE, "MMTS train to catch rush up ...");
//	    i.putExtra(AlarmClock.EXTRA_HOUR, d.getHours());
//	    i.putExtra(AlarmClock.EXTRA_MINUTES, d.getMinutes());
//	    activity.startActivity(i);
	}

	@Override
	public void notificationHandler(String change) {
		// TODO Auto-generated method stub

	}

}
