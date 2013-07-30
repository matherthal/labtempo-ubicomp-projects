package br.uff.tempo.apps.reminder.dialogs;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import br.uff.tempo.R;

public class TimeAndDateDialog extends Dialog implements OnClickListener {

	public static final int START = 0;
	public static final int END = 1;
	
	private int id;
	private ITimeAndDateReceiver receiver;
	private TimePicker time;
	private DatePicker date;
	private Calendar calendar;
	
	
	public TimeAndDateDialog(ITimeAndDateReceiver receiver, Context context) {
		this(receiver, context, START);
	}
	
	public TimeAndDateDialog(ITimeAndDateReceiver receiver, Context context, int id) {
		super(context);
		
		this.id = id;
		this.receiver = receiver;
		init();
	}
	
	private final void init() {
		
		setContentView(R.layout.time_and_date_dialog);
		setTitle("Choose Time and Date");
		
		Button btnOk = (Button) this.findViewById(R.id.btnChooseTimeAnDate);
		btnOk.setOnClickListener(this);
		
		time = (TimePicker) this.findViewById(R.id.timePicker);
		date = (DatePicker) this.findViewById(R.id.datePicker);
		
		this.setCanceledOnTouchOutside(true);
	}

	public Calendar getCalendar() {
		return this.calendar;
	}
	
	public int getID() {
		return this.id;
	}

	@Override
	public void onClick(View v) {
	
		//Gets the information that was set
		int hour = time.getCurrentHour();
		int minute = time.getCurrentMinute();
		
		int day = date.getDayOfMonth();
		int month = date.getMonth();
		int year = date.getYear();
		
		//Sets the selected date and timer in a calendar
		calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, minute);
		
		//Sends an event to the caller
		receiver.onTimeAndDateChoosed(this);
		
		//Closes the dialog
		this.dismiss();
	}
}
