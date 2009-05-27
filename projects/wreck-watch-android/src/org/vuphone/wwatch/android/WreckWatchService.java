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
	WaypointTracker tracker_ = null;
	
	public void checkAccident() {
		
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
		
	}
	
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service.onDestroy", Toast.LENGTH_SHORT).show();
	}
	
	// LocationListener
	
	public void onLocationChanged(Location location) {
		// TODO - REMOVE THIS CHECK
		if (location.getLatitude() == 12 && location.getLongitude() == 34)
			this.stopSelf();
		
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
