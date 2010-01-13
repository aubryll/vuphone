/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import edu.vanderbilt.vuphone.android.events.R;
import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;

/**
 * @author Hamilton Turner
 * 
 */
public class EventDetailsActivity extends Activity {
	
	public static final String EVENT_ID = "sexy_event_here";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_details);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		Long eventId = getIntent().getExtras().getLong(EVENT_ID);
		DBAdapter adapter = new DBAdapter(this);
		adapter.openReadable();
		Cursor c = adapter.getSingleRowCursor(eventId);
		if (c == null)
			return;
		String name = c.getString(c.getColumnIndex(DBAdapter.COLUMN_NAME));
		String desc = c.getString(c.getColumnIndex(DBAdapter.COLUMN_DESCRIPTION));
		Long start = c.getLong(c.getColumnIndex(DBAdapter.COLUMN_START_TIME)) * 1000;
		Long end = c.getLong(c.getColumnIndex(DBAdapter.COLUMN_END_TIME)) * 1000;
		c.close();
		adapter.close();
			
		TextView nameView = (TextView) findViewById(R.event_details.name);
		TextView timeView = (TextView) findViewById(R.event_details.time);
		TextView descView = (TextView) findViewById(R.event_details.description);
		
		nameView.setText(name);
		descView.setText(desc);
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(start);
		String startStr = getTime(cal);
		cal.setTimeInMillis(end);
		String endStr = getTime(cal);
		
		timeView.setText(startStr + " - " + endStr);
	}
	
	private String getTime(Calendar c) {
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		StringBuilder s = new StringBuilder();
		s.append(getMonth(month)).append(" ").append(day); // .append("/").append(year);

		s.append(" ");
		s.append(hour).append(": ").append(minute);

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
