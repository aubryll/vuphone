/**
 * 
 */
package org.vuphone.vandyupon.android.filters;

import java.util.HashMap;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.LocationManager;
import org.vuphone.vandyupon.android.R;
import org.vuphone.vandyupon.android.submitevent.ChooseLocation;
import org.vuphone.vandyupon.android.submitevent.LocationChooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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

	/** Used to indicate to ourselves which activities we requested */
	private static final int REQUEST_LIST_LOCATION = 0;
	private static final int REQUEST_MAP_LOCATION = 1;

	/** Used to declare what data this activity returns */
	public static final String RESULT_DATA_NAME = "name";
	public static final String RESULT_DATA_LAT = "lat";
	public static final String RESULT_DATA_LON = "lon";

	/** Handles to the UI elements */
	Spinner locationSpinner_;
	Button radiusButton_;
	Button cancelButton_;
	Button setFilterButton_;
	Button clearButton_;

	/** Used to populate the various radii */
	private static final String[] radiusItemStrings_ = { "50 feet", "100 feet",
			"200 feet", "400 feet", "1600 feet" };

	/** Used to map radius Strings to Integers */
	private static final HashMap<String, Integer> radiusItems_ = new HashMap<String, Integer>();
	static {
		radiusItems_.put("50 feet", 50);
		radiusItems_.put("100 feet", 100);
		radiusItems_.put("200 feet", 200);
		radiusItems_.put("400 feet", 400);
		radiusItems_.put("1600 feet", 1600);
	}

	/** Handles clicks from the center button */
	private OnItemSelectedListener centerListener_ = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// If they selected other, then start activity for an other result
			if (position == 1) {
				Intent i = new Intent(PositionActivity.this,
						ChooseLocation.class);
				startActivityForResult(i, 0);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			Log.w(tag, pre
					+ "onNothingSelected was called, but should not have been");
		}
	};

	/** Handles clicks from the radius button */
	private OnClickListener radiusListener_ = new OnClickListener() {
		public void onClick(View v) {
			showDialog(0);
		}
	};

	/** Handles selections from the radius dialog */
	private DialogInterface.OnClickListener radiusSelectedListener_ = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			radiusButton_.setText(radiusItemStrings_[which]);
			radius_ = radiusItems_.get(radiusItemStrings_[which]);
		}
	};

	/** Handles clicks from the set filter button */
	private OnClickListener updateListener_ = new OnClickListener() {
		public void onClick(View v) {
			Intent data = new Intent();
			if (locationSpinner_.getSelectedItemPosition() == 0)
				data.putExtra(EXTRA_LOCATION_IS_CURRENT, true);
			else
				data.putExtra(EXTRA_LOCATION_IS_CURRENT, false);

			data.putExtra(EXTRA_LOCATION_NAME, locationName_);
			data.putExtra(EXTRA_LOCATION_LAT, location_.getLatitudeE6());
			data.putExtra(EXTRA_LOCATION_LON, location_.getLongitudeE6());
			data.putExtra(EXTRA_RADIUS, radius_);
			
			setResult(Constants.RESULT_UPDATE, data);
			finish();
		}
	};

	/** Handles clicks from the cancel button */
	private OnClickListener cancelListener_ = new OnClickListener() {
		public void onClick(View v) {
			setResult(Constants.RESULT_CANCELED);
			finish();
		}
	};

	/** Handles clicks from the clear button */
	private OnClickListener clearListener_ = new OnClickListener() {
		public void onClick(View v) {
			setResult(Constants.RESULT_CLEAR);
			finish();
		}
	};

	/** Store the data this activity is interested in */
	private GeoPoint location_;
	private String locationName_;
	private int radius_;

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
		if (resultCode == Constants.RESULT_CANCELED) {
			// Set them back to the current location selection
			locationSpinner_.setSelection(0);
			Log.i(tag, pre + "Cancel request");
			return;
		}

		if (requestCode == REQUEST_LIST_LOCATION
				&& resultCode == Constants.RESULT_OK) {
			locationName_ = data.getStringExtra(ChooseLocation.RESULT_NAME);
			location_ = LocationManager.coordinates.get(data
					.getStringExtra(ChooseLocation.RESULT_NAME));
			Log.i(tag, pre + "Location Name is " + locationName_);
			Log.i(tag, pre + "Point is " + location_);
		} else if (requestCode == REQUEST_LIST_LOCATION
				&& resultCode == Constants.RESULT_UNKNOWN) {
			locationName_ = "Custom";
			startActivityForResult(new Intent(this, LocationChooser.class),
					REQUEST_MAP_LOCATION);
		} else if (requestCode == REQUEST_MAP_LOCATION) {
			int lat = data.getIntExtra(LocationChooser.RESULT_LAT,
					Constants.vandyCenter.getLatitudeE6());
			int lng = data.getIntExtra(LocationChooser.RESULT_LNG,
					Constants.vandyCenter.getLongitudeE6());

			location_ = new GeoPoint(lat, lng);
			
			Log.i(tag, pre + "Location Name is " + locationName_);
			Log.i(tag, pre + "Point is " + location_);
		}
	}

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
		setResult(Constants.RESULT_CANCELED);

		// Set up some defaults that make sense
		locationName_ = "My Current Position";
		location_ = Constants.vandyCenter;
		radius_ = 200;

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

	/** Called when a dialog is first created */
	@Override
	protected Dialog onCreateDialog(int id) {
		// Ignore the id because we only have one dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(radiusItemStrings_, radiusSelectedListener_);

		return builder.create();
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
