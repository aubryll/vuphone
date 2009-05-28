package org.vuphone.wwatch.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class WreckWatchService extends Service implements LocationListener{

	private final long FREQUENCY = 500;	// How often (ms) to run accident checking routine
	final static double HIGH_SPEED = 22.353; // 50 mph
	final static double HIGH_ACCEL = 0; // TODO - Look up reasonable values
	
	private WaypointTracker tracker_ = null;
	
	public void checkAccident() {
		if (tracker_.getLatestSpeed() >= WreckWatchService.HIGH_SPEED &&
			tracker_.getLatestAcceleration() >= WreckWatchService.HIGH_ACCEL){
			
			this.getUserConfirmation();
		}
	}
	
	/**
	 * Invokes a GUI window to ask the user whether an accident occurred.
	 * To avoid binding to this service, the following hack will be used.
	 * This service's onStart() method will be called from the dialog. 
	 * The calling intent will contain a boolean response.
	 */
	public void getUserConfirmation() {
		Intent intent = new Intent(this, org.vuphone.wwatch.android.ServiceUI.class);

		intent.putExtra("ActivityMode", ServiceUI.CONFIRM);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}
	
	public void reportAccident() {
		Toast.makeText(this, "reportAccident hook", Toast.LENGTH_SHORT).show();
	}
	
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	// Service lifecycle
	
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service.onCreate", Toast.LENGTH_SHORT).show();
		tracker_ = new WaypointTracker();
		
		// Schedule an accident check every FREQUENCY milliseconds
		(new Timer(true)).scheduleAtFixedRate(new TimerTask() {
			public void run() {
				WreckWatchService.this.checkAccident();
			}
		}, 0, FREQUENCY);
		
		
		LocationManager man = (LocationManager) super.getSystemService(Context.LOCATION_SERVICE);
		man.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		double d = intent.getExtras().getDouble("TimeDialation");
		Toast.makeText(this, "Service.onStart: " + d, Toast.LENGTH_SHORT).show();
		
		if (intent.hasExtra("DidAccidentOccur")) {
			if (intent.getExtras().getBoolean("DidAccidentOccur")) {
				this.reportAccident();
			}else {
				Toast.makeText(this, "No accident", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service.onDestroy", Toast.LENGTH_SHORT).show();
	}
	
	// LocationListener
	
	public void onLocationChanged(Location location) {
		// TODO - REMOVE THESE CHECKS
		if (location.getLongitude() == 12 && location.getLatitude() == 34)
			this.stopSelf();
		
		// Test the dialog
		if (location.getLongitude() == 56 && location.getLatitude() == 78) {
			this.getUserConfirmation();
		}
		
		tracker_.addWaypoint(location);
		Toast.makeText(this, "New location added. Latest Speed: " + tracker_.getLatestSpeed(), Toast.LENGTH_SHORT).show();
	}
	
	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras){
	}
}
