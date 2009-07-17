package org.vuphone.vandyupon.android.submitevent;

import java.io.IOException;
import java.util.GregorianCalendar;

import org.vuphone.vandyupon.android.LocationManager;
import org.vuphone.vandyupon.android.R;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

/**
 * Allows a user to submit a new event to the main server
 * 
 * @author Hamilton Turner
 * 
 */
public class SubmitEvent extends Activity {
	/** Used to indicate the status of returned activities */
	protected static final int RESULT_OK = 0;
	protected static final int RESULT_UNKNOWN = 1;
	protected static final int RESULT_CANCELED = 2;

	/** Used to access data returned by other activities */
	protected static final String RESULT_NAME = "r";
	protected static final String RESULT_LAT = "lat";
	protected static final String RESULT_LNG = "lng";

	/** Used to indicate to ourselves which activities we requested */
	private static final int REQUEST_LIST_LOCATION = 0;
	private static final int REQUEST_MAP_LOCATION = 1;

	/** Used to indicate which dialog should be started */
	private static final int DIALOG_DATE_PICKER = 0;
	private static final int DIALOG_TIME_PICKER = 1;
	private static final int DIALOG_END_DATE_PICKER = 2;
	private static final int DIALOG_END_TIME_PICKER = 3;

	/** Used to access all of the TextViews */
	private TextView dateLabel_;
	private TextView timeLabel_;
	private TextView dateEndLabel_;
	private TextView timeEndLabel_;
	private TextView buildingLabel_;

	/** Used to keep track of the start and end times */
	private GregorianCalendar startCalendar_;
	private GregorianCalendar endCalendar_;

	/**
	 * Keeps track of the lat and lng we will send to the server. Default to FGH
	 */
	private GeoPoint location_ = LocationManager.coordinates
			.get("Featheringill");

	/** Updates the text when the DatePicker dialog is set */
	private DatePickerDialog.OnDateSetListener dateSetListener_ = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			final int startHour = startCalendar_
					.get(GregorianCalendar.HOUR_OF_DAY);
			final int startMin = startCalendar_.get(GregorianCalendar.MINUTE);
			GregorianCalendar newCalendar = new GregorianCalendar(year,
					monthOfYear, dayOfMonth, startHour, startMin);

			// If the newer date is greater than the current end date, then bump
			// up the current end date by the original time span
			if (newCalendar.getTimeInMillis() > endCalendar_.getTimeInMillis())
				endCalendar_.setTimeInMillis(newCalendar.getTimeInMillis()
						+ (endCalendar_.getTimeInMillis() - startCalendar_
								.getTimeInMillis()));

			startCalendar_ = newCalendar;

			if (endCalendar_.getTimeInMillis()
					- startCalendar_.getTimeInMillis() < 60 * 1000)
				endCalendar_.add(GregorianCalendar.MINUTE, 5);

