package org.vuphone.wwatch.android;

import java.util.Timer;
import java.util.TimerTask;

import org.vuphone.wwatch.android.http.HTTPPoster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class WreckWatchService extends Service implements LocationListener {

	private final long FREQUENCY = 500; // How often (ms) to run accident
	// checking routine
	final static double HIGH_SPEED = 22.353; // 50 mph = 22.353 meters/second
	final static double HIGH_ACCEL = 0; // TODO - Look up reasonable values

	private WaypointTracker tracker_ = null;
	private boolean startedDecelerationService_ = false;

	public void checkSpeed() {
		// If we are going fast, start the Deceleration detection service
		// If we are going slow, stop it
		// Use a bool flag to prevent generating garbage intents
		if ((tracker_.getLatestSpeed() >= HIGH_SPEED)
				&& (startedDecelerationService_ == false)) {
			startService(new Intent(this,
					org.vuphone.wwatch.android.DecelerationCheckService.class));
			startedDecelerationService_ = true;
		} else if ((tracker_.getLatestSpeed() < HIGH_SPEED)
				&& (startedDecelerationService_)) {
			stopService(new Intent(this,
					org.vuphone.wwatch.android.DecelerationCheckService.class));
		}
	}

	public void reportAccident() {
		Toast.makeText(this, "Reporting Accident", Toast.LENGTH_LONG).show();

		// TODO - should be updated to get the actual acceleration
		HTTPPoster.doAccidentPost(System.currentTimeMillis(), tracker_
				.getLatestSpeed(), tracker_.getLatestAcceleration(), tracker_
				.getList());
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	// Service lifecycle
	public void onCreate() {
		super.onCreate();

		Toast.makeText(this, "GPS Service Created", Toast.LENGTH_LONG).show();
		tracker_ = new WaypointTracker();

		// Schedule an speed check every FREQUENCY milliseconds
		// If we are going quickly enough, it will start the accelerometer
		new Timer("GPS speed checker").scheduleAtFixedRate(new TimerTask() {
			public void run() {
				checkSpeed();
			}
		}, 0, FREQUENCY);

		// TODO - possibly change this to coarse location, and definitely
		// increase the min time between GPS updates to conserve battery power
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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

		// Returned from the 'Are you OK?' dialog
		if (intent.hasExtra("DidAccidentOccur")) {
			if (intent.getExtras().getBoolean("DidAccidentOccur")) {
				this.reportAccident();
				Toast.makeText(this, "Accident Reported", Toast.LENGTH_LONG);
			} else {
				Toast.makeText(this, "No accident, glad you're ok!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void onDestroy() {
		
		super.onDestroy();
		Toast.makeText(this, "GPS Service destroyed", Toast.LENGTH_SHORT)
				.show();
	}

	// LocationListener

	public void onLocationChanged(Location location) {
		tracker_.addWaypoint(location);
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
