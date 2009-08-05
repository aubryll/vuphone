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

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class SensorActivity extends Activity implements SensorEventListener {

    float azimuth;
    long time;
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
    }
    
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		SensorManager man = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		Sensor orientation = man.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		man.registerListener(this, orientation, SensorManager.SENSOR_DELAY_NORMAL);
		
		FIRAngleFilter filter = new FIRAngleFilter(5);
		for (int i = 0; i < 5; i++)
			filter.add(i);
		Log.v("AndroidTests", filter.toString());
		Log.v("AndroidTests", filter.dump());
		
		
		for (int i = 5; i < 7; i++)
			filter.add(i);
		Log.v("AndroidTests", filter.toString());
		Log.v("AndroidTests", filter.dump());		
	}
	//Fastest ~ 20ms
	//Normal ~ 200ms
	//Game ~ 40ms
	//UI ~ 80ms

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.values == null)
			return;
		
		long delta = (event.timestamp - time) / 1000000; // ms
		time = event.timestamp;
		
		azimuth = event.values[0];
        Log.v("AndroidTests", "Orientation: " + azimuth + " TimeDelta: " + delta);
	}	
}
