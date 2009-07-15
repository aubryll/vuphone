/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.android;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.vuphone.assassins.android.http.HTTPGetter;
import org.vuphone.assassins.android.landmine.LandMine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServicePeriodicUpdate extends Service {

	/** The time in milliseconds between updates */
	private static final int period_ = 1000 * 120;

	/** The timer used to run the update */
	private Timer timer_;
	
	private String pre = "ServicePeriodicUpdate: ";
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(VUphone.tag, pre + "onCreate called.");
		timer_ = new Timer();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i(VUphone.tag, pre + "onStart called.");
		start();
	}
	
	private TimerTask getNewTask() {
		return new TimerTask() {
			public void run() {
				performUpdate();
				Log.i(VUphone.tag, pre + "Scheduling new timer task in "+period_);
				timer_.schedule(getNewTask(), period_);
			}
		};
	}

	protected void start() {
		Log.i(VUphone.tag, pre + "Starting Full Update");
		
		// To be sure that timer dies
		timer_.cancel();

		timer_ = new Timer();
		timer_.schedule(getNewTask(), 0);
	}

	protected void stop() {
		Log.i(VUphone.tag, pre + "Stopping Full Update");
		timer_.cancel();
	}
	
	protected void forceUpdate() {
		Log.i(VUphone.tag, pre + "Forcing update");
		stop();
		start();
	}

	/**
	 * This method will query the server for all forms of data
	 * that need to be updated after specific time intervals.
	 * This pull approach is used rather than a push from the
	 * server approach in order to save battery by not forcing
	 * the phone to always maintain an open connection to the
	 * server, at the cost of potentially receiving some updates
	 * late.
	 */
	private void performUpdate() {
		Log.d(VUphone.tag, pre + "Performing full update");
		
		ArrayList<LandMine> mines = HTTPGetter.doLandMineGet();
		GameObjects.getInstance().setLandMines(mines, this);
		
	}

}
