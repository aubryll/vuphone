/**
 * 
 */
package org.vuphone.vandyupon.android.filters;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.maps.GeoPoint;

/**
 * @author Hamilton Turner
 * 
 */
public class PositionActivity extends Activity {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "PositionActivity: ";

	/** Used to pass extras into this activity */
	public static final String EXTRA_LOCATION_IS_CURRENT = "current";
	public static final String EXTRA_LOCATION_NAME = "eln";
	public static final String EXTRA_LOCATION_LAT = "ella";
	public static final String EXTRA_LOCATION_LON = "ello";
	public static final String EXTRA_RADIUS = "rad";

	/** Used to declare what result this activity gives */
	public static final int RESULT_CANCEL = 0;
	public static final int RESULT_CLEAR = 1;
	public static final int RESULT_UPDATE = 2;

	/** Handles to the UI elements */
	Spinner locationSpinner_;
	Button radiusButton_;
	Button cancelButton_;
	Button setFilterButton_;
	Button clearButton_;

	/** Handles clicks from the center button */
	private OnItemSelectedListener centerListener_ = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			Log.i(tag, pre + "item " + position + " was selected");
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			Log.w(tag, pre
					+ "onNothingSelected was called, but should not have been");
		}
	};

	/** Handles clicks from the radius button */
	private OnClickListener radiusListener_ = new OnClickListener() {
		public void onClick(View v) {

		}
	};

	/** Handles clicks from the set filter button */
	private OnClickListener updateListener_ = new OnClickListener() {
		public void onClick(View v) {
			Intent data = new Intent();
			if (locationName_.equals("My Current Location"))
				data.putExtra(EXTRA_LOCATION_IS_CURRENT, true);
			else
				data.putExtra(EXTRA_LOCATION_IS_CURRENT, false);

			data.putExtra(EXTRA_LOCATION_NAME, locationName_);
			data.putExtra(EXTRA_LOCATION_LAT, location_.getLatitudeE6());
			data.putExtra(EXTRA_LOCATION_LON, location_.getLongitudeE6());
			data.putExtra(EXTRA_RADIUS, radius_);

			Toast.makeText(PositionActivity.this,
					"Set position to '" + locationName_ + "'",
					Toast.LENGTH_SHORT).show();
			setResult(RESULT_UPDATE, data);
			finish();
		}
	};

	/** Handles clicks from the cancel button */
	private OnClickListener cancelListener_ = new OnClickListener() {
		public void onClick(View v) {
			setResult(RESULT_CANCEL);
			finish();
		}
	};

	/** Handles clicks from the clear button */
	private OnClickListener clearListener_ = new OnClickListener() {
		public void onClick(View v) {
			Toast.makeText(PositionActivity.this, "Cleared position filter",
					Toast.LENGTH_SHORT).show();
			setResult(RESULT_CLEAR);
			finish();
		}
	};

	/** Store the data this activity is interested in */
	private GeoPoint location_;
	private String locationName_;
	private int radius_;

	/** Called when the activity is first created */
	@Override
	public void onCreate(Bundle saved) {
		super.onCreate(saved);

		setTitle("Position Filter");

		// Have the system blur any windows behind this one.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

		setContentView(R.layout.position_filter);

		// If anything happens to this activity, we should cancel
		setResult(RESULT_CANCEL);

		// Set up some defaults that make sense
		locationName_ = "My Current Position";
		location_ = Constants.vandyCenter;
		radius_ = 10000;

		// Get handles to all UI elements
		locationSpinner_ = (Spinner) findViewById(R.id.SPIN_position_filter);
		radiusButton_ = (Button) findViewById(R.id.BT_radius);
		cancelButton_ = (Button) findViewById(R.id.BT_position_filter_cancel);
		setFilterButton_ = (Button) findViewById(R.id.BT_position_filter_ok);
		clearButton_ = (Button) findViewById(R.id.BT_position_filter_clear);

		// Assign click listeners to all UI elements
		locationSpinner_.setOnItemSelectedListener(centerListener_);
		radiusButton_.setOnClickListener(radiusListener_);
		cancelButton_.setOnClickListener(cancelListener_);
		setFilterButton_.setOnClickListener(updateListener_);
		clearButton_.setOnClickListener(clearListener_);
	}

	/** Called when the activity is about to become visible */
	@Override
	public void onStart() {
		super.onStart();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.getBoolean(EXTRA_LOCATION_IS_CURRENT))
				locationName_ = "My Current Position";
			else
				locationName_ = extras.getString(EXTRA_LOCATION_NAME);
			int lat = extras.getInt(EXTRA_LOCATION_LAT);
			int lon = extras.getInt(EXTRA_LOCATION_LON);
			location_ = new GeoPoint(lat, lon);
			radius_ = extras.getInt(EXTRA_RADIUS);
		}
	}
}
