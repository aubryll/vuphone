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

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.maps.MapView;

// TODO - this class needs to know if the zoom changed, so it can let the AccidentList know
public class AccidentMapView extends MapView {
	/** Used for logging */
	private static final String tag = VUphone.tag;
	private static final String pre = "AccidentMapView: ";

	private PinOverlay pinOverlay_;

	/** Used for callbacks */
	private Cache routes_;
	private boolean firedFirstScrollEvent_ = false;

	public AccidentMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setBuiltInZoomControls(true);
		pinOverlay_ = new PinOverlay(context, this);
		getOverlays().add(pinOverlay_);
		routes_ = new Cache(pinOverlay_, context);
	}

	public PinOverlay getOverlay() {
		return pinOverlay_;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();

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
