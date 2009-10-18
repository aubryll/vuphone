/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.filters;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Represents a start and end time to filter events by. Events who have a start
 * time before the set end time, AND an end time after the set start time, will
 * pass this filter. All other events will fail
 * 
 * @author Hamilton Turner
 * 
 */
public class TimeFilter {

	/** 
	 * Used to fetch the start time of this filter
	 * @return
	 */
	public GregorianCalendar getStartTime() {
		return new GregorianCalendar();
	}

	/** 
	 * Used to fetch the end time of this filter
	 * @return
	 */
	public GregorianCalendar getEndTime() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.HOUR_OF_DAY, 24);
		return cal;
	}
}
