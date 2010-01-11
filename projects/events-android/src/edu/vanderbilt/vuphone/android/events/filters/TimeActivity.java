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
	private long smallestTime;
	
	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			calendar_.setTimeInMillis(progress + smallestTime);
			
			
			if (seekBar.equals(startTime_))
			{
				startText_.setText(getTime(calendar_));
				startText_.postInvalidate();
				// todo - update the timefilters to the new filter with the new dates
				// 			then make the new filter actually re-query the database
				//FilterManager.updateFilter(new TimeFilter())
			}
			else 
			{
				endText_.setText(getTime(calendar_));
				endText_.postInvalidate();
			}
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
		
		startTime_.setMax((int) (largest-smallest));
		endTime_.setMax((int) (largest-smallest));
	}
	
	private String getTime(Calendar c)
	{
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		StringBuilder s = new StringBuilder();
		s.append(month).append("/").append(day).append("/").append(year);
		
		s.append(" ");
		s.append(hour).append(": ").append(minute);
		
		return s.toString();
	}
	
	
	

}
