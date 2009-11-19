package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.vanderbilt.vuphone.android.dining.Range;
import edu.vanderbilt.vuphone.android.dining.Time;

public class RestaurantHours {

	private ArrayList<Range> mondayRanges = new ArrayList<Range>();

	public ArrayList<Range> getMondayRanges() {
		return mondayRanges;
	}
	
	public void setMondayRanges(ArrayList<Range> newMondayRange)
	{
		mondayRanges = newMondayRange;
	}

	public int getMondayRangeCount() {
		return mondayRanges.size();
	}

	private ArrayList<Range> tuesdayRanges = new ArrayList<Range>();

	public ArrayList<Range> getTuesdayRanges() {
		return tuesdayRanges;
	}
	
	public void setTuesdayRanges(ArrayList<Range> newTuesdayRange)
	{
		tuesdayRanges = newTuesdayRange;
	}

	public int getTuesdayRangeCount() {
		return tuesdayRanges.size();
	}

	private ArrayList<Range> wednesdayRanges = new ArrayList<Range>();

	public ArrayList<Range> getWednesdayRanges() {
		return wednesdayRanges;
	}

	public void setWednesdayRanges(ArrayList<Range> newWednesdayRange)
	{
		wednesdayRanges = newWednesdayRange;
	}
	
	public int getWednesdayRangeCount() {
		return wednesdayRanges.size();
	}

	private ArrayList<Range> thursdayRanges = new ArrayList<Range>();

	public ArrayList<Range> getThursdayRanges() {
		return thursdayRanges;
	}
	
	public void setThursdayRanges(ArrayList<Range> newThursdayRange)
	{
		thursdayRanges = newThursdayRange;
	}

	public int getThursdayRangeCount() {
		return thursdayRanges.size();
	}

	private ArrayList<Range> fridayRanges = new ArrayList<Range>();

	public ArrayList<Range> getFridayRanges() {
		return fridayRanges;
	}

	public void setFridayRanges(ArrayList<Range> newFridayRange)
	{
		fridayRanges = newFridayRange;
	}
	
	public int getFridayRangeCount() {
		return fridayRanges.size();
	}

	private ArrayList<Range> saturdayRanges = new ArrayList<Range>();

	public ArrayList<Range> getSaturdayRanges() {
		return saturdayRanges;
	}

	public void setSaturdayRanges(ArrayList<Range> newSaturdayRange)
	{
		saturdayRanges = newSaturdayRange;
	}
	
	public int getSaturdayRangeCount() {
		return saturdayRanges.size();
	}

	private ArrayList<Range> sundayRanges = new ArrayList<Range>();

	public ArrayList<Range> getSundayRanges() {
		return sundayRanges;
	}

	public void setSundayRanges(ArrayList<Range> newSundayRange)
	{
		sundayRanges = newSundayRange;
	}
	
	public int getSundayRangeCount() {
		return sundayRanges.size();
	}

	// returns whether now is contained in the ranges of _open
	public boolean isOpen() {
		Calendar c = new GregorianCalendar();
		int day = c.get(Calendar.DAY_OF_WEEK);
		
		ArrayList<Range> ranges = null;
		int numberOfRanges = 0;
		switch (day) {
		case Calendar.MONDAY:
			// use monday
			numberOfRanges = this.getMondayRangeCount();
			ranges = this.getMondayRanges();
			break;
		case Calendar.TUESDAY:
			// use tuesday
			numberOfRanges = this.getTuesdayRangeCount();
			ranges = this.getTuesdayRanges();
			break;
		case Calendar.WEDNESDAY:
			numberOfRanges = this.getWednesdayRangeCount();
			ranges = this.getWednesdayRanges();
			break;
		case Calendar.THURSDAY:
			numberOfRanges = this.getThursdayRangeCount();
			ranges = this.getThursdayRanges();
			break;
		case Calendar.FRIDAY:
			numberOfRanges = this.getFridayRangeCount();
			ranges = this.getFridayRanges();
			break;
		case Calendar.SATURDAY:
			numberOfRanges = this.getSaturdayRangeCount();
			ranges = this.getSaturdayRanges();
			break;
		case Calendar.SUNDAY:
			numberOfRanges = this.getSundayRangeCount();
			ranges = this.getSundayRanges();
			break;
		default:
			// should never get here...
			break;
		}

		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		for (int i = 0; i < numberOfRanges; i++) {
			Range currentRange = ranges.get(i);
			if (currentRange.inRange(new Time(hour, minute)) == true)
				return true;
		}

		// We never returned true, so we are not in any of the
		// Ranges
		return false;

	}

}
