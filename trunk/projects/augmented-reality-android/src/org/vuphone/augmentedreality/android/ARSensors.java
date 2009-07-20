/*******************************************************************************
 * Copyright 2009 Krzysztof Zienkiewicz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package org.vuphone.augmentedreality.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class ARSensors implements LocationListener, SensorEventListener {

	private static ARSensors instance_ = null;

	private static final long GPS_TIME = 0;
	private static final float GPS_DISTANCE = 0f;
	private static final int ORIENTATION_DELAY = SensorManager.SENSOR_DELAY_FASTEST;

	private LocationManager lMan_;
	private SensorManager sMan_;

	private Location location_ = null;
	private float[] orientation_ = null;

	public static ARSensors getInstance(Context context) {
		if (instance_ == null)
			instance_ = new ARSensors(context);
		return instance_;
	}

	private ARSensors(Context context) {
		lMan_ = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		lMan_.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME,
				GPS_DISTANCE, this);

		sMan_ = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor orientation = sMan_.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		sMan_.registerListener(this, orientation, ORIENTATION_DELAY);
	}

	public void finish() {
		lMan_.removeUpdates(this);
		sMan_.unregisterListener(this);
		instance_ = null;
	}

	public Location getLocation() {
		return location_;
	}

	public float[] getOrientation() {
		return orientation_;
	}

	@Override
	public void onLocationChanged(Location location) {
		location_ = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.accuracy != SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
			Log.v("AndroidTests", "Unreliable sensor change");
			return;
		}
		
		orientation_ = event.values;
		
		//orientation_ = new float[] {0, 0, 0};
	}
}
