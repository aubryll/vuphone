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
package org.vuphone.assassins.android.notices;

import org.vuphone.assassins.android.R;
import org.vuphone.assassins.android.VUphone;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * This is the activity that will be displayed whenever the proximity
 * alert for entering or leaving the game area is fired.
 * 
 * @author Scott Campbell
 */
public class ActivityGameEntry extends Activity implements OnClickListener{
	
	private String pre = "ActivityGameEntry: ";
	
	private TextView title_;
	private TextView bigMessage_;
	private Button okayButton_;
	private TextView smallMessage_;
	
	/**
	 * The "constructor". Sets up the fields and the layout. This is either
	 * called because somebody requested this activity to start up or because
	 * the activity is being reloaded due to a keyboard flip.
	 */
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.game_entry);
		Log.d(VUphone.tag, pre + "onCreate called.");
		
		okayButton_ = (Button) findViewById(R.id.entry_ok_button);
		okayButton_.setOnClickListener(this);
		
		title_ = (TextView) findViewById(R.id.entry_big_title);
		bigMessage_ = (TextView) findViewById(R.id.entry_main_message);
		smallMessage_ = (TextView) findViewById(R.id.entry_small_message);
	}

	@Override
	protected void onStart() {
	
		super.onStart();
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (!extras.containsKey(LocationManager.KEY_PROXIMITY_ENTERING)) {
			Log.e(VUphone.tag, pre + "Somehow, this activity got started " +
					"from an intent that doesn't contain the " +
					"KEY_PROXIMITY_ENTERING flag, meaning it did not come " +
					"from a proximity alert.  This should never happen.  " +
					"Exiting.");
			return;
		}
		
		boolean entering = i.getBooleanExtra(
				LocationManager.KEY_PROXIMITY_ENTERING, true);
		
		if (!entering) {
			title_.setText("Good Bye");
			bigMessage_.setText("You have left the playing area of the " +
					"Assassins game.");
			smallMessage_.setText("Re-enter the playing area in order to " +
					"continue playing.");
			VUphone.IN_GAME_AREA = false;
		}
		else {
			VUphone.IN_GAME_AREA = true;
		}
	}

	@Override
	public void onClick(View view) {
		
		finish();
	}
}
