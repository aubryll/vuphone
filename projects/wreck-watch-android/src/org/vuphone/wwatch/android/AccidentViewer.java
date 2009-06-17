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
import java.util.ArrayList;
import java.util.Iterator;
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
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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

	private ArrayList<Route> routes_;

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
		mc_.setZoom(8);
		map_.postInvalidate();

		// Get fixes as quickly as possible.
		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
						this);

		// TODO - we really need to cache this somehow, and use a cron-esq thing
		// to get it
		t.scheduleAtFixedRate(task_, 0, 9000);

		curCenter_ = map_.getMapCenter();

	}

	@Override
	public void onStart() {
		super.onStart();

		if (firstLoc_ != null)
			return;

		// If we don't have a fix, zoom in on default location.
		(new Thread() {
			public void run() {
				Geocoder coder = new Geocoder(AccidentViewer.this);
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

				AccidentViewer.this.runOnUiThread(new Thread() {
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
		t.cancel();
		task_.cancel();
	}

	private void getAccidentXML(GeoPoint bl, GeoPoint br, GeoPoint tl,
			GeoPoint tr) {
		HTTPGetter.doAccidentGet(bl, br, tl, tr, this);
	}

	public void operationComplete(HttpResponse resp) {

		Log.i(VUphone.tag, LOG_PREFIX
				+ "HTTP operation complete.  Processing response.");
		AccidentDataHandler adh = new AccidentDataHandler();
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		try {
			resp.getEntity().writeTo(bao);
			Log.d(VUphone.tag, LOG_PREFIX + "Http response: " + bao.toString());
			
			routes_ = adh.processXML(new InputSource(new ByteArrayInputStream(
					bao.toByteArray())));
			Iterator<Route> i = routes_.iterator();
			ArrayList<Waypoint> points = new ArrayList<Waypoint>();
			while (i.hasNext())
				points.add(i.next().getEndPoint());
			
			Log.i(VUphone.tag, LOG_PREFIX + "Adding waypoints: " + points.toString());
			map_.addPins(points);
			
		} catch (IOException e) {
			Log.e(VUphone.tag, LOG_PREFIX
					+ "IOException processing HttpResponse object: "
					+ e.getMessage());
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
