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

    float[] R = new float[16];
    float[] outR = new float[16];
    float[] I = new float[16];
    float[] values = new float[3];
    
    float[] accelVals;
    float[] magVals;

    Sensor accel, mag;
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		SensorManager man = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		accel = man.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mag = man.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		man.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
		man.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.equals(accel)) {
			accelVals = event.values;
		} else if (event.sensor.equals(mag)) {
			magVals = event.values;
		}
		
		if (accelVals == null || magVals == null)
			return;
		
		SensorManager.getRotationMatrix(R, I, accelVals, magVals);

        SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
        SensorManager.getOrientation(outR, values);
        
        float angle = values[0] * 180f / (float) Math.PI;
        Log.v("AndroidTests", "Orientation: " + angle);

	}	
}
