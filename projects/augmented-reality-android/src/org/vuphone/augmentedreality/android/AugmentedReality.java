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

public class AugmentedReality extends Activity {

	private ARView view_;
	private IntelligentDrawer drawer_;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		view_ = new ARView(this);
		
		setContentView(view_);
		
		drawer_ = new IntelligentDrawer(this); 
		view_.addDrawer(drawer_);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		view_.destroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		view_.pause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		view_.resume();
	}
}