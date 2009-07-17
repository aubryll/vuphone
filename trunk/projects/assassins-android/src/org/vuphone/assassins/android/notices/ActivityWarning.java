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
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ActivityWarning extends Activity{
	
	private String pre = "ActivityWarning: ";
	
	private TextView warnMessage_;
	
	/**
	 * The "constructor". Sets up the fields and the layout. This is either
	 * called because somebody requested this activity to start up or because
	 * the activity is being reloaded due to a keyboard flip.
	 */
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.warn);
		Log.d(VUphone.tag, pre + "onCreate called.");
		
		warnMessage_ = (TextView) findViewById(R.id.warnMessage);
		
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String warnMethod = "";
		if (!extras.containsKey("org.vuphone.assassins.android.WarningAbout")) {
			Log.e(VUphone.tag, pre + "Calling Intent does not have" +
					" the WarningAbout extra.  This is required to properly" +
					" display a warning to the user.");
			warnMethod = "unknown";
		}
		else {
			warnMethod = extras.getString(
					"org.vuphone.assassins.android.WarningAbout");
		}
		
		if (warnMethod.equalsIgnoreCase("LandMine")) {
			float dist = extras.getFloat(
					"org.vuphone.assassins.android.WarningDistance");
			warnMessage_.append("In the Assassins game, you are within "
					+ dist + " meters of a land mine!  " 
					+"You should probably turn around before you get killed!");
		}
		// Add more warn methods here as you develop them.
		else {
			warnMessage_.append("Something unknown is menacing you...");
		}

	}

}
