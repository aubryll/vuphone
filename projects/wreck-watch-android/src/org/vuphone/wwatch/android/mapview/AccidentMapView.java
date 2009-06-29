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
package org.vuphone.wwatch.android.mapview;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.mapview.pinoverlays.PinController;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.maps.MapView;

// TODO - this class needs to know if the zoom changed, so it can let the AccidentList know
public class AccidentMapView extends MapView {
	/** Used for logging */
	

	private PinController pinGroup_;
	private static final String tag = VUphone.tag;
	private static final String pre = "AccidentMapView: ";

	/** Used for callbacks */
	private Cache routes_;
	private boolean firedFirstScrollEvent_ = false;

	public AccidentMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setBuiltInZoomControls(true);
		pinGroup_ = new PinController(this, context);
		getOverlays().add(pinGroup_.getRouteOverlay());
		getOverlays().add(pinGroup_.getWreckOverlay());
		
		routes_ = new Cache(pinGroup_, context);
		
		pinGroup_.setCache(routes_);
	}
	
	public void startCache() {
		routes_.start();
	}
	
	public void stopCache() {
		routes_.stop();
	}

	public PinController getOverlayController() {
		return pinGroup_;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		Log.v(tag, pre + "Compute Scroll called");

		// The first event seems to come before the map is fully rendered, and
		// the second event typically triggers an update on the initial data, so
		// we might as just wait until the first event has passed to load
		// initial data
		if (firedFirstScrollEvent_)
			routes_.onMapScroll(this);
		else
			firedFirstScrollEvent_ = true;
	}
}
