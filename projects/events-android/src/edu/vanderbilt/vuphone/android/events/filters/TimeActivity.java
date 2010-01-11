/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.filters;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import edu.vanderbilt.vuphone.android.events.R;
import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;

/**
 * @author Hamilton Turner
 * 
 */
public class TimeActivity extends Activity {
	private SeekBar startTime_;
	private SeekBar endTime_;
	private TextView startText_;
	private TextView endText_;
	private GregorianCalendar calendar_ = new GregorianCalendar();
	
	/** Keeps us from having to create a new Calendar every time the progress changes */
	private GregorianCalendar secondCalendar = new GregorianCalendar();
	
	private long smallestTime;

	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			calendar_.setTimeInMillis(progress + smallestTime);

			TimeFilter t;
			
			// TODO - make it so that start and end cannot be in the wrong order
			
			if (seekBar.equals(startTime_)) {
				startText_.setText(getTime(calendar_));
				startText_.postInvalidate();
				
				secondCalendar.setTimeInMillis(endTime_.getProgress() + smallestTime);
				t = new TimeFilter(calendar_, secondCalendar);
				
			} else {
				endText_.setText(getTime(calendar_));
				endText_.postInvalidate();
				
				secondCalendar.setTimeInMillis(startTime_.getProgress() + smallestTime);
				t = new TimeFilter(secondCalendar, calendar_);
			}
			
			FilterManager.updateFilter(t);
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
		}

	};

	public void onCreate(Bundle saved) {
		// TODO - load up the current timefilter from the filtermanager and use those values to setup
		super.onCreate(saved);
		setContentView(R.layout.time_filter);

		// Setup seek bars
		startTime_ = (SeekBar) findViewById(R.timeFilter.SB_start_time);
		endTime_ = (SeekBar) findViewById(R.timeFilter.SB_end_time);
		startTime_.setOnSeekBarChangeListener(seekBarListener);
		endTime_.setOnSeekBarChangeListener(seekBarListener);

		// Setup text fields
		startText_ = (TextView) findViewById(R.timeFilter.TV_start_time);
		endText_ = (TextView) findViewById(R.timeFilter.TV_end_time);

		DBAdapter adapter = new DBAdapter(this);
		adapter.openReadable();

		// Converting from database time (seconds) to system time (milliseconds)
		long largest = adapter.getLargestTime() * 1000;
		long smallest = adapter.getSmallestTime() * 1000;
		smallestTime = smallest;
		adapter.close();

		startTime_.setMax((int) (largest - smallest));
		endTime_.setMax((int) (largest - smallest));
		
		calendar_.setTimeInMillis(smallestTime);
		startText_.setText(getTime(calendar_));
		startText_.invalidate();
		
		calendar_.setTimeInMillis(largest);
		endTime_.setProgress(endTime_.getMax());
		endTime_.invalidate();
		endText_.setText(getTime(calendar_));
		endText_.invalidate();
	}

	private String getTime(Calendar c) {
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		StringBuilder s = new StringBuilder();
		s.append(getMonth(month)).append(" ").append(day); // .append("/").append(year);

		// s.append(" ");
		// s.append(hour).append(": ").append(minute);

		return s.toString();
	}

	private String getMonth(int month) {
		switch (month) {
		case Calendar.JANUARY:
			return "Jan";
		case Calendar.FEBRUARY:
			return "Feb";
		case Calendar.MARCH:
			return "Mar";
		case Calendar.APRIL:
			return "Apr";
		case Calendar.MAY:
			return "May";
		case Calendar.JUNE:
			return "Jun";
		case Calendar.JULY:
			return "Jul";
		case Calendar.AUGUST:
			return "Aug";
		case Calendar.SEPTEMBER:
			return "Sep";
		case Calendar.NOVEMBER:
			return "Nov";
		case Calendar.DECEMBER:
			return "Dec";
		default:
			return "Jan";
		}
	}

}
