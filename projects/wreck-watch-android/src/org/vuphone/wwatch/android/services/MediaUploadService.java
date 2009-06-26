package org.vuphone.wwatch.android.services;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.media.ImageUploadListener;
import org.vuphone.wwatch.android.media.ImageUploadMetaInformation;
import org.vuphone.wwatch.android.media.ImageUploader;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MediaUploadService extends Service implements ImageUploadListener {

	private static float ACCURACY_MARGIN = 5f;
	private static long WAIT_TIME = 5000;

	/** Manages the location service */
//	private LocationManager man_;
	/** Current location */
//	private Location location_ = null;

	/** Responsible for loading and uploading the image */
	private ImageUploader uploader_;

	/** A flag indicating whether the image was loaded */
	private boolean imageLoaded_ = false;

	private long wreckId_ = 0;
	
	/** Used to properly display toasts from callbacks */
	private final Handler handler_ = new Handler();

	public void badServerResponse() {

	}

	public void fileNotFound() {
		showToast("Requested image not found");
		stopSelf();
	}

	public void goodServerResponse() {
		showToast("Image uploaded succesfully");
		stopSelf();
	}

	public void imageLoaded() {
		imageLoaded_ = true;
		showToast("Image loaded");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// Need to get the geo fix as quickly as possible so request here.
//		man_ = (LocationManager) getSystemService(LOCATION_SERVICE);
//		man_.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(VUphone.tag, "onDestroy()");
		// Turn off the GPS in case it's still active.
//		man_.removeUpdates(this);
	}
/*
	public void onLocationChanged(Location newLocation) {
		// If this gets called then we're still allowed to collect data so wait
		// for the best Location point.

		// If no accuracy data is available then simply save the new location
		if (!newLocation.hasAccuracy()) {
			location_ = newLocation;
		} else {

			if (location_ == null)
				location_ = newLocation;
			
			// Else, only save if this location is more accurate than the
			// previous +/- some margin. Note, I'm using this margin because
			// later location updates are usually closer to the actual position
			// but not necessarily more accurate.
			if (newLocation.getAccuracy() + ACCURACY_MARGIN < location_
					.getAccuracy()) {
				location_ = newLocation;
			}
		}

		// If the image is ready and location is non-null then send it and
		// remove gps updates.
		if (imageLoaded_ && location_ != null) {
			uploader_.uploadImage();
			man_.removeUpdates(this);
		}
	}
*/
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		final String uriStr = intent.getStringExtra("Uri");
		if (uriStr == null) // We weren't started correctly.
			stopSelf();
		
		wreckId_ = intent.getLongExtra("WreckId", -1);
		if (wreckId_ == -1) {
			//stopSelf();
		}

		Uri uri = Uri.parse(uriStr);
		Log.v(VUphone.tag, "Uri=" + uri.toString());

		uploader_ = new ImageUploader(uri, this, this);
		uploader_.loadImage();
		uploader_.uploadImage();
	}

	public void serverUploadFailed() {
		showToast("Upload failed");
		stopSelf();
	}

	public void setMetaInformation(ImageUploadMetaInformation info) {
		//info.setLocation(location_);
		showToast("Setting meta");
		info.setTime(System.currentTimeMillis());
		info.setId(wreckId_);
	}

	public void showToast(final String msg) {
		handler_.post(new Runnable() {
			public void run() {
				Toast
						.makeText(MediaUploadService.this, msg,
								Toast.LENGTH_SHORT).show();
			}
		});
	}
}
