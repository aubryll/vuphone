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
package org.vuphone.assassins.android.landmine;

import org.vuphone.assassins.android.MarkedArea;
import org.vuphone.assassins.android.VUphone;
import org.vuphone.assassins.android.notices.ActivityDeathNotice;
import org.vuphone.assassins.android.notices.ActivityWarning;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

/**
 * This class encapsulates the data necessary to represent a land mine.
 * It can be activated or deactivated and contains two warning levels.
 * 
 * @author Scott Campbell
 */
public class LandMine implements MarkedArea{

	private double latitude_;
	private double longitude_;
	private float kill_radius_;
	private PendingIntent kill_intent_;
	private float first_warn_radius_;
	private PendingIntent first_warn_intent_;
	private float second_warn_radius_;
	private PendingIntent second_warn_intent_;
	private boolean activated_ = false;
	
	/**
	 * The id will be the location in the array of active landmines.
	 * It will be set when the landmine is added to that array, which
	 * must happen before activate() is called.
	 */
	private int id_;
	
	private String pre = "LandMine: ";
	
	public LandMine(double lat, double lon, float radius, float first_warn, 
			float second_warn) {
		latitude_ = lat;
		longitude_ = lon;
		kill_radius_ = radius;
		first_warn_radius_ = first_warn;
		second_warn_radius_ = second_warn;
	}
	
	public LandMine(double lat, double lon, float radius, float warn) {
		this (lat, lon, radius, warn * 2, warn);
	}
	
	public LandMine(double lat, double lon, float radius) {
		this (lat, lon, radius, radius * 4, radius * 2);
	}
	
	public void activate(final Context c) {
		if (!activated_) {
			activated_ = true;
			Log.d(VUphone.tag, pre + "LandMine activated!");

			String pack = "org.vuphone.assassins.android.";
			
			Intent toSend = new Intent(c, ActivityDeathNotice.class);
			toSend.putExtra(pack+"KillMethod","LandMine");
			toSend.putExtra(pack+"Id", id_);
			kill_intent_ = PendingIntent.getActivity(c, 0, toSend, 
					Intent.FLAG_ACTIVITY_NEW_TASK);
			LocationManager lm = (LocationManager) 
					c.getSystemService(Context.LOCATION_SERVICE);
			lm.addProximityAlert(latitude_, longitude_, kill_radius_, -1, 
					kill_intent_);
			Log.d(VUphone.tag, pre + "Added proximity alert at ("+latitude_
					+", "+longitude_+") with radius "+kill_radius_);

			Intent toSend2 = new Intent(c, ActivityWarning.class);
			toSend2.putExtra(pack+"WarningAbout", "LandMine");
			toSend2.putExtra(pack+"WarningDistance", first_warn_radius_);
			first_warn_intent_ = PendingIntent.getActivity(c, 0, toSend2, 
					Intent.FLAG_ACTIVITY_NEW_TASK);
			lm.addProximityAlert(latitude_, longitude_, first_warn_radius_, 
					-1, first_warn_intent_);
			Log.d(VUphone.tag, pre + "Added proximity alert at ("+latitude_
					+", "+longitude_+") with radius "+first_warn_radius_);

			Intent toSend3 = new Intent(c, ActivityWarning.class);
			toSend3.putExtra(pack+"WarningAbout", "LandMine");
			toSend3.putExtra(pack+"WarningDistance", second_warn_radius_);
			second_warn_intent_ = PendingIntent.getActivity(c, 0, toSend3, 
					Intent.FLAG_ACTIVITY_NEW_TASK);
			lm.addProximityAlert(latitude_, longitude_, second_warn_radius_, 
					-1, second_warn_intent_);
			Log.d(VUphone.tag, pre + "Added proximity alert at ("+latitude_
					+", "+longitude_+") with radius "+second_warn_radius_);
		}

	}
	
	public void deactivate(Context c) {
		if (activated_) {
			activated_ = false;
			Log.i(VUphone.tag, pre + "Deactivating...");
			LocationManager lm = (LocationManager) 
					c.getSystemService(Context.LOCATION_SERVICE);
			lm.removeProximityAlert(kill_intent_);
			lm.removeProximityAlert(first_warn_intent_);
			lm.removeProximityAlert(second_warn_intent_);
			Log.i(VUphone.tag, pre + "Removed proximity alerts for id "+id_);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		
		boolean eq = false;
		if (o instanceof LandMine) {
			LandMine lmo = (LandMine) o;
			if (lmo.latitude_ == this.latitude_ && 
					lmo.longitude_ == this.longitude_ &&
					lmo.kill_radius_ == this.kill_radius_) {
				eq = true;
			}
		}
		return eq;
	}

	public int getId() {
		return id_;
	}

	public void setId(int id) {
		this.id_ = id;
	}

	public double getLatitude() {
		return latitude_;
	}

	public double getLongitude() {
		return longitude_;
	}
	
	public float getRadius() {
		return kill_radius_;
	}
}
