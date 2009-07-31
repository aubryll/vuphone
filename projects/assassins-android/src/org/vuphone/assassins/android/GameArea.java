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

import java.util.HashMap;

import org.vuphone.assassins.android.http.HTTPGetter;
import org.vuphone.assassins.android.notices.ActivityGameEntry;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

/**
 * This is the playing area within which all the action of the game
 * will occur.
 * 
 * @author Scott Campbell
 */
public class GameArea implements MarkedArea{

	private static double latitude_;
	private static double longitude_;
	private static float radius_;
	private PendingIntent intent_;
	private boolean activated_ = false;
	
	private static String pre = "GameArea: ";
	
	private static GameArea instance_;
	
	private GameArea() {}
	
	public static GameArea getInstance() {
		if (instance_ == null) {
			instance_ = new GameArea();
			// I really should use some other object that is more intuitive
			// than this simple map, but this will do the job for now...
			HashMap<String, Double> map = HTTPGetter.doGameAreaGet();
			latitude_ = map.get("Latitude");
			longitude_ = map.get("Longitude");
			radius_ = map.get("Radius").floatValue();
			Log.i(VUphone.tag, pre + "The game area has been set to ("+latitude_+", "+longitude_+") with radius "+radius_);
		}
		return instance_;
	}
	
	public void activate(Context c) {
		if (!activated_) {
			activated_ = true;
			Log.d(VUphone.tag, pre + "GameArea activated!");
			
			Intent toSend = new Intent(c, ActivityGameEntry.class);
			intent_ = PendingIntent.getActivity(c, 0, toSend, 
					Intent.FLAG_ACTIVITY_NEW_TASK);
			LocationManager lm = (LocationManager) 
					c.getSystemService(Context.LOCATION_SERVICE);
			lm.addProximityAlert(latitude_, longitude_, radius_, -1, intent_);
			Log.d(VUphone.tag, pre + "Added proximity alert for Game Area.");
		}
	}
	
	public void deactivate(Context c) {
		if (activated_) {
			activated_ = false;
			Log.i(VUphone.tag, pre + "Deactivating Game Area...");
			LocationManager lm = (LocationManager) 
					c.getSystemService(Context.LOCATION_SERVICE);
			lm.removeProximityAlert(intent_);
			Log.i(VUphone.tag, pre + "Removed proximity alert for Game Area.");
		}
	}

	public double getLatitude() {
		return latitude_;
	}

	public double getLongitude() {
		return longitude_;
	}

	public float getRadius() {
		return radius_;
	}

}
