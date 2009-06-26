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


// TODO - Work on animated zooming

import java.io.IOException;

import org.vuphone.wwatch.android.R;
import org.vuphone.wwatch.android.VUphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;

// For testing only
// private static final String XML =
// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Points><Point><Latitude>37.413532</Latitude>"
// +
// "<Longitude>-122.072855</Longitude></Point><Point><Latitude>37.421975</Latitude><Longitude>-122.084054</Longitude></Point></Points>";


public class AccidentActivity extends MapActivity implements LocationListener {

	private static final String pre = "AccidentActivity: ";

	private MapController mc_;
	
	private AccidentList routes_;

	// Used for centering on the first fix.
	private Location firstLoc_ = null;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accidentview);
		AccidentMapView map_ = (AccidentMapView) findViewById(R.id.accidentview);
		mc_ = map_.getController();
		mc_.setZoom(15);
		map_.postInvalidate();
		
		routes_ = new AccidentList(map_);

		// Get fixes as quickly as possible.
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
						this);

	}

	@Override
	public void onStart() {
		super.onStart();

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
						mc_.animateTo(point);
						mc_.setZoom(10);
					}
				});
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.removeUpdates(this);
		routes_.stopUpdates();
	}

	public void onLocationChanged(Location location) {
		firstLoc_ = location;
		if (firstLoc_ == null)
			return;

		// center and zoom in
		GeoPoint center = new GeoPoint(
				(int) (firstLoc_.getLatitude() * 1000000), (int) (firstLoc_
						.getLongitude() * 1000000));

		mc_.animateTo(center, new Runnable() {
			public void run() {
				mc_.setZoom(15);
			}
		});
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.removeUpdates(this);
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
