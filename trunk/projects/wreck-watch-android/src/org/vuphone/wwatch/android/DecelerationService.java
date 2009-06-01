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
import android.widget.Toast;

public class DecelerationService extends Service {
	private static final String LOG_TAG = "VUPHONE";
	private final static long TIME_BETWEEN_MEASUREMENTS = 50; // in ms
	private final static int MAX_ALLOWED_DECELERATION = 30; // in (m/s^2)
	private SensorManager sensorManager_;
	private Sensor accelerometer_;
	private final RegisterTask task_ = new RegisterTask();
	
	/**
	 * Used to keep track of the classes that have bound to us, and allow us to
	 * call on them using the interface declared in the ISettingsViewCallback
	 * class
	 */
	private RemoteCallbackList<ISettingsViewCallback> callbacks_ = new RemoteCallbackList<ISettingsViewCallback>();

	/**
	 * Used to wake up the accelerometer once every TIME_BETWEEN_MEASUREMENTS
	 */
	private final Timer t = new Timer("accelerometer polling service");

	/**
	 * Magnifies the readings from the accelerometer for testing purposes
	 * 
	 */
	private float accelerationScale_ = (float) 1.0;

	/**
	 * The IRegister interface is defined through IDL, and this variable
	 * contains the implementations of the methods described in the IRegister
	 * interface
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

	/**
	 * Used to help the listener. Allows us to conserve battery by only
	 * receiving one update every time we turn on the accelerometer
	 */
	private boolean listenerCalled_;

	/**
	 * Listener for Accelerometer data. Also handles starting the 'Are you OK?'
	 * dialog
	 */
	private SensorEventListener listener_ = new SensorEventListener() {

		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		public void onSensorChanged(SensorEvent e) {
			// Only allow one sensor event in
			if (listenerCalled_)
				return;
			listenerCalled_ = true;

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

			// Do stuff with data
			if (Math.abs(valx) > MAX_ALLOWED_DECELERATION
					|| Math.abs(valy) > MAX_ALLOWED_DECELERATION
					|| Math.abs(valz) > MAX_ALLOWED_DECELERATION) {

				Intent intent = new Intent(DecelerationService.this,
						org.vuphone.wwatch.android.ServiceUI.class);

				intent.putExtra("ActivityMode", ServiceUI.CONFIRM);
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
				listenerCalled_ = true;
				stopSelf();
			} else {
				// Unregister ourself
				unregisterAccelerometer();
			}
		}
	};

	/**
	 * Return an IBinder for an activity to bind against, allowing that activity
	 * to contact this service and communicate with it, using the interface
	 * declared in the binder_ variable (For this case, the IRegister interface)
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
	public void onCreate() {
		super.onCreate();

		sensorManager_ = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer_ = sensorManager_
				.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);

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

		Toast.makeText(this,
				"Deceleration Service Started, scale " + accelerationScale_,
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Comparable to a destructor, this is called when a service is no longer
	 * needed
	 */
	public void onDestroy() {
		super.onDestroy();
		Toast
				.makeText(this, "Deceleration Service Destroyed",
						Toast.LENGTH_SHORT).show();
		unregisterAccelerometer();
		t.cancel();

	}

	/**
	 * Used to unregister the listener from the accelerometer
	 */
	private void unregisterAccelerometer() {
		sensorManager_.unregisterListener(listener_, accelerometer_);
		listenerCalled_ = false;
	}

	/**
	 * Used to register the listener for data from the accelerometer.
	 * 
	 */
	private class RegisterTask extends TimerTask {

		@Override
		public void run() {
			sensorManager_.registerListener(listener_, accelerometer_,
					SensorManager.SENSOR_DELAY_UI);
		}

	}
}