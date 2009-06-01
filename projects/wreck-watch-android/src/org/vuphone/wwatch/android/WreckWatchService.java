package org.vuphone.wwatch.android;

import java.util.Timer;
import java.util.TimerTask;

import org.vuphone.wwatch.android.http.HTTPPoster;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class WreckWatchService extends Service implements LocationListener {
	private RemoteCallbackList<ISettingsViewCallback> callbacks_ = new RemoteCallbackList<ISettingsViewCallback>();

	final static double HIGH_SPEED = 22.353; // 50 mph = 22.353 meters/second
	final static double HIGH_ACCEL = 0; // TODO - Look up reasonable values

	final static String tag = "VUPHONE";

	final static boolean BOOTSTRAP_ACCEL_SERVICE = true;

	private WaypointTracker tracker_ = null;
	private boolean startedDecelerationService_ = false;

	private float accelerationScale_ = (float) 1.0;

	public void checkSpeed() {
		// If we are going fast, start the Deceleration detection service
		// If we are going slow, stop it
		// Use a bool flag to prevent generating garbage intents
		// Use the bootstrap to avoid this check alltogether
		if (BOOTSTRAP_ACCEL_SERVICE == false) {
			if ((tracker_.getLatestSpeed() >= HIGH_SPEED)
					&& (startedDecelerationService_ == false)) {
				Intent dec = new Intent(this, DecelerationCheckService.class);
				dec.putExtra("AccelerationScaleFactor", accelerationScale_);
				startService(dec);
				bindService(dec, connection_, Context.BIND_AUTO_CREATE);
				startedDecelerationService_ = true;
			} else if ((tracker_.getLatestSpeed() < HIGH_SPEED)
					&& (startedDecelerationService_)) {
				startedDecelerationService_ = false;
				stopService(new Intent(this, DecelerationCheckService.class));
				unbindService(connection_);
			}
		}

		Log.v(tag, "WWS informing SUI of real and scale speed");
		final int N = callbacks_.beginBroadcast();
		for (int i = 0; i < N; i++) {
			try {
				callbacks_.getBroadcastItem(i).setRealSpeed(
						tracker_.getLatestSpeed() / tracker_.getDilation());
				callbacks_.getBroadcastItem(i).setScaleSpeed(
						tracker_.getLatestSpeed());
			} catch (RemoteException ex) {
				// The RemoteCallbackList will take care of removing
				// the dead object for us.
			}
		}
		callbacks_.finishBroadcast();
	}

	public void reportAccident() {
		Toast.makeText(this, "Reporting Accident", Toast.LENGTH_LONG).show();

		// TODO - should be updated to get the actual acceleration
		HTTPPoster.doAccidentPost(System.currentTimeMillis(), tracker_
				.getLatestSpeed(), tracker_.getLatestAcceleration(), tracker_
				.getList());
	}

	public IBinder onBind(Intent intent) {
		Log.v(tag, "WWS returning IBinder");
		return binder_;
	}

	// Service lifecycle
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "GPS Service Created", Toast.LENGTH_SHORT).show();
		tracker_ = new WaypointTracker();

		// TODO - possibly change this to coarse location, and definitely
		// increase the min time between GPS updates to conserve battery power
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

		if (BOOTSTRAP_ACCEL_SERVICE)
			(new Timer()).schedule(new TimerTask() {

				@Override
				public void run() {
					Intent dec = new Intent(WreckWatchService.this,
							DecelerationCheckService.class);
					dec.putExtra("AccelerationScaleFactor", accelerationScale_);
					startService(dec);
					bindService(dec, connection_, Context.BIND_AUTO_CREATE);
					startedDecelerationService_ = true;
				}
			}, 2000);
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		// If we return from the 'Are you OK?' dialog, we need to skip this
		if (intent.hasExtra("TimeDialation")) {
			double d = intent.getExtras().getDouble("TimeDialation");
			tracker_.setDilation(d);
			Toast.makeText(this, "GPS Service Started, dialation is " + d,
					Toast.LENGTH_LONG).show();
		}
		if (intent.hasExtra("AccelerationScaleFactor")) {
			accelerationScale_ = intent.getExtras().getFloat(
					"AccelerationScaleFactor");
		}

		// Returned from the 'Are you OK?' dialog
		if (intent.hasExtra("DidAccidentOccur")) {
			if (intent.getExtras().getBoolean("DidAccidentOccur")) {
				this.reportAccident();
				Toast.makeText(this, "Accident Reported", Toast.LENGTH_LONG);
			} else {
				Toast.makeText(this, "Here's to your health",
						Toast.LENGTH_SHORT).show();
				// Restart the DecelerationCheckService
				// Intent dec = new Intent(WreckWatchService.this,
				// DecelerationCheckService.class);
				// dec.putExtra("AccelerationScaleFactor", accelerationScale_);
				// startService(dec);
				// bindService(dec, connection_, Context.BIND_AUTO_CREATE);
				// startedDecelerationService_ = true;

			}
		}

	}

	public void onDestroy() {

		super.onDestroy();
		Toast.makeText(this, "GPS Service destroyed", Toast.LENGTH_SHORT)
				.show();
		Intent intent = new Intent(this,
				org.vuphone.wwatch.android.DecelerationCheckService.class);
		stopService(intent);
		
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(this);
		
		try {
			unbindService(connection_);
		} catch (Exception e) {

		}

		

	}

	// LocationListener

	public void onLocationChanged(Location location) {
		tracker_.addWaypoint(location);

		Log.v(tag, "WWS sending location changed");
		final int N = callbacks_.beginBroadcast();
		for (int i = 0; i < N; i++) {
			try {
				callbacks_.getBroadcastItem(i).gpsChanged(
						location.getLatitude(), location.getLongitude());
			} catch (RemoteException ex) {
				// The RemoteCallbackList will take care of removing
				// the dead object for us.
			}
		}
		callbacks_.finishBroadcast();

		checkSpeed();
	}

	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "GPS disabled", Toast.LENGTH_SHORT);
	}

	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "GPS enabled", Toast.LENGTH_SHORT);
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * The IRemoteInterface is defined through IDL
	 */
	private final IRegister.Stub binder_ = new IRegister.Stub() {

		public void registerCallback(ISettingsViewCallback cb) {
			Log.v(tag, "WWS registering listener");
			if (cb != null)
				callbacks_.register(cb);
		}

		public void unregisterCallback(ISettingsViewCallback cb) {
			Log.v(tag, "WWS unregistering listener");
			if (cb != null)
				callbacks_.unregister(cb);
		}
	};

	/**
	 * Class for interacting with the main interface of the Deceleration Check
	 * Service
	 */
	private ServiceConnection connection_ = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been
			// established, giving us the service object we can use to
			// interact with the service. We are communicating with our
			// service through an IDL interface, so get a client-side
			// representation of that from the raw service object.
			IRegister mService = IRegister.Stub.asInterface(service);

			// We want to monitor the service for as long as we are
			// connected to it.
			try {
				mService.registerCallback(callback_);
			} catch (RemoteException e) {
				// In this case the service has crashed before we could even
				// do anything with it; we can count on soon being
				// disconnected (and then reconnected if it can be restarted)
				// so there is no need to do anything here.
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
		}
	};

	/**
	 * Defined as our callback implementation, allowing DecelerationCheckService
	 * to call on us
	 */
	private ISettingsViewCallback callback_ = new ISettingsViewCallback.Stub() {
		public void accelerometerChanged(float x, float y, float z)
				throws RemoteException {

			final int N = callbacks_.beginBroadcast();
			for (int i = 0; i < N; i++) {
				try {
					callbacks_.getBroadcastItem(i)
							.accelerometerChanged(x, y, z);
				} catch (RemoteException ex) {
					// The RemoteCallbackList will take care of removing
					// the dead object for us.
				}
			}
			callbacks_.finishBroadcast();
		}

		public void gpsChanged(double lat, double lng) throws RemoteException {

			final int N = callbacks_.beginBroadcast();
			for (int i = 0; i < N; i++) {
				try {
					callbacks_.getBroadcastItem(i).gpsChanged(lat, lng);
				} catch (RemoteException ex) {
					// The RemoteCallbackList will take care of removing
					// the dead object for us.
				}
			}
			callbacks_.finishBroadcast();

		}

		public void setAccelerometerMultiplier(int multip)
				throws RemoteException {

			final int N = callbacks_.beginBroadcast();
			for (int i = 0; i < N; i++) {
				try {
					callbacks_.getBroadcastItem(i).setAccelerometerMultiplier(
							multip);
				} catch (RemoteException ex) {
					// The RemoteCallbackList will take care of removing
					// the dead object for us.
				}
			}
			callbacks_.finishBroadcast();

		}

		public void setRealSpeed(double speed) throws RemoteException {

			final int N = callbacks_.beginBroadcast();
			for (int i = 0; i < N; i++) {
				try {
					callbacks_.getBroadcastItem(i).setRealSpeed(speed);
				} catch (RemoteException ex) {
					// The RemoteCallbackList will take care of removing
					// the dead object for us.
				}
			}
			callbacks_.finishBroadcast();

		}

		public void setScaleSpeed(double speed) throws RemoteException {

			final int N = callbacks_.beginBroadcast();
			for (int i = 0; i < N; i++) {
				try {
					callbacks_.getBroadcastItem(i).setScaleSpeed(speed);
				} catch (RemoteException ex) {
					// The RemoteCallbackList will take care of removing
					// the dead object for us.
				}
			}
			callbacks_.finishBroadcast();
		}

		public void showConfirmDialog() throws RemoteException {
			int num = callbacks_.beginBroadcast();
			for (int i = 0; i < num; ++i) {
				try {
					callbacks_.getBroadcastItem(i).showConfirmDialog();
				} catch (RemoteException re) {

				}
			}
		}

	};

}
