package org.vuphone.vandyupon.android;

import java.io.IOException;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Allows a user to submit a new event to the main server
 * 
 * @author Hamilton Turner
 * 
 */
public class SubmitEvent extends Activity {
	protected static final int RESULT_OK = 0;
	protected static final int RESULT_UNKNOWN = 1;
	protected static final int RESULT_CANCELED = 2;
	protected static final String RESULT = "r";

	private static final int DIALOG_DATE_PICKER = 0;
	private static final int DIALOG_TIME_PICKER = 1;

	private TextView dateLabel_;
	private TextView timeLabel_;
	private TextView buildingLabel_;

	private int year_;
	private int month_;
	private int day_;

	private int hour_;
	private int minute_;

	/** Updates the text when the DatePicker dialog is set */
	private DatePickerDialog.OnDateSetListener dateSetListener_ = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			year_ = year;
			month_ = monthOfYear;
			day_ = dayOfMonth;
			updateDateLabel();
			dateLabel_.requestFocus();
		}
	};

	/** Updates the text when the TimePicker dialog is set */
	private TimePickerDialog.OnTimeSetListener timeSetListener_ = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			hour_ = hourOfDay;
			minute_ = minute;
			updateTimeLabel();
			timeLabel_.requestFocus();
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
		if (resultCode == RESULT_OK)
			buildingLabel_.setText(data.getStringExtra(RESULT));
		else if (resultCode == RESULT_CANCELED)
			;
		else {
			// TODO - make the Other location dialog
			Toast.makeText(
					this,
					"queue Dialog to enter String"
							+ " representing 'Other' location",
					Toast.LENGTH_LONG).show();
			buildingLabel_.setText("Other");
		}

		// TODO - None of these will work right now, because the screen is in
		// touch mode. We don't want the controls to allow focus in touch mode,
		// because then you would have to double click them to activate them -
		// once to focus and once to click. So, we would like to figure out how
		// to change the mode of the screen here and then request focus
		buildingLabel_.requestFocus();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_event);

		dateLabel_ = (TextView) findViewById(R.id.TV_event_date);
		timeLabel_ = (TextView) findViewById(R.id.TV_event_time);
		buildingLabel_ = (TextView) findViewById(R.id.TV_event_building);

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

		final Calendar c = Calendar.getInstance();

		// Set the initial date
		year_ = c.get(Calendar.YEAR);
		month_ = c.get(Calendar.MONTH);
		day_ = c.get(Calendar.DATE);
		hour_ = c.get(Calendar.HOUR_OF_DAY);
		minute_ = c.get(Calendar.MINUTE);
		updateDateLabel();
		updateTimeLabel();

		// Set up the location chooser
		buildingLabel_.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(SubmitEvent.this,
						ChooseLocation.class), RESULT_OK);
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
			return new DatePickerDialog(this, dateSetListener_, year_, month_,
					day_);
		case DIALOG_TIME_PICKER:
			return new TimePickerDialog(this, timeSetListener_, hour_, minute_,
					false);
		default:
			return null;
		}

	}

	/** Handles menu item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Save"))
			Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
		else if (item.getTitle().equals("Clear"))
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
			((DatePickerDialog) dialog).updateDate(year_, month_, day_);
			break;
		case DIALOG_TIME_PICKER:
			((TimePickerDialog) dialog).updateTime(hour_, minute_);
			break;
		}
	}

	/** Uses the current date variables to update the date text */
	private void updateDateLabel() {
		StringBuilder date = new StringBuilder(convertMonth(month_));
		date.append(". ");
		date.append(day_);
		date.append(", ");
		date.append(year_);
		dateLabel_.setText(date.toString());
	}

	/** Uses the current time variables to update the time text */
	private void updateTimeLabel() {
		int civilianHour = hour_;
		String amPm;

		if (civilianHour == 12)
			amPm = "PM";
		else if (civilianHour > 12) {
			civilianHour -= 12;
			amPm = "PM";
		} else {
			amPm = "AM";
		}

		// Correct for 0th hour
		if (civilianHour == 0)
			civilianHour = 12;

		StringBuilder time = new StringBuilder("" + civilianHour);
		time.append(":");
		if (minute_ < 10)
			time.append("0");
		time.append(minute_);
		time.append(" ");
		time.append(amPm);
		timeLabel_.setText(time.toString());
	}
}