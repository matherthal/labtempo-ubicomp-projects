package br.uff.tempo.middleware.resources;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	private String TAG = "AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
		Log.d(TAG, "Alarm worked");
		Toast.makeText(context, "Alarm worked", Toast.LENGTH_LONG).show();

		// try {
		// Bundle bundle = intent.getExtras();
		// String cv = bundle.getString("cv");
		// String url = bundle.getString("url");
		//
		// notifyStakeholders(JSONHelper.createChange(url, cv, ""));
		// } catch (Exception e) {
		// Toast.makeText(
		// context,
		// "There was an error somewhere, but we still received an alarm",
		// Toast.LENGTH_SHORT).show();
		// e.printStackTrace();
		//
		// }
	}
}
