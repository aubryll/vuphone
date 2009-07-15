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

import org.vuphone.assassins.android.GameObjects;
import org.vuphone.assassins.android.R;
import org.vuphone.assassins.android.VUphone;
import org.vuphone.assassins.android.http.HTTPPoster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityCreateLandMine extends Activity implements OnClickListener{
	
	Button createMine_;
	TextView activatedMessage_;
	
	Location location_ = null;
	
	private static final float GPS_RADIUS = 0f;
	private static final long GPS_FREQUENCY = 500;
	// 6 seems to be a common accuracy, so we'll accept those to make
	// testing easier.
	private static final float MIN_ACCURACY = 7;
	
	private String pre = "ActivityCreateLandMine: ";
	
	/**
	 * The LocationListener used to receive GPS updates
	 */
	private final LocationListener listener_ = new LocationListener() {
		public void onLocationChanged(Location location) {
			if (location.hasAccuracy() && location.getAccuracy() > MIN_ACCURACY) {
				Log.d(VUphone.tag, pre + "location rejected because accuracy = "
						+location.getAccuracy());
				return;
			}
			if (!location.hasAccuracy()) {
				Log.d(VUphone.tag, pre + "location rejected because it has no accuracy.");
				return;
			}
			location_ = location;
			Log.d(VUphone.tag, pre + "location_ has been set to ("+
					location_.getLatitude()+", "+location_.getLongitude()
					+").");
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.create_mine);
		Log.d(VUphone.tag, pre + "onCreate called.");
		
		createMine_ = (Button) findViewById(R.id.create_land_mine);
		createMine_.setOnClickListener(this);
		
		activatedMessage_ = (TextView) findViewById(R.id.activated_message);
		
		/**
		 * The following code needs to be called to start the updater
		 * Service whenever the application is first started.  Right
		 * now, this is the main launcher activity, but if that changes
		 * this code will need to go in the new main launcher activity.
		 */
		Intent i = new Intent(this, 
				org.vuphone.assassins.android.ServicePeriodicUpdate.class);
		startService(i);
	}

	@Override
	public void onClick(View view) {
		LocationManager lm = (LocationManager) 
				getSystemService(Context.LOCATION_SERVICE);
		//Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_FREQUENCY,
				GPS_RADIUS, listener_);

		if (location_ != null) {
			Log.d(VUphone.tag, pre + "About to create a land mine...");
			LandMine mine = new LandMine(location_.getLatitude(), 
					location_.getLongitude(), (float) 5.0);
			//mine.activate(this);
			GameObjects.getInstance().addLandMine(mine, this);
			HTTPPoster.doLandMinePost(mine);

			createMine_.setVisibility(View.INVISIBLE);
			String msg = "You have successfully created a land mine.  " +
					"It will become activated in "+LandMine.MAX_TIME+
					" seconds, so you better leave the area by then!";
			activatedMessage_.setText(msg);
			activatedMessage_.setVisibility(View.VISIBLE);
		}
		else {
			Log.e(VUphone.tag, pre + "location_ was null after requesting updates.");
			String msg = "The GPS satellites cannot get a lock on your " +
					"current position, so it is impossible to create a " +
					"land mine here.  Try going outside or moving to a " +
					"location with a better view of the sky.";
			activatedMessage_.setText(msg);
			activatedMessage_.setVisibility(View.VISIBLE);
		}
	}

}