			updateDateLabels();
			dateLabel_.requestFocusFromTouch();
		}
	};

	/** Updates the text when the DatePicker dialog is set */
	private DatePickerDialog.OnDateSetListener endDateSetListener_ = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			final int hour = endCalendar_.get(GregorianCalendar.HOUR_OF_DAY);
			final int minute = endCalendar_.get(GregorianCalendar.MINUTE);
			GregorianCalendar newCalendar = new GregorianCalendar(year,
					monthOfYear, dayOfMonth, hour, minute);

			if (newCalendar.getTimeInMillis() < startCalendar_
					.getTimeInMillis())
				return;

			endCalendar_ = newCalendar;

			if (endCalendar_.getTimeInMillis()
					- startCalendar_.getTimeInMillis() < 60 * 1000)
				endCalendar_.add(GregorianCalendar.MINUTE, 5);

			updateDateLabels();
			dateEndLabel_.requestFocusFromTouch();
		}
	};

	/** Updates the text when the TimePicker dialog is set */
	private TimePickerDialog.OnTimeSetListener timeSetListener_ = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			final int year = startCalendar_.get(GregorianCalendar.YEAR);
			final int month = startCalendar_.get(GregorianCalendar.MONTH);
			final int day = startCalendar_.get(GregorianCalendar.DAY_OF_MONTH);

			GregorianCalendar newCalendar = new GregorianCalendar(year, month,
					day, hourOfDay, minute);

			// If the newer date is greater than the current end date, then bump
			// up the current end date by the original time span
			if (newCalendar.getTimeInMillis() > endCalendar_.getTimeInMillis())
				endCalendar_.setTimeInMillis(newCalendar.getTimeInMillis()
						+ (endCalendar_.getTimeInMillis() - startCalendar_
								.getTimeInMillis()));

			startCalendar_ = newCalendar;

			if (endCalendar_.getTimeInMillis()
					- startCalendar_.getTimeInMillis() < 60 * 1000)
				endCalendar_.add(GregorianCalendar.MINUTE, 5);

			updateTimeLabels();
			timeLabel_.requestFocusFromTouch();
		}
	};

	/** Updates the text when the TimePicker dialog is set */
	private TimePickerDialog.OnTimeSetListener endTimeSetListener_ = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			final int year = endCalendar_.get(GregorianCalendar.YEAR);
			final int month = endCalendar_.get(GregorianCalendar.MONTH);
			final int day = endCalendar_.get(GregorianCalendar.DAY_OF_MONTH);

			GregorianCalendar newCalendar = new GregorianCalendar(year, month,
					day, hourOfDay, minute);

			if (newCalendar.getTimeInMillis() < startCalendar_
					.getTimeInMillis())
				return;

			endCalendar_ = newCalendar;

			if (endCalendar_.getTimeInMillis()
					- startCalendar_.getTimeInMillis() < 60 * 1000)
				endCalendar_.add(GregorianCalendar.MINUTE, 5);

			updateTimeLabels();
			timeEndLabel_.requestFocusFromTouch();
		}
	};

	/** Clears the EditText fields. Used in the Menu */
	private void clear() {
		EditText et = (EditText) findViewById(R.id.ET_event_title);
		et.setText("");
		et = (EditText) findViewById(R.id.ET_event_desc);
		et.setText("");
	}

	/** Helper function to turn the Month from an integer into a String */
	private String convertMonth(int month) {
		switch (month) {
		case 0:
			return "Jan";
		case 1:
			return "Feb";
		case 2:
			return "Mar";
		case 3:
			return "Apr";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "Aug";
		case 8:
			return "Sept";
		case 9:
			return "Oct";
		case 10:
			return "Nov";
		case 11:
			return "Dec";
		default:
			return "Unknown";
		}
	}

	/**
	 * This method is called when the sending activity has finished, with the
	 * result it supplied.
	 * 
	 * @param requestCode
	 *            The original request code as given to startActivity().
	 * @param resultCode
	 *            From sending activity as per setResult().
	 * @param data
	 *            From sending activity as per setResult().
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED)
			return;

		if (requestCode == REQUEST_LIST_LOCATION && resultCode == RESULT_OK) {
			buildingLabel_.setText(data.getStringExtra(RESULT_NAME));
			location_ = LocationManager.coordinates.get(data
					.getStringExtra(RESULT_NAME));
		} else if (requestCode == REQUEST_LIST_LOCATION
				&& resultCode == RESULT_UNKNOWN) {
			startActivityForResult(new Intent(this, LocationChooser.class),
					REQUEST_MAP_LOCATION);
		} else if (requestCode == REQUEST_MAP_LOCATION) {
			int lat = data.getIntExtra(RESULT_LAT, LocationManager.vandyCenter_
					.getLatitudeE6());
			int lng = data.getIntExtra(RESULT_LNG, LocationManager.vandyCenter_
					.getLongitudeE6());

			location_ = new GeoPoint(lat, lng);
			buildingLabel_.setText("Other");
		}

		buildingLabel_.requestFocusFromTouch();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_event);

		dateLabel_ = (TextView) findViewById(R.id.TV_event_date);
		timeLabel_ = (TextView) findViewById(R.id.TV_event_time);
		buildingLabel_ = (TextView) findViewById(R.id.TV_event_building);

		dateEndLabel_ = (TextView) findViewById(R.id.TV_event_date_end);
		timeEndLabel_ = (TextView) findViewById(R.id.TV_event_time_end);

		// Create the onClickListener for the date
		dateLabel_.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_DATE_PICKER);
			}
		});

		// Create the onClickListener for the date
		timeLabel_.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_TIME_PICKER);
			}
		});

		// Create the onClickListener for the date
		dateEndLabel_.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_END_DATE_PICKER);
			}
		});

		// Create the onClickListener for the date
		timeEndLabel_.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_END_TIME_PICKER);
			}
		});

		// Set the initial date
		startCalendar_ = new GregorianCalendar();
		endCalendar_ = new GregorianCalendar();
		endCalendar_.add(GregorianCalendar.HOUR, 2);

		while ((startCalendar_.get(GregorianCalendar.MINUTE) % 15) != 0)
			startCalendar_.add(GregorianCalendar.MINUTE, 1);

		while ((endCalendar_.get(GregorianCalendar.MINUTE) % 15) != 0)
			endCalendar_.add(GregorianCalendar.MINUTE, 1);

		updateDateLabels();
		updateTimeLabels();

		// Set up the location chooser
		buildingLabel_.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(SubmitEvent.this,
						ChooseLocation.class), REQUEST_LIST_LOCATION);
			}
		});

		ColorStateList csl = null;
		XmlResourceParser parser = getResources().getXml(
				R.color.focused_textview);
		try {
			csl = ColorStateList.createFromXml(getResources(), parser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (csl == null)
			return;

		dateLabel_.setTextColor(csl);
		timeLabel_.setTextColor(csl);
		buildingLabel_.setTextColor(csl);

		dateEndLabel_.setTextColor(csl);
		timeEndLabel_.setTextColor(csl);
	}

	/** Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Save");
		menu.add("Clear");
		return true;
	}

	/** Called when a dialog is first created */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DATE_PICKER:
			final int year = startCalendar_.get(GregorianCalendar.YEAR);
			final int month = startCalendar_.get(GregorianCalendar.MONTH);
			final int day = startCalendar_.get(GregorianCalendar.DAY_OF_MONTH);

			return new DatePickerDialog(this, dateSetListener_, year, month,
					day);
		case DIALOG_TIME_PICKER:
			final int hour = startCalendar_.get(GregorianCalendar.HOUR_OF_DAY);
			final int minute = startCalendar_.get(GregorianCalendar.MINUTE);

			return new TimePickerDialog(this, timeSetListener_, hour, minute,
					false);
		case DIALOG_END_DATE_PICKER:
			final int eYear = endCalendar_.get(GregorianCalendar.YEAR);
			final int eMonth = endCalendar_.get(GregorianCalendar.MONTH);
			final int eDay = endCalendar_.get(GregorianCalendar.DAY_OF_MONTH);

			return new DatePickerDialog(this, endDateSetListener_, eYear,
					eMonth, eDay);
		case DIALOG_END_TIME_PICKER:
			final int eHour = endCalendar_.get(GregorianCalendar.HOUR_OF_DAY);
			final int eMinute = endCalendar_.get(GregorianCalendar.MINUTE);

			return new TimePickerDialog(this, endTimeSetListener_, eHour,
					eMinute, false);
		default:
			return null;
		}

	}

	/** Handles menu item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Save")) {
			Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
			Log.v("tag", "" + location_.getLatitudeE6() + ", "
					+ location_.getLongitudeE6());
		} else if (item.getTitle().equals("Clear"))
			clear();
		else
			return false;
		return true;
	}

	/** Called when a (created) dialog is about to be shown */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_DATE_PICKER:
			final int year = startCalendar_.get(GregorianCalendar.YEAR);
			final int month = startCalendar_.get(GregorianCalendar.MONTH);
			final int day = startCalendar_.get(GregorianCalendar.DAY_OF_MONTH);

			((DatePickerDialog) dialog).updateDate(year, month, day);
			break;
		case DIALOG_TIME_PICKER:
			final int hour = startCalendar_.get(GregorianCalendar.HOUR_OF_DAY);
			final int minute = startCalendar_.get(GregorianCalendar.MINUTE);

			((TimePickerDialog) dialog).updateTime(hour, minute);
			break;
		case DIALOG_END_DATE_PICKER:
			final int eYear = endCalendar_.get(GregorianCalendar.YEAR);
			final int eMonth = endCalendar_.get(GregorianCalendar.MONTH);
			final int eDay = endCalendar_.get(GregorianCalendar.DAY_OF_MONTH);

			((DatePickerDialog) dialog).updateDate(eYear, eMonth, eDay);
			break;
		case DIALOG_END_TIME_PICKER:
			final int eHour = endCalendar_.get(GregorianCalendar.HOUR_OF_DAY);
			final int eMinute = endCalendar_.get(GregorianCalendar.MINUTE);

			((TimePickerDialog) dialog).updateTime(eHour, eMinute);
			break;
		}
	}

	/** Uses the current date variables to update the date text */
	private void updateDateLabels() {
		final int year = startCalendar_.get(GregorianCalendar.YEAR);
		final int month = startCalendar_.get(GregorianCalendar.MONTH);
		final int day = startCalendar_.get(GregorianCalendar.DAY_OF_MONTH);

		StringBuilder date = new StringBuilder(convertMonth(month));
		date.append(". ");
		date.append(day);
		date.append(", ");
		date.append(year);
		dateLabel_.setText(date.toString());

		// Repeat process for end date
		final int eYear = endCalendar_.get(GregorianCalendar.YEAR);
		final int eMonth = endCalendar_.get(GregorianCalendar.MONTH);
		final int eDay = endCalendar_.get(GregorianCalendar.DAY_OF_MONTH);

		date = new StringBuilder(convertMonth(eMonth));
		date.append(". ");
		date.append(eDay);
		date.append(", ");
		date.append(eYear);
		dateEndLabel_.setText(date.toString());
	}

	/** Uses the current time variables to update the time text */
	private void updateTimeLabels() {
		final int hour = startCalendar_.get(GregorianCalendar.HOUR);
		final int minute = startCalendar_.get(GregorianCalendar.MINUTE);
		final int amPm = startCalendar_.get(GregorianCalendar.AM_PM);

		StringBuilder time = new StringBuilder();
		if (hour == 0)
			time.append("12");
		else 
			time.append(hour);
		time.append(":");
		if (minute < 10)
			time.append("0");
		time.append(minute);
		if (amPm == GregorianCalendar.AM)
			time.append(" AM");
		else
			time.append(" PM");
		timeLabel_.setText(time.toString());

		// Repeat the entire process for the end time
		final int eHour = endCalendar_.get(GregorianCalendar.HOUR);
		final int eMinute = endCalendar_.get(GregorianCalendar.MINUTE);
		final int eamPm = startCalendar_.get(GregorianCalendar.AM_PM);

		// Correct for 0th hour
		time = new StringBuilder();
		if (eHour == 0)
			time.append("12");
		else
			time.append(eHour);
		time.append(":");
		if (eMinute < 10)
			time.append("0");
		time.append(eMinute);
		if (eamPm == GregorianCalendar.AM)
			time.append(" AM");
		else
			time.append(" PM");
		timeEndLabel_.setText(time.toString());
	}
}