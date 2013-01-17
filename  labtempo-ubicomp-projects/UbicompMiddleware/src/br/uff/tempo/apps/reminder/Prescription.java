package br.uff.tempo.apps.reminder;

import java.util.Calendar;
import java.util.Random;

public class Prescription {
	
	private int id; //Prescription ID
	private Calendar startTime;
	private Calendar endTime;
	private int period; //Period time in hours
	String displayName; //Name that will be displayed
	String description;
	
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
	
	//Getters
	public long getStartInTimeMillis() {
		return startTime.getTimeInMillis();
	}
	
	public long getEndInTimeMillis() {
		return endTime.getTimeInMillis();
	}
	
	public long getPeriodInMillis() {
		return period * 3600000;
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
