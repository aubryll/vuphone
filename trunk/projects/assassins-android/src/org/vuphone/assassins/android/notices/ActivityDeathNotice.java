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

import org.vuphone.assassins.android.GameObjects;
import org.vuphone.assassins.android.R;
import org.vuphone.assassins.android.VUphone;
import org.vuphone.assassins.android.http.HTTPPoster;
import org.vuphone.assassins.android.landmine.LandMine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * This Activity should be displayed to the user whenever the die by any
 * means possible in the game.  It has a TextView that will tell them
 * something about how they died.  Right now, only deaths by land mine are
 * supported.
 * 
 * @author Scott Campbell
 */
public class ActivityDeathNotice extends Activity{
	
	private String pre = "ActivityDeathNotice: ";
	
	private TextView killMessage_;
	
	/**
	 * The "constructor". Sets up the fields and the layout. This is either
	 * called because somebody requested this activity to start up or because
	 * the activity is being reloaded due to a keyboard flip.
	 */
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.death);
		Log.d(VUphone.tag, pre + "onCreate called.");
		
		killMessage_ = (TextView) findViewById(R.id.killMessage);
		
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		String killMethod = "";
		if (!extras.containsKey("org.vuphone.assassins.android.KillMethod")) {
			Log.e(VUphone.tag, pre + "Calling Intent does not have" +
					" the KillMethod extra.  This is required in order" +
					" to properly display a message to the user about" +
					" their death.");
			killMethod = "unknown";
		}
		else {
			killMethod = extras.getString("org.vuphone.assassins.android.KillMethod");
		}
		
		if (killMethod.equalsIgnoreCase("LandMine")) {
			killMessage_.append("You were blown up by a land mine!");
			
			int id = extras.getInt("org.vuphone.assassins.android.Id");
			final LandMine lm = GameObjects.getInstance().getLandMine(id);
			
			new Thread(new Runnable() {
				public void run() {
					HTTPPoster.doLandMineRemove(lm);
				}
			}, "LandMineRemover").start();
			
			GameObjects.getInstance().removeLandMine(id, this);

		}
		else if (killMethod.equalsIgnoreCase("Sniper")) {
			killMessage_.append("You were shot by a sniper!");
		}
		// Add more kill methods here as you develop them.
		else {
			killMessage_.append("You were killed by some unknown method...");
		}
		
		removeFromGame();
	}
	
	public void removeFromGame() {
		//TODO - add the necessary code here to un-register the user from
		//the game and alert and centralized server of the kill.
	}

}
