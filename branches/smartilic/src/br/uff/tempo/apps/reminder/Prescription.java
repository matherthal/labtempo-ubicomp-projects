package br.uff.tempo.apps.reminder;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Prescription implements Serializable {
	
	private static final long serialVersionUID = 7314780947341327975L;
	
	private int id; //Prescription ID
	private Calendar startTime;
	private Calendar endTime;
	private int period; //Period time in hours
	private String displayName; //Name that will be displayed
	private String description;
	
	//Constructors
	
	public Prescription(String displayName, Calendar endTime, int period) {
		this(displayName, Calendar.getInstance(), endTime, period);
	}
	
	public Prescription(String displayName, Calendar startTime, Calendar endTime, int period) {
		this(displayName, startTime, endTime, period, "");
	}
	
	public Prescription(String displayName, Calendar endTime, int period, String description) {
		this(displayName, Calendar.getInstance(), endTime, period, description);
	}
	
	public Prescription(String displayName, Calendar startTime, Calendar endTime, int period, String description) {
		
		this.displayName = displayName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.period = period;
		this.description = description;
		
		//Generate a random ID
		id = (new Random()).nextInt(1000000) + this.displayName.hashCode();
	}
	
	//General Methods
	public static String calendarToString(Calendar cal) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
		return sdf.format(cal.getTime());
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || !(obj instanceof Prescription)) {
			return false;
		}
		
		return this.id == ((Prescription)obj).getId();
	}
	
	//Getters
	public long getStartInTimeMillis() {
		return startTime.getTimeInMillis();
	}
	
	public long getEndInTimeMillis() {
		return endTime.getTimeInMillis();
	}
	
	public long getPeriodInMillis() {
		return period * 1000;//3600000;
	}

	public Calendar getStartTime() {
		return startTime;
	}
	
	public Calendar getEndTime() {
		return endTime;
	}

	public int getPeriod() {
		return period;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}
}
