package br.uff.tempo.apps.reminder;

import java.util.Date;

public class Prescription {
	
	public final static int  HOUR = 0;
	public final static int DAY = 1;
	public final static int WEEK = 2;
	
	String description;
	Date beginTime;
	int frequency;
	int frequencyFlag;
	
	public Prescription(String description, Date beginTime, int frequency, int frequencyFlag) {
		this.description = description;
		this.beginTime = beginTime;
		this.frequency = frequency;
		this.frequencyFlag = frequencyFlag;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setNextTime() {
		int total = beginTime.getHours()+frequency;
		int offset = 0;
		int month = beginTime.getMonth();
		switch (frequencyFlag){
			case HOUR:
				if (total>=24){
					int subTotal = beginTime.getDay()+1;
					offset = verifyMonthBoundaries(subTotal, month);
					beginTime.setDate(subTotal-offset);
					offset = 24;
				}
				beginTime.setHours(total-offset);
				break;
			case DAY:
				total = beginTime.getDay()+frequency;
				offset = verifyMonthBoundaries(total,month);
				beginTime.setDate(total-offset);
				break;
			case WEEK:
				total = beginTime.getDay()+ 7*frequency;
				offset = verifyMonthBoundaries(total,month);
				beginTime.setDate(total - offset);
				break;
		}
		
		
	}

	public int verifyMonthBoundaries(int total, int month){
		int offset = 0;
		int monthAux = 0;
		if (month<12) {
			monthAux = month;
		}
		if (total>28){
			boolean feb = beginTime.getMonth()==2;
			boolean bissextile = beginTime.getYear()%4 == 0;
			if (feb){
				if (!bissextile){				
					beginTime.setMonth(monthAux+1);
					offset = 28;
				} else if (total > 29){
					beginTime.setMonth(monthAux+1);
					offset = 29;
				}	
			} else if (total > 30){
				boolean oddMonth = month%2 == 1;
				boolean monthIndexMinor = month<8;
				boolean monthSize31 = (oddMonth && monthIndexMinor) || (!oddMonth && !monthIndexMinor);
				if (monthSize31 && total>31){
					beginTime.setMonth(monthAux+1);
					offset = 31;
				} else if (!monthSize31){
					beginTime.setMonth(monthAux+1);
					offset = 30;
				}
			}
		}
		return offset;			
	}
	
	public String getDescription() {
		return description;
	}

	public int getFrequency() {
		return frequency;
	}

	public int getFrequencyFlag() {
		return frequencyFlag;
	}
	
	

}
