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
import android.util.Log;
import android.widget.Toast;

public class DecelerationCheckService extends Service {
	
	private static final String LOG_TAG = "VUPHONE";
	private static final String LOG_MSG_PREFIX = "DecelerationCheckService: ";

	private final static long TIME_BETWEEN_MEASUREMENTS = 50; // in ms
	private final static int MAX_ALLOWED_DECELERATION = 10; // in (m/s^2)

	private final Timer t = new Timer(
			"Wreck Watch - accelerometer crash check service");
	private final DecelerationListener listener_ = new DecelerationListener();
	private SensorManager sensorManager_;
	private Sensor accelerometer_;
	private final RegisterTask task_ = new RegisterTask();
	private boolean startedTimer_ = false;
	private final Context context_ = this;
	
	/**
	 * Same as timeDialation_ except for accelerometer
	 * @see WreckWatchService
	 */
	private float accelerationScale_ = (float) 1.0;

	public void onCreate() {
		super.onCreate();
		Log.v(LOG_TAG, LOG_MSG_PREFIX + "Creating DecelerationCheckService");

		sensorManager_ = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer_ = sensorManager_
				.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.v(LOG_TAG, LOG_MSG_PREFIX + "Starting DecelerationCheckService");
		Toast.makeText(this, "Deceleration Service Started", Toast.LENGTH_SHORT).show();
		
		if (intent.hasExtra("AccelerationScaleFactor")){
			accelerationScale_ = intent.getExtras().getFloat("AccelerationScaleFactor");
			Log.d(LOG_TAG, LOG_MSG_PREFIX + "Acceleration Scale set to " + accelerationScale_);
		}else{
			Log.e(LOG_TAG, LOG_MSG_PREFIX + "No scale factor provided, using 1.0");
		}

		// Ensure that the timer is not scheduled with multiple calls to onStart
		if (startedTimer_ == false)
			t.schedule(task_, 0, TIME_BETWEEN_MEASUREMENTS);
		startedTimer_ = true;
	}

	public void onDestroy() {
		super.onDestroy();
		Log.v(LOG_TAG, LOG_MSG_PREFIX + "Destroying DecelerationCheckService");
		Toast.makeText(this, "Deceleration Service Stopped", Toast.LENGTH_SHORT).show();

		if (startedTimer_)
			t.cancel();
		
		unregisterAccelerometer();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void makeToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
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
			Log.d(LOG_TAG, LOG_MSG_PREFIX + "Sensor event received");
			Log.d(LOG_TAG, LOG_MSG_PREFIX + "X: " + e.values[0] + " \nY: " + e.values[1] + "\nX: " + e.values[2]);
			
			if (called_)
				return;
			called_ = true;
			
			float valx = e.values[0] * accelerationScale_;
			float valy = e.values[1] * accelerationScale_;
			float valz = e.values[2] * accelerationScale_;
			Log.d(LOG_TAG, LOG_MSG_PREFIX + "Scaled X: " + valx + "\nScaled Y: " + valy + "\nScaled Z: " + valz);

			// Do stuff with data
			if (Math.abs(valx) > MAX_ALLOWED_DECELERATION
					|| Math.abs(valy) > MAX_ALLOWED_DECELERATION
					|| Math.abs(valz) > MAX_ALLOWED_DECELERATION) {
				makeToast("Firing intent, detected X:" + valx + ", Y:"
						+ valy + ", Z:" + valz);
				Intent intent = new Intent(context_,
						org.vuphone.wwatch.android.ServiceUI.class);

				Log.i(LOG_TAG, LOG_MSG_PREFIX + "Potential wreck detected");
				intent.putExtra("ActivityMode", ServiceUI.CONFIRM);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context_.startActivity(intent);

				// We detected wreck, turn ourself off to conserve power and to
				// clean up nicely
				unregisterAccelerometer();
				stopSelf();
			}

			// Unregister ourself
			unregisterAccelerometer();
		}
	}

	private class RegisterTask extends TimerTask {

		@Override
		public void run() {
			// I would use sensor display normal, but I am not convinced it works
			sensorManager_.registerListener(listener_, accelerometer_,
					SensorManager.SENSOR_DELAY_UI);
		}

	}
}