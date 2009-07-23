/*******************************************************************************
 * Copyright 2009 Krzysztof Zienkiewicz
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

package org.vuphone.augmentedreality.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class AugmentedReality extends Activity {

	IntelligentDrawer drawer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Moved to XML
		 * // G1 camera does not support preview resizing so run in fullscreen
		 * // landscape mode to ensure proper aspect ratio.
		 * setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 * requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */

		ARView view_ = new ARView(this);
		drawer = new IntelligentDrawer(this); 
		view_.addDrawer(drawer);

		setContentView(view_);
	}
	
	@Override
	public boolean onTrackballEvent(MotionEvent e) {
		//drawer.azimuth += 5 * e.getY();
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ARSensors.getInstance(this).finish();
	}
}