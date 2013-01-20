package br.uff.tempo.apps.reminder;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import br.uff.tempo.R;
import br.uff.tempo.apps.reminder.dialogs.ITimeAndDateReceiver;
import br.uff.tempo.apps.reminder.dialogs.TimeAndDateDialog;
import br.uff.tempo.middleware.management.ResourceData;
import br.uff.tempo.middleware.management.ResourceDiscovery;
import br.uff.tempo.middleware.management.interfaces.IResourceDiscovery;
import br.uff.tempo.middleware.management.stubs.ResourceDiscoveryStub;
import br.uff.tempo.middleware.resources.Television;

public class ReminderActivity extends Activity implements ITimeAndDateReceiver {

	public static final int DAYS_TO_HOURS = 24;
	public static final int WEEKS_TO_HOURS = 168;
	public static final int MONTHS_TO_HOURS = 730484398;

	private Calendar start;
	private Calendar end;
	private EditText edtName;
	private EditText edtDescription;
	private EditText edtPeriodicity;
	private Spinner spnPeriodicity;
	private TextView txtStartPrescription;
	private TextView txtEndPrescription;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder);

		// Get the widgets references
		edtName = (EditText) findViewById(R.id.edtPrescriptionName);
		edtDescription = (EditText) findViewById(R.id.edtPrescriptionDescription);
		edtPeriodicity = (EditText) findViewById(R.id.edtPrescriptionPeriodicity);
		spnPeriodicity = (Spinner) findViewById(R.id.spnPrescriptionPeriodicity);
		txtStartPrescription = (TextView) findViewById(R.id.txtStartPrescription);
		txtEndPrescription = (TextView) findViewById(R.id.txtEndPrescription);

		start = Calendar.getInstance();
		end = Calendar.getInstance();

		// init text views with current date
		setLabels();
	}

	private void setLabels() {

		String startDate = Prescription.calendarToString(start);
		String endDate = Prescription.calendarToString(end);

		txtStartPrescription.setText(startDate);
		txtEndPrescription.setText(endDate);
	}

	public void onSubmitPrescriptionClick(View v) {

		// Get the prescription information in the form
		String name = edtName.getText().toString();
		String description = edtDescription.getText().toString();
		String periodicity = edtPeriodicity.getText().toString();
		String periodUnit = spnPeriodicity.getSelectedItem().toString();

		int period = getPeriod(periodicity, periodUnit);

		Prescription p = new Prescription(name, start, end, period, description);

		schedule(p);
	}

	private void schedule(Prescription p) {

		// Using Android AlarmManager, to schedule the prescription to
		// appropriate time

		// TODO search about 'PendingIntent'
		Intent i = new Intent(this, ReminderReceiver.class);
		i.putExtra("prescription", p);

		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
				0, i, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

		//Set the Android alarm according the Prescription parameters
		if (p.getPeriod() != 0) {
			am.setRepeating(AlarmManager.RTC_WAKEUP, p.getStartInTimeMillis(),
					p.getPeriodInMillis(), pi);
		} else {
			am.set(AlarmManager.RTC_WAKEUP, p.getStartInTimeMillis(), pi);
		}
	}

	private int getPeriod(String periodicity, String periodUnit) {

		int period = 0;

		if (periodicity.equals("")) {
			return 0;
		}

		try {
			period = Integer.parseInt(periodicity);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException(
					"Invalid periodicity value! Please, enter an integer value!");
		}

		if (periodUnit.equals("Hours")) {
			return period;
		}
		if (periodUnit.equals("Days")) {
			return period * DAYS_TO_HOURS;
		}
		if (periodUnit.equals("Weeks")) {
			return period * WEEKS_TO_HOURS;
		}
		if (periodUnit.equals("Months")) {
			return period * MONTHS_TO_HOURS;
		}

		throw new InvalidParameterException("Invalid Periodicity unity!");
	}

	// Called when user clicks in the 'prescription start' button
	public void onPrescriptionStartClick(View v) {

		new TimeAndDateDialog(this, this, TimeAndDateDialog.START).show();
	}

	// Called when user clicks in the 'prescription end' button
	public void onPrescriptionEndClick(View v) {
		new TimeAndDateDialog(this, this, TimeAndDateDialog.END).show();
	}

	// Called when user sets date and time in the dialog
	@Override
	public void onTimeAndDateChoosed(TimeAndDateDialog dialog) {

		if (dialog.getID() == TimeAndDateDialog.START) {
			start = dialog.getCalendar();
		} else {
			end = dialog.getCalendar();
		}

		setLabels();
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