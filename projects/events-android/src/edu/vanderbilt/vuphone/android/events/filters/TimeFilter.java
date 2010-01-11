/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.filters;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.util.Log;
import edu.vanderbilt.vuphone.android.events.Constants;

/**
 * Represents a start and end time to filter events by. Events who have a start
 * time before the set end time, AND an end time after the set start time, will
 * pass this filter. All other events will fail
 * 
 * @author Hamilton Turner
 * 
 */
public class TimeFilter implements Filter {
	private GregorianCalendar start_;
	private GregorianCalendar end_;

	private String tag = Constants.tag;
	private String pre = "TimeFilter: ";

	public TimeFilter(GregorianCalendar start, GregorianCalendar end) {
		if (start.after(end)) {
			Log.e(tag, pre + "Created a TimeFilter with the start time"
					+ " after the end time. Reversing times");
			start_ = end;
			end_ = start;
			return;
		}
		
		start_ = start;
		end_ = end;

	}

	/**
	 * Used to fetch the start time of this filter
	 * 
	 * @return
	 */
	public GregorianCalendar getStartTime() {
		return start_;
	}

	/**
	 * Used to fetch the end time of this filter
	 * 
	 * @return
	 */
	public GregorianCalendar getEndTime() {
		return end_;
	}
}
