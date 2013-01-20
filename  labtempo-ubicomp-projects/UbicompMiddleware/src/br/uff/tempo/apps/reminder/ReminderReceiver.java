package br.uff.tempo.apps.reminder;

import java.io.Serializable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Serializable s = intent.getSerializableExtra("prescription");
		//Receive the alarm
		if (s != null) {
			
			Prescription p = (Prescription)s;
			Log.d("MyAlarm", "Name:" + p.getDisplayName());
			Toast.makeText(context, p.getDisplayName() + " at " + Prescription.calendarToString(p.getStartTime()), Toast.LENGTH_LONG).show();
		}
		
		
	}

}
