package org.vuphone.wwatch.android;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

public class Tabs extends TabActivity {
	public final static String tag = "VUPHONE";
	public final static String PREFS = "WreckWatchMainPrefs";
	public final static String FIRST_LOAD = "FirstTimeLoading";

	private Intent gpsIntent_;
	private Intent accelIntent_;

	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		// Start GPS Service
		gpsIntent_ = new Intent(this, GPService.class);
		startService(gpsIntent_);
		Log.v(tag, "Tabs started GPS, now binding");

		bindService(gpsIntent_, TestingUI.gpsConnection_, BIND_AUTO_CREATE);

		// Start Accelerometer service
		accelIntent_ = new Intent(this, DecelerationService.class);
		float accelScale = (float) 1.0;
		accelIntent_.putExtra("AccelerationScaleFactor", accelScale);
		startService(accelIntent_);
		Log.v(tag, "Tabs started Accel, now binding");
		boolean bound = bindService(accelIntent_, TestingUI.accelConnection_,
				BIND_AUTO_CREATE);

		SharedPreferences prefs = getSharedPreferences(PREFS,
				Context.MODE_PRIVATE);

		if (prefs.contains(FIRST_LOAD) == false) {
			Editor editor = prefs.edit();
			editor.putBoolean(FIRST_LOAD, false);
			editor.commit();
		}

		TabHost th = getTabHost();
		th.addTab(th.newTabSpec("Tab1").setIndicator("Settings").setContent(
				new Intent(this, SettingsUI.class)));
		th.addTab(th.newTabSpec("Tab2").setIndicator("Testing").setContent(
				new Intent(this, TestingUI.class)));
		th.addTab(th.newTabSpec("Tab3").setIndicator("Wrecks").setContent(
				new Intent(this, AccidentViewer.class)));
		th.addTab(th.newTabSpec("Tab4").setIndicator("Contacts").setContent(
				new Intent(this, ContactPicker.class)));

		th.setCurrentTab(2);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(tag, "Tabs onDestroy reached");

		try {

			unbindService(TestingUI.accelConnection_);
			unbindService(TestingUI.gpsConnection_);
		} catch (Exception e) {
			Log
					.w(VUphone.tag,
							"Tabs: onDestroy(): Service was not bound, but unbindService was called");
		}
	}

	public void stopServices() {
		try {
			unbindService(TestingUI.accelConnection_);
			unbindService(TestingUI.gpsConnection_);
		} catch (Exception e) {
			Log
					.w(VUphone.tag,
							"Tabs: stopServices(): Service was not bound, but unbindService was called");
		}

		stopService(accelIntent_);
		stopService(gpsIntent_);
	}
}
