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
package org.vuphone.wwatch.android;

// TODO - Work on animated zooming

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.http.HTTPGetter;
import org.vuphone.wwatch.android.http.HttpOperationListener;
import org.vuphone.wwatch.android.mapping.AccidentDataHandler;
import org.vuphone.wwatch.android.mapping.AccidentMapView;
import org.vuphone.wwatch.android.mapping.Route;
import org.xml.sax.InputSource;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Projection;

public class AccidentViewer extends MapActivity implements
		HttpOperationListener, LocationListener {

	private static final String LOG_PREFIX = "AccidentViewer: ";

	private MapController mc_;

	private AccidentMapView map_;

	private GeoPoint curCenter_;

	private Timer t = new Timer("Accident View Delay");

	// Used for centering on the first fix.
	private Location firstLoc_ = null;

	private final TimerTask task_ = new TimerTask() {

		@Override
		public void run() {

			if (map_.getZoomLevel() > 7) {
				if ((map_.getMapCenter().getLatitudeE6() != curCenter_
						.getLatitudeE6())
						|| (map_.getMapCenter().getLongitudeE6() != curCenter_
								.getLongitudeE6())) {
					curCenter_ = map_.getMapCenter();
					Display d = getWindowManager().getDefaultDisplay();
					int snHeight = d.getHeight();
					int snWidth = d.getWidth();
					Projection p = map_.getProjection();
					GeoPoint upperRight = p.fromPixels(snWidth, snHeight);
					GeoPoint upperLeft = p.fromPixels(0, snHeight);
					GeoPoint lowerLeft = p.fromPixels(0, 0);
					GeoPoint lowerRight = p.fromPixels(snWidth, 0);

					getAccidentXML(lowerLeft, lowerRight, upperLeft, upperRight);
				}
			}

		}

	};

	// For testing only
	// private static final String XML =
	// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Points><Point><Latitude>37.413532</Latitude>"
	// +
	// "<Longitude>-122.072855</Longitude></Point><Point><Latitude>37.421975</Latitude><Longitude>-122.084054</Longitude></Point></Points>";

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accidentview);
		map_ = (AccidentMapView) findViewById(R.id.accidentview);
		mc_ = map_.getController();
		mc_.setZoom(7);
		map_.postInvalidate();

		// Get fixes as quickly as possible. This will be turned off after the
		// first fix comes in.
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
						this);

		t.scheduleAtFixedRate(task_, 0, 3000);

		curCenter_ = map_.getMapCenter();

	}

	@Override
	public void onPause() {
		super.onPause();
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.removeUpdates(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Only enable GPS if we still don't have a fix
		if (firstLoc_ == null)
			((LocationManager) getSystemService(Context.LOCATION_SERVICE))
					.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
							this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.removeUpdates(this);
		t.cancel();
		task_.cancel();
	}

	private void getAccidentXML(GeoPoint bl, GeoPoint br, GeoPoint tl,
			GeoPoint tr) {
		HTTPGetter.doAccidentGet(bl, br, tl, tr, this);
	}

	public void operationComplete(HttpResponse resp) {

		Log.i(VUphone.tag, LOG_PREFIX + "HTTP operation complete.  Processing response.");
		AccidentDataHandler adh = new AccidentDataHandler();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		try {
			resp.getEntity().writeTo(bao);	
			Log.d(VUphone.tag, LOG_PREFIX + "Http response: " + bao.toString());
			for (Route r:adh.processXML(new InputSource(new ByteArrayInputStream(bao.toByteArray())))){
				for (Waypoint w:r.getRoute()){
					Log.d(VUphone.tag, LOG_PREFIX + "Adding accident point: " + w.toString());
					map_.addPin(w);
				}
				
			}
		} catch (IOException e) {
			Log.e(VUphone.tag, LOG_PREFIX + "IOException processing HttpResponse object: " + e.getMessage());
		}
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
				mc_.setZoom(16);
			}
		});
		((LocationManager) getSystemService(Context.LOCATION_SERVICE)).removeUpdates(this);
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
