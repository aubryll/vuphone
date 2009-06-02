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

import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.http.HTTPGetter;
import org.vuphone.wwatch.android.http.HttpOperationListener;
import org.vuphone.wwatch.android.mapping.AccidentDataHandler;
import org.vuphone.wwatch.android.mapping.AccidentMapView;
import org.vuphone.wwatch.android.mapping.EnhancedGeoPoint;
import org.xml.sax.InputSource;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;

public class AccidentViewer extends MapActivity implements HttpOperationListener, LocationListener {

	private static final String LOG_LABEL = "VUPHONE";
	private static final String LOG_PREFIX = "AccidentViewer: ";

	private MapController mc_;

	private AccidentMapView map_;
	private MyLocationOverlay mlo_ ;

	private GeoPoint curCenter_;

	private Timer t = new Timer("Accident View Delay");
	private boolean update_ = true;

	//For testing only
	private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Points><Point><Latitude>37.413532</Latitude>" +
	"<Longitude>-122.072855</Longitude></Point><Point><Latitude>37.421975</Latitude><Longitude>-122.084054</Longitude></Point></Points>";



	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}


	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accidentview);
		map_ = (AccidentMapView)findViewById(R.id.accidentview);
		mlo_ = new MyLocationOverlay(this, map_);
		mlo_.enableMyLocation();
		mc_ = map_.getController();
		mc_.setZoom(7);
		map_.postInvalidate();

		((LocationManager) getSystemService(Context.LOCATION_SERVICE))
		.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

		t.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				update_ = true;

				if (map_.getZoomLevel() < 8){
					if ((map_.getMapCenter().getLatitudeE6() != curCenter_.getLatitudeE6()) || 
							(map_.getMapCenter().getLongitudeE6() != curCenter_.getLongitudeE6())){
						curCenter_ = map_.getMapCenter();
						DisplayMetrics dm = new DisplayMetrics();
						int snHeight = dm.heightPixels;
						int snWidth = dm.widthPixels;
						Projection p = map_.getProjection();
						GeoPoint upperRight = p.fromPixels(snWidth, snHeight);
						GeoPoint upperLeft = p.fromPixels(0, snHeight);
						GeoPoint lowerLeft = p.fromPixels(0, 0);
						GeoPoint lowerRight = p.fromPixels(snWidth, 0);

						getAccidentXML(lowerLeft, lowerRight, upperLeft, upperRight);
					}
				}

			}

		}, 0, 3000);

		if (mlo_.getMyLocation() != null){
			mc_.setCenter(mlo_.getMyLocation());

		}
		curCenter_ = map_.getMapCenter();

	}

	@Override
	public void onPause(){
		super.onPause();
		mlo_.disableMyLocation();
	}

	@Override
	public void onResume(){
		super.onResume();
		mlo_.enableMyLocation();
	}

	private void getAccidentXML(GeoPoint bl, GeoPoint br, GeoPoint tl, GeoPoint tr){
		HTTPGetter.doAccidentGet(bl, br, tl, tr, this);
	}


	public void operationComplete(HttpResponse resp) {

		Log.i(LOG_LABEL, LOG_PREFIX + "HTTP operation complete.  Processing response.");
		AccidentDataHandler adh = new AccidentDataHandler();
		for (EnhancedGeoPoint p:adh.processXML(new InputSource(new ByteArrayInputStream(XML.getBytes())))){
			Log.d(LOG_LABEL, LOG_PREFIX + "Adding accident point: " + p.toString());
			map_.addPin(p);
		}
	}


	public void onLocationChanged(Location location) {
		if (map_.getZoomLevel() < 8){
			DisplayMetrics dm = new DisplayMetrics();
			int snHeight = dm.heightPixels;
			int snWidth = dm.widthPixels;
			Projection p = map_.getProjection();
			GeoPoint upperRight = p.fromPixels(snWidth, snHeight);
			GeoPoint upperLeft = p.fromPixels(0, snHeight);
			GeoPoint lowerLeft = p.fromPixels(0, 0);
			GeoPoint lowerRight = p.fromPixels(snWidth, 0);
			if (update_){
				getAccidentXML(lowerLeft, lowerRight, upperLeft, upperRight);
			}
		}
	}

	public void onProviderDisabled(String provider) {
	}


	public void onProviderEnabled(String provider) {
	}


	public void onStatusChanged(String provider, int status, Bundle extras) {
	}




}
