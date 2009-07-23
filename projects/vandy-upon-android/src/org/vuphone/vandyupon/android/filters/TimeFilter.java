/**
 * 
 */
package org.vuphone.vandyupon.android.filters;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Hamilton Turner
 *
 */
public class TimeFilter {

	public GregorianCalendar getStartTime() {
		return new GregorianCalendar();
	}
	
	public GregorianCalendar getEndTime() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.add(Calendar.HOUR_OF_DAY, 24);
		return cal;
	}
}
