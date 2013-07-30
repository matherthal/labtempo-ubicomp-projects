package br.uff.tempo.apps.reminder;

import java.io.Serializable;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Television;

public class ReminderReceiver extends BroadcastReceiver {
	
	IResourceDiscovery discovery;

	@Override
	public void onReceive(Context context, Intent intent) {

		Serializable s = intent.getSerializableExtra("prescription");
		// Receive the alarm
		if (s != null) {
			
			discovery = new ResourceDiscoveryStub(IResourceDiscovery.rans);

			Prescription p = (Prescription) s;
			Log.d("MyAlarm", "Name:" + p.getDisplayName());
			Toast.makeText(
					context,
					p.getDisplayName() + " at "
							+ Prescription.calendarToString(p.getStartTime()),
					Toast.LENGTH_LONG).show();

			// TODO set 'PrescriptionManager' agent context variable
			// 'prescription fired'
		} else {

			Log.e("MyAlarm", "Cannot receipt the Alarm correctly");
		}
	}

	public ResourceData search() {

		List<ResourceData> lista = discovery.search(
				ResourceData.TYPE, Television.class.getCanonicalName());

		return lista.get(0);
	}
}
