package org.vuphone.wwatch.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MediaUploadService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(VUphone.tag, "MediaUploadService.onCreate()");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(VUphone.tag, "MediaUploadService.onDestroy()");
	}
	
	@Override
	public void onStart(Intent intent, int id) {
		super.onStart(intent, id);
		Log.v(VUphone.tag, "MediaUploadService.onStart()");
	}

}
