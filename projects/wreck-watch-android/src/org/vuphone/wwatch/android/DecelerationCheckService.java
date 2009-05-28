package org.vuphone.wwatch.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class DecelerationCheckService extends Service {

	private final SensorManager sensorManager_ = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	private final DecelerationListener listener_ = new DecelerationListener();
	private final static long TIME_BETWEEN_MEASUREMENTS = 1000; // in ms
	private final Sensor accelerometer_ = sensorManager_.getSensorList(
			Sensor.TYPE_ACCELEROMETER).get(0);

	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service.onCreate", Toast.LENGTH_SHORT).show();
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Toast.makeText(this, "Service.onStart: ", Toast.LENGTH_SHORT).show();

		Timer t = new Timer("Wreck Watch - accelerometer crash check service");
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				sensorManager_.registerListener(listener_, accelerometer_,
						SensorManager.SENSOR_DELAY_NORMAL);
			}

		}, 0, TIME_BETWEEN_MEASUREMENTS);
	}

	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service.onDestroy", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void unregisterAccelerometer() {
		sensorManager_.unregisterListener(listener_, accelerometer_);
		listener_.called_ = false;
	}

	private class DecelerationListener implements SensorEventListener {
		public boolean called_;
			
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		public void onSensorChanged(SensorEvent e) {
			// Only allow one sensor event in
			if (called_)
				return;
			called_ = true;
			
			// DO stuff with data
			
			
			// Unregister ourself
			unregisterAccelerometer();
		}

	}
}