package org.vuphone.augmentedreality.android;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ARSensors implements SensorEventListener{
	
	private static final int ORIENTATION_RATE = SensorManager.SENSOR_DELAY_FASTEST;
	
	private static ARSensors instance_ = null;
	
	private SensorManager sMan_;
	private Sensor orientationSensor_;
	private boolean orientationActive_;
	private float[] orientationData_;
	
	private ARSensors(Context c) {
		sMan_ = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		orientationSensor_ = sMan_.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		orientationActive_ = false;
		orientationData_ = null;
	}
	
	public float[] getOrientation() {
		if (!orientationActive_)
			throw new RuntimeException("Orientation sensor not active. Call " +
					"startOrientationSensor()");
		
		return orientationData_;
	}
	
	public static ARSensors getSensors() {
		if (instance_ == null)
			throw new RuntimeException("ARSensors does not exist. Call " +
					"getSensors(Context)");
		
		return instance_;
	}
	
	public static ARSensors getSensors(Context c) {
		if (instance_ == null) {
			instance_ = new ARSensors(c);
		}
		
		return instance_;
	}
	
	public void startOrientationSensor() {
		sMan_.registerListener(this, orientationSensor_, ORIENTATION_RATE);
		orientationActive_ = true;
	}
	
	public void stopOrientationSensor() {
		sMan_.unregisterListener(this);
		orientationActive_ = false;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
				
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			
			orientationData_ = event.values;
		}
	}
}
