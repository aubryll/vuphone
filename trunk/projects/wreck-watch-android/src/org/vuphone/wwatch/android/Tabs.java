package org.vuphone.wwatch.android;

import org.vuphone.wwatch.android.http.HTTPPoster;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Tabs extends TabActivity {
	public final static String tag = "VUPHONE";
	public final static String PREFS = "WreckWatchMainPrefs";
	public final static String FIRST_LOAD = "FirstTimeLoading";
	
	public void onCreate(Bundle ice) {
		super.onCreate(ice);
		
		// Start GPS Service
		Intent gpsIntent = new Intent(this, GPService.class);
		double dialation = 1.0;
		gpsIntent.putExtra("TimeDialation", dialation);

		startService(gpsIntent);
		Log.v(tag, "Tabs started GPS, now binding");
		
		bindService(gpsIntent, TestingUI.gpsConnection_, BIND_AUTO_CREATE);

		// Start Accelerometer service
		Intent accelIntent = new Intent(this,
				DecelerationService.class);
		float accelScale = (float) 1.0;
		gpsIntent.putExtra("AccelerationScaleFactor", accelScale);
		startService(accelIntent);
		Log.v(tag, "Tabs started Accel, now binding");
		bindService(accelIntent, TestingUI.accelConnection_, BIND_AUTO_CREATE);
		
		
		SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		
		if (prefs.contains(FIRST_LOAD) == false)
		{
			Editor editor = prefs.edit();
			editor.putBoolean(FIRST_LOAD, false);
			editor.commit();
			HTTPPoster.doInitialPost();
		}
		
		TabHost th = getTabHost();
		th.addTab(th.newTabSpec("Tab1").setIndicator("Settings").setContent(new Intent(this, SettingsUI.class)));
		th.addTab(th.newTabSpec("Tab2").setIndicator("Testing").setContent(new Intent(this, TestingUI.class)));
		th.addTab(th.newTabSpec("Tab3").setIndicator("Wrecks").setContent(new Intent(this, AccidentViewer.class)));
		th.addTab(th.newTabSpec("Tab4").setIndicator("Contacts").setContent(new Intent(this, ContactPicker.class)));
		
		th.setCurrentTab(2);
	}
}
