package org.vuphone.wwatch.android;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MediaUploadService extends Service implements LocationListener {

	private LocationManager man_;
	private static float ACCURACY = 20f;
	private Location location_ = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		man_ = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		man_.removeUpdates(this);
	}

	public void onLocationChanged(Location newLocation) {
		// If accuracy info is not available that simple save the location and
		// unregister
		if (!newLocation.hasAccuracy()) {
			if (location_ == null) {
				location_ = newLocation;
				man_.removeUpdates(this);
			}
			return;
		}
		// Else, if we have accuracy then compare to the last one and only save
		// if accuracy is better
		// TODO - Make this a progressive capture...............................
		if (newLocation.getAccuracy() < ACCURACY) {
			location_ = newLocation;
			man_.removeUpdates(this);
		}
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		final String uriStr = intent.getStringExtra("Uri");
		if (uriStr == null)	// We weren't started correctly.
			stopSelf();
		
		Uri uri = Uri.parse(uriStr);
		Log.v(VUphone.tag, "Uri=" + uri.toString());
		final ImageUploader imgUp = new ImageUploader(uri, this);
		imgUp.loadImage();
		imgUp.uploadImage();
		
		// Get updates as often as possible.
		man_.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
