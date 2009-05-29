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

public class DecelerationCheckService extends Service {
	private RemoteCallbackList<ISettingsViewCallback> callbacks_ = new RemoteCallbackList<ISettingsViewCallback>();
	
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

		sensorManager_ = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer_ = sensorManager_
				.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		
		if (intent.hasExtra("AccelerationScaleFactor"))
			accelerationScale_ = intent.getExtras().getFloat("AccelerationScaleFactor");
		
		Toast.makeText(this, "Deceleration Service Started, scale " + accelerationScale_, Toast.LENGTH_SHORT).show();
		
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
		final int N = callbacks_.beginBroadcast();
		for (int i = 0; i < N; i++) {
			try {
				callbacks_.getBroadcastItem(i).setAccelerometerMultiplier((int)accelerationScale_);
			} catch (RemoteException ex) {
				// The RemoteCallbackList will take care of removing
				// the dead object for us.
			}
		}
		callbacks_.finishBroadcast();
		return binder_;
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
			
			float valx = e.values[0] * accelerationScale_;
			float valy = e.values[1] * accelerationScale_;
			float valz = e.values[2] * accelerationScale_;

			// Do stuff with data
			if (Math.abs(valx) > MAX_ALLOWED_DECELERATION
					|| Math.abs(valy) > MAX_ALLOWED_DECELERATION
					|| Math.abs(valz) > MAX_ALLOWED_DECELERATION) {
				
				Intent intent = new Intent(context_,
						org.vuphone.wwatch.android.ServiceUI.class);

				intent.putExtra("ActivityMode", ServiceUI.CONFIRM);
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

	private class RegisterTask extends TimerTask {

		@Override
		public void run() {
			// I would use sensor display normal, but I am not convinced it works
			sensorManager_.registerListener(listener_, accelerometer_,
					SensorManager.SENSOR_DELAY_UI);
		}

	}
}