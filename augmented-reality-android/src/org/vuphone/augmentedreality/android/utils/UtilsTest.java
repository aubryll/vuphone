package org.vuphone.augmentedreality.android.utils;

import java.util.Timer;
import java.util.TimerTask;

import org.vuphone.augmentedreality.android.filter.AngleFilter;

import android.app.Activity;
import android.os.Bundle;

public class UtilsTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		final AngleFilter filter = new AngleFilter();
		filter.start();
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				filter.addAngle(0);
			}
		};
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(task, 1000, 2000);
	}
}
