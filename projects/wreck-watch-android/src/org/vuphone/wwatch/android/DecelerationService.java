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
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 * Used to detect a deceleration large enough to report a wreck (or ask the user
 * if there was a wreck). Uses a timer to poll the accelerometer every so often,
 * therefore saving battery life over having the sensor turned on continually
 * 
 * @author Hamilton Turner
 * 
 */
public class DecelerationService extends Service {

	/**
	 * The time in ms between accelerometer measurements. Higher = fewer
	 * measurements, but longer battery life
	 */
	private final static long TIME_BETWEEN_MEASUREMENTS = 100;

	/**
	 * The maximum deceleration in m/s^2 that should be detected before asking
	 * the user if there was a wreck
	 */
	private final static int MAX_ALLOWED_DECELERATION = 30; // in (m/s^2)

	// Variables used to access the accelerometer data
	private SensorManager sensorManager_;
	private Sensor accelerometer_;

	/**
	 * The TimerTask that is used to start the accelerometer every
	 * TIME_BETWEEN_MEASUREMENTS
	 */
	private final RegisterTask task_ = new RegisterTask();

	/**
	 * Timer used to contain our RegisterTask
	 * 
	 * @see DecelerationService.RegisterTask
	 */
	private Timer t = new Timer("accelerometer polling service");

	/**
	 * Used to keep track of the classes that have bound to us, and allow us to
	 * call on them using the interface declared in the ISettingsViewCallback
	 * class
	 */
	private RemoteCallbackList<ISettingsViewCallback> callbacks_ = new RemoteCallbackList<ISettingsViewCallback>();

	/**
	 * Magnifies the readings from the accelerometer for testing purposes
	 */
	private float accelerationScale_ = (float) 1.0;

	/**
	 * The IRegister interface is defined through IDL, and this variable
	 * contains the implementations of the methods described in the IRegister
	 * interface (In this case, all those callbacks do is allow someone bound to
	 * our service to register/unregister themselves for receiving callbacks
	 * from us)
	 */
	private final IRegister.Stub binder_ = new IRegister.Stub() {

		public void registerCallback(ISettingsViewCallback callback)
				throws RemoteException {
			if (callback != null)
				callbacks_.register(callback);
		}

		public void unregisterCallback(ISettingsViewCallback callback)
				throws RemoteException {
			if (callback != null)
				callbacks_.unregister(callback);
		}

	};
	
	// set to true as a bootstrapping measure telling
	private boolean receivedData_ = true;

	/**
	 * Listener for Accelerometer data. Also handles starting the 'Are you OK?'
	 * dialog
	 */
	private SensorEventListener listener_ = new SensorEventListener() {

		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		public void onSensorChanged(SensorEvent e) {
			Log.v(VUphone.tag, "Accel data received");
			// Only allow one sensor event in
			
			if (receivedData_)
			{
				Log.v(VUphone.tag, "Skipping sensor data");
				return;
			}
			
			receivedData_ = true;

			// Send out changed data to anyone that has registered for
			// broadcasts
			final int N = callbacks_.beginBroadcast();
			for (int i = 0; i < N; i++) {
				try {
					callbacks_.getBroadcastItem(i).accelerometerChanged(
							e.values[0], e.values[1], e.values[2]);
				} catch (RemoteException ex) {
					// The RemoteCallbackList will take care of removing
					// the dead object for us.
				}
			}
			callbacks_.finishBroadcast();

			float valx = e.values[0];
			float valy = e.values[1];
			float valz = e.values[2];

			// Save ourselves a multiply if we can
			if (accelerationScale_ != 1.0) {
				valx *= accelerationScale_;
				valy *= accelerationScale_;
				valz *= accelerationScale_;
			}

			// Check if we exceeded our max decel
			// Open dialog if we have
			if (Math.abs(valx) > MAX_ALLOWED_DECELERATION
					|| Math.abs(valy) > MAX_ALLOWED_DECELERATION
					|| Math.abs(valz) > MAX_ALLOWED_DECELERATION) {

				Intent intent = new Intent(DecelerationService.this,
						org.vuphone.wwatch.android.TestingUI.class);

				intent.putExtra("ActivityMode", TestingUI.CONFIRM);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				final int num = callbacks_.beginBroadcast();
				for (int i = 0; i < num; ++i) {
					try {
						callbacks_.getBroadcastItem(i).showConfirmDialog();
					} catch (RemoteException re) {

					}

				}
				callbacks_.finishBroadcast();

				// We detected wreck, turn ourself off to conserve power and to
				// clean up nicely
				t.cancel();
				unregisterAccelerometer();
				stopSelf();
			} else {
				// Unregister ourself, we only want one update at a time
				unregisterAccelerometer();
			}
		}
	};

	/**
	 * Return an IBinder for an activity to bind against, allowing that activity
	 * to contact this service and communicate with it, using the interface
	 * declared in the IBinder (For this case, the IRegister interface)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// First set the acceleration scale for our parent, so they can see
		// the scaled acceleration values
		final int N = callbacks_.beginBroadcast();
		for (int i = 0; i < N; i++) {
			try {
				callbacks_.getBroadcastItem(i).setAccelerometerMultiplier(
						(int) accelerationScale_);
			} catch (RemoteException ex) {
				// The RemoteCallbackList will take care of removing
				// the dead object for us.
			}
		}
		callbacks_.finishBroadcast();

		// Now return binder
		return binder_;
	}

	/**
	 * Comparable to a constructor, this is the first method called to load the
	 * service
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		// Setup vars to talk to accelerometer
		sensorManager_ = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer_ = sensorManager_
				.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		
		// Start our RegisterTask
		t.scheduleAtFixedRate(task_, 0, TIME_BETWEEN_MEASUREMENTS);

	}

	/**
	 * Called when the service is being started
	 */
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		if (intent.hasExtra("AccelerationScaleFactor"))
			accelerationScale_ = intent.getExtras().getFloat(
					"AccelerationScaleFactor");

		final int num = callbacks_.beginBroadcast();
		for (int i = 0; i < num; ++i) {
			try {
				callbacks_.getBroadcastItem(i).setAccelerometerMultiplier((int)accelerationScale_);
			} catch (RemoteException re) {

			}

		}
		callbacks_.finishBroadcast();
		
		Toast.makeText(this,
				"Deceleration scale " + accelerationScale_,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Comparable to a destructor, this is called when a service is no longer
	 * needed
	 */
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Deceleration Service Destroyed",
				Toast.LENGTH_SHORT).show();
		Log.v(VUphone.tag, "Decel Service onDestroy reached");
		unregisterAccelerometer();
		task_.cancel();
		t.cancel();
		t = null;

	}

	/**
	 * Used to unregister the listener from the accelerometer
	 */
	private void unregisterAccelerometer() {
		Log.v(VUphone.tag, "Listener unregistered");
		sensorManager_.unregisterListener(listener_, accelerometer_);
	}

	/**
	 * Used to register the listener for data from the accelerometer every
	 * TIME_BETWEEN_MEASUREMENTS ms
	 */
	private class RegisterTask extends TimerTask {

		@Override
		public void run() {
			
			if (receivedData_ == false)
			{
				//Log.v(VUphone.tag, "Skipping registerTask");
				return;
			}
			receivedData_ = false;
			
			sensorManager_.registerListener(listener_, accelerometer_,
					SensorManager.SENSOR_DELAY_FASTEST);
			
			
			Log.v(VUphone.tag, "RegisterTask activated");
		}

	}
}