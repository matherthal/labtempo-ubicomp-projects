package br.uff.tempo.apps.reminder;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.os.Bundle;
import android.view.View;
import br.uff.tempo.R;
import br.uff.tempo.apps.reminder.dialogs.ITimeAndDateReceiver;
import br.uff.tempo.apps.reminder.dialogs.TimeAndDateDialog;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.ResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Television;

public class ReminderActivity extends Activity implements ITimeAndDateReceiver {

	private Calendar start;
	private Calendar end;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder);
	}

	public void onSubmitPrescriptionClick() {

		Prescription p = new Prescription("Name", start, end, 4);

		//Using Android AlarmManager, to schedule the prescription to appropriate time
		//TODO search about 'PendingIntent'
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, p.getStartInTimeMillis(), p.getPeriodInMillis(), null);
	}
	
	//Called when user clicks in the 'prescription start' button
	public void onPrescriptionStartClick(View v) {

		new TimeAndDateDialog(this, this, TimeAndDateDialog.START).show();
	}

	//Called when user clicks in the 'prescription end' button
	public void onPrescriptionEndClick(View v) {
		new TimeAndDateDialog(this, this, TimeAndDateDialog.END).show();
	}

	//Called when user sets date and time in the dialog
	@Override
	public void onTimeAndDateChoosed(TimeAndDateDialog dialog) {
		
		if (dialog.getID() == TimeAndDateDialog.START) {
			start = dialog.getCalendar();
		} else {
			end = dialog.getCalendar();
		}
	}

	// TODO Move this method to an appropriated place
	public void search() {

		IResourceDiscovery discovery = new ResourceDiscoveryStub(
				IResourceDiscovery.rans);

		List<ResourceData> lista = discovery.searchForAttribute(
				ResourceData.TYPE, Television.class.getCanonicalName());

		ResourceDiscovery dis;
	}
}