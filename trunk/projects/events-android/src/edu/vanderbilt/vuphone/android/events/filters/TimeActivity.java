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
 * TODO - If the date of events is to large away, such as 6 months, then
 * the largest - smallest times will be larger than Integer.MAX_VALUE, resulting
 * in an endTime with the maxvalue set to a 0, and a useless time filter. This
 * whole filtering concept needs to have a logarithamic scale applied, so the first 
 * 1/4 of the screen is the current day, the next 1/4 the week, then the month, etc...
 */
public class TimeActivity extends Activity {
	private SeekBar startTime_;
	private SeekBar endTime_;
	private TextView startText_;
	private TextView endText_;
	private GregorianCalendar calendar_ = new GregorianCalendar();

	/**
	 * Keeps us from having to create a new Calendar every time the progress
	 * changes
	 */
	private GregorianCalendar secondCalendar = new GregorianCalendar();

	private long smallestTime;

	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			calendar_.setTimeInMillis(progress + smallestTime);

			TimeFilter t;

			if (seekBar.equals(startTime_)) {
				if (progress > endTime_.getProgress())
					endTime_.setProgress(progress + 1);

				startText_.setText(getTime(calendar_));
				startText_.postInvalidate();

				secondCalendar.setTimeInMillis(endTime_.getProgress()
						+ smallestTime);
				t = new TimeFilter(calendar_, secondCalendar);

			} else {
				if (progress < startTime_.getProgress())
					startTime_.setProgress(progress - 1);

				endText_.setText(getTime(calendar_));
				endText_.postInvalidate();

				secondCalendar.setTimeInMillis(startTime_.getProgress()
						+ smallestTime);
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

		TimeFilter current = FilterManager.getCurrentTimeFilter();
		if (current == null) {
			calendar_.setTimeInMillis(smallest);
			startText_.setText(getTime(calendar_));
			startTime_.setProgress(0);
			startTime_.invalidate();
			startText_.invalidate();

			calendar_.setTimeInMillis(largest);
			endText_.setText(getTime(calendar_));
			endTime_.setProgress(endTime_.getMax());
			endTime_.invalidate();
			endText_.invalidate();
		} else {
			calendar_.setTimeInMillis(current.getStartTime().getTimeInMillis());
			startText_.setText(getTime(calendar_));
			startTime_
					.setProgress((int) (calendar_.getTimeInMillis() - smallest));
			startTime_.invalidate();
			startText_.invalidate();

			calendar_.setTimeInMillis(current.getEndTime().getTimeInMillis());
			endTime_
					.setProgress((int) (calendar_.getTimeInMillis() - smallest));
			endTime_.invalidate();
			endText_.setText(getTime(calendar_));
			endText_.invalidate();
		}
	}

	private String getTime(Calendar c) {
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		//int year = c.get(Calendar.YEAR);
		//int hour = c.get(Calendar.HOUR_OF_DAY);
		//int minute = c.get(Calendar.MINUTE);

		StringBuilder s = new StringBuilder();
		s.append(getMonth(month)).append(" ").append(day); //.append("/").append(year);

		//s.append(" ");
		//s.append(hour).append(": ").append(minute);

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
