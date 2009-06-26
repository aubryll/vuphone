 /**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.wwatch.android.mapview.pinoverlays;

import org.vuphone.wwatch.android.Waypoint;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;


public class RouteOverlay extends ItemizedOverlay<Waypoint>{
	
	private static final String TAG = "VUPHONE";
	private static final String PREFIX = "RouteOverlay: ";

	private PinController pc_;
	public RouteOverlay(PinController pc){
		super(new ShapeDrawable(new OvalShape()));
		pc_ = pc;
		
	}

	@Override
	protected Waypoint createItem(int i) {
		return pc_.getRouteItem(i);
		//return new Waypoint(0.0, 0.0, System.currentTimeMillis());
	}

	@Override
	public int size() {
		return pc_.getRouteSize();
		//return 1;
	}
	
	public void populateRoutes(){
		super.populate();
	}
	
	@Override
	public boolean onTap(int e){
		Log.d(TAG, PREFIX + "onTap");
		return false;
	}

}
