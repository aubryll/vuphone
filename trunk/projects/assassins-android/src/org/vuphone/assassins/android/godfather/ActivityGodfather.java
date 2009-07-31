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
package org.vuphone.assassins.android.godfather;

import org.vuphone.assassins.android.R;
import org.vuphone.assassins.android.VUphone;
import org.vuphone.assassins.android.http.HTTPPoster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * This Activity is the main launcher activity for the Godfather, whose
 * job is to create the game area and invite the players who will 
 * praticipate in the game.
 * 
 * @author Scott Campbell
 */
public class ActivityGodfather extends Activity{

	private RadioButton fgh_;
	private RadioButton isis_;
	private RadioButton other_;
	private EditText input_radius_;
	private Button submit_;
	
	private double latitude_;
	private double longitude_;
	
	static final int CUSTOM_CENTRAL_POINT = 0;
	
	private String pre = "ActivityGodfather: ";
	
	/**
	 * The "constructor". Sets up the fields and the layout. This is either
	 * called because somebody requested this activity to start up or because
	 * the activity is being reloaded due to a keyboard flip.
	 */
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.godfather);
		
		fgh_ = (RadioButton) findViewById(R.id.godfather_radio_fgh);
		isis_ = (RadioButton) findViewById(R.id.godfather_radio_isis);
		other_ = (RadioButton) findViewById(R.id.godfather_radio_other);
		input_radius_ = (EditText) findViewById(R.id.godfather_edit_radius);
		submit_ = (Button) findViewById(R.id.godfather_submit);
		
		fgh_.setOnClickListener(radio_listener_);
		isis_.setOnClickListener(radio_listener_);
		other_.setOnClickListener(radio_listener_);
		submit_.setOnClickListener(submit_listener_);
	}
	
	OnClickListener radio_listener_ = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i(VUphone.tag, pre + "Entered the radio_listenter_.onClick method.");
			RadioButton rb = (RadioButton) v;
			if (((String)rb.getText()).equalsIgnoreCase("Featheringill Hall")) {
				// This is the exact lat lon for FGH, according to itouchmap.com
				latitude_ = 36.144754117468786;
				longitude_ = -86.80315017700195; 
				Log.i(VUphone.tag, pre + "set the lat lon for FGH.");
			}
			else if (((String)rb.getText()).equalsIgnoreCase("ISIS")) {
				// This is the exact lat lon for ISIS, according to itouchmap.com
				latitude_ = 36.14957107053674;
				longitude_ = -86.79964184761047;
				Log.i(VUphone.tag, pre + "set the lat lon for ISIS.");
			}
			else if (((String)rb.getText()).equalsIgnoreCase("Other")) {
				Intent i = new Intent(ActivityGodfather.this, ActivityEnterCentralPoint.class);
				startActivityForResult(i, CUSTOM_CENTRAL_POINT);
			}
		}
		
	};
	
	OnClickListener submit_listener_ = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new Thread(new Runnable() {
				public void run() {
					HTTPPoster.doGameAreaPost(latitude_, longitude_, 
							Float.valueOf(input_radius_.getText().toString()));
				}
			}, "GameAreaPoster").start();
			Intent i = new Intent(ActivityGodfather.this, 
					ActivityChoosePlayers.class);
			startActivity(i);
			finish();
			
		}
		
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CUSTOM_CENTRAL_POINT) {
			if (data != null) {
				latitude_ = data.getDoubleExtra("Latitude", 0.0);
				longitude_ = data.getDoubleExtra("Longitude", 0.0);
				Log.i(VUphone.tag, pre + "set lat lon for custom point ("+
						latitude_+", "+longitude_+").");
			}
			else {
				// The custom lat lon were not set, so we will assume FGH
				// as a default.
				fgh_.setChecked(true);
				latitude_ = 36.144754117468786;
				longitude_ = -86.80315017700195; 
				Log.i(VUphone.tag, pre + "set the lat lon for FGH.");
			}
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}
