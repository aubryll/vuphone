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

	public void onCreate() {
		super.onCreate();

		sensorManager_ = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer_ = sensorManager_
				.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Toast.makeText(this, "Deceleration Service Started", Toast.LENGTH_SHORT).show();

		// Ensure that the timer is not scheduled with multiple calls to onStart
		if (startedTimer_ == false)
			t.schedule(task_, 0, TIME_BETWEEN_MEASUREMENTS);
		startedTimer_ = true;
	}

	public void onDestroy() {
		super.onDestroy();
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
			if (called_)
				return;
			called_ = true;

			// Do stuff with data
			if (Math.abs(e.values[0]) > MAX_ALLOWED_DECELERATION
					|| Math.abs(e.values[1]) > MAX_ALLOWED_DECELERATION
					|| Math.abs(e.values[2]) > MAX_ALLOWED_DECELERATION) {
				makeToast("Firing intent, detected X:" + e.values[0] + ", Y:"
						+ e.values[1] + ", Z:" + e.values[2]);
				Intent intent = new Intent(context_,
						org.vuphone.wwatch.android.ServiceUI.class);

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