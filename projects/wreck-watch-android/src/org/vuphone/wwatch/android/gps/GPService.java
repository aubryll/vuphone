package org.vuphone.wwatch.android.gps;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.bindings.IRegister;
import org.vuphone.wwatch.android.bindings.ISettingsViewCallback;
import org.vuphone.wwatch.android.http.HTTPPoster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class GPService extends Service {

	private static final float GPS_RADIUS = 0.5f;
	private static final long GPS_FREQUENCY = 1000;

	private final WaypointTracker tracker_ = new WaypointTracker();

	/**
	 * Used to keep track of the classes that have bound to us, and allow us to
	 * call on them using the interface declared in the ISettingsViewCallback
	 * class
	 */
	private RemoteCallbackList<ISettingsViewCallback> callbacks_ = new RemoteCallbackList<ISettingsViewCallback>();

	/**
	 * The LocationListener used to receive GPS updates
	 */
	private final LocationListener listener_ = new LocationListener() {
		public void onLocationChanged(Location location) {
			tracker_.addWaypoint(location);

			final int N = callbacks_.beginBroadcast();
			for (int i = 0; i < N; i++) {
				try {
					callbacks_.getBroadcastItem(i).gpsChanged(
							location.getLatitude(), location.getLongitude());
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

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/**
	 * The IRegister interface is defined through IDL, and this variable
	 * contains the implementations of the methods described in the IRegister
	 * interface
	 */
	private final IRegister.Stub binder_ = new IRegister.Stub() {

		public void registerCallback(ISettingsViewCallback cb) {
			Log.v(VUphone.tag, "GPS registering listener");
			if (cb != null)
				callbacks_.register(cb);
		}

		public void unregisterCallback(ISettingsViewCallback cb) {
			Log.v(VUphone.tag, "GPS unregistering listener");
			if (cb != null)
				callbacks_.unregister(cb);
		}
	};

	/**
	 * Return an IBinder for an activity to bind against, allowing that activity
	 * to contact this service and communicate with it, using the interface
	 * declared in the binder_ variable (For this case, the IRegister interface)
	 */
	public IBinder onBind(Intent intent) {
		Log.v(VUphone.tag, "GPS returning IBinder");
		return binder_;
	}

	/**
	 * Comparable to a constructor, this is the first method called to load the
	 * service
	 */
	public void onCreate() {
		super.onCreate();
		VUphone.setContext(this);
		Log.v(VUphone.tag, "GPS ON_CREATE");
		Toast.makeText(this, "GPS Service Created", Toast.LENGTH_SHORT).show();

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_FREQUENCY,
				GPS_RADIUS, listener_);

	}

	/**
	 * Called when the service is being started
	 */
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.v(VUphone.tag, "GPS ON_START");

			SharedPreferences prefs = getSharedPreferences(VUphone.PREFERENCES_FILE, Context.MODE_PRIVATE);
			double d = prefs.getFloat(VUphone.SPEED_SCALE, 1.0f);
			tracker_.setDilation(d);
			Toast.makeText(this, "Speed Scale: " + d, Toast.LENGTH_SHORT)
					.show();

		// Returned from the 'Are you OK?' dialog
		if (intent.hasExtra("DidAccidentOccur")) {
			if (intent.getExtras().getBoolean("DidAccidentOccur")) {
				reportAccident();
				Toast.makeText(this, "Accident Reported", Toast.LENGTH_LONG);
			} else
				Toast.makeText(this, "Here's to your health",
						Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * Comparable to a destructor, this is called when a service is no longer
	 * needed
	 */
	public void onDestroy() {
		super.onDestroy();
		Log.v(VUphone.tag, "GPS ON_DESTROY");
		Toast.makeText(this, "GPS Service destroyed", Toast.LENGTH_SHORT)
				.show();

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(listener_);
	}

	/**
	 * Called when there is solid evidence there was an accident. Simply
	 * communicates with the server for now
	 */
	public void reportAccident() {

		if (tracker_.getList().size() > 0) {
			Toast.makeText(this, "Reporting Accident", Toast.LENGTH_LONG)
					.show();
			Waypoint temp = tracker_.getList().get(
					tracker_.getList().size() - 1);
			final String aid = ((TelephonyManager) super
					.getSystemService(Service.TELEPHONY_SERVICE)).getDeviceId();

			HTTPPoster.doAccidentPost(aid, System.currentTimeMillis(), tracker_
					.getLatestSpeed(), tracker_.getLatestAcceleration(), temp
					.getLatitudeDegrees(), temp.getLongitudeDegrees(), new Runnable() {
						public void run() {
							HTTPPoster.doRoutePost(aid, tracker_.getList());
						}
					});
		} else
			Toast.makeText(this, "No valid GPS data to report.",
					Toast.LENGTH_SHORT).show();
	}

}
