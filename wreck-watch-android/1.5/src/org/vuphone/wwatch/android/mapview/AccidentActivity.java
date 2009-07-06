/**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.wwatch.android.mapview;

import java.io.IOException;

import org.vuphone.wwatch.android.R;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.services.MediaUploadService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;

// For testing only
// private static final String XML =
// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Points><Point><Latitude>37.413532</Latitude>"
// +
// "<Longitude>-122.072855</Longitude></Point><Point><Latitude>37.421975</Latitude><Longitude>-122.084054</Longitude></Point></Points>";


public class AccidentActivity extends MapActivity implements LocationListener {
	private static final String tag = VUphone.tag;
	private static final String pre = "AccidentActivity: ";

	private MapController controller_;
	private AccidentMapView mapView_;
	
	private boolean lookingForWreckId_;
	private int wreckId_;

	// Used for centering on the first fix.
	private Location firstLoc_ = null;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void startUploadProcess(int id) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, id);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			Uri photoUri = intent.getData();
			if (photoUri != null) {
				Log.v(VUphone.tag, "Image chosen: " + photoUri);
				
				Intent service = new Intent(this, MediaUploadService.class);
				service.putExtra("Uri", photoUri.toString());
				service.putExtra("WreckId", requestCode);
				startService(service);
			}
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accidentview);
		
		mapView_ = (AccidentMapView) findViewById(R.id.accidentview);
		controller_ = mapView_.getController();
		controller_.setZoom(8);

		Log.v(tag, pre + "Reached onCreate");
		
		mapView_.postInvalidate();

		// Get fixes as quickly as possible.
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
						this);

		Intent intent = getIntent();
		lookingForWreckId_ = intent.getBooleanExtra("LookingForWreckId", false);
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.v(tag, pre + "Reached onStart");
		mapView_.startCache();
		
		if (firstLoc_ != null)
			return;

		// If we don't have a fix, zoom in on default location.
		(new Thread() {
			public void run() {
				Geocoder coder = new Geocoder(AccidentActivity.this);
				Address address = null;
				SharedPreferences prefs = getSharedPreferences(
						VUphone.PREFERENCES_FILE, Context.MODE_PRIVATE);
				String defaultLoc = prefs.getString(VUphone.LOCATION_TAG,
						"Nashville, TN");
				try {
					address = coder.getFromLocationName(defaultLoc, 1).get(0);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (address == null)
					return;

				final GeoPoint point = new GeoPoint((int) (address
						.getLatitude() * 1000000), (int) (address
						.getLongitude() * 1000000));

				AccidentActivity.this.runOnUiThread(new Thread() {
					public void run() {
						controller_.animateTo(point);
						controller_.setZoom(10);
					}
				});
			}
		}).start();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.v(tag, pre + "Reached onPause");
		mapView_.stopCache();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.v(tag, pre + "Reached onResume");
		mapView_.startCache();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(tag, pre + "Reached onDestroy");
		mapView_.stopCache();
		
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.removeUpdates(this);
	}

	public void onLocationChanged(Location location) {
		if (location == null)
			return;

		firstLoc_ = location;
		
		// Remove location updates
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
		.removeUpdates(this);
		
		
		// Center and zoom in
		GeoPoint center = new GeoPoint(
				(int) (firstLoc_.getLatitude() * 1000000), (int) (firstLoc_
						.getLongitude() * 1000000));
		controller_.animateTo(center, new Runnable() {
			public void run() {
				controller_.setZoom(15);
			}
		});
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public boolean isLookingForWreckId() {
		return lookingForWreckId_;
	}

	public void setLookingForWreckId(boolean lookingForWreckId) {
		this.lookingForWreckId_ = lookingForWreckId;
	}

	public int getWreckId() {
		return wreckId_;
	}

	public void setWreckId(int wreckId) {
		this.wreckId_ = wreckId;
		
		// Once someone has chosen a wreck, there is no reason for
		// this instance of the activity to continue.  Therefore,
		// we will finish here.
		setResult(wreckId_);
		finish();
	}

}
