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
			int month = calendar_.get(Calendar.MONTH);
			int day = calendar_.get(Calendar.DAY_OF_MONTH);
			
			if (seekBar.equals(startTime_))
			{
				startText_.setText("" + month + day);
				startText_.postInvalidate();
				// todo - update the timefilters to the new filter with the new dates
				// 			then make the new filter actually re-query the database
				//FilterManager.updateFilter(new TimeFilter())
			}
			else 
			{
				endText_.setText("" + month + day);
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
		long largest = adapter.getLargestTime();
		long smallest = adapter.getSmallestTime();
		smallestTime = smallest;
		adapter.close();
		
		startTime_.setMax((int) (largest-smallest));
		endTime_.setMax((int) (largest-smallest));
	}
	
	
	
	

}
