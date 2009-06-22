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
import org.vuphone.wwatch.android.http.HTTPGetter;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

// TODO - this class needs to know if the zoom changed, so it can fire another get :P
public class AccidentMapView extends MapView {

	private PinOverlay pinGroup_;
	private static final String tag = VUphone.tag;
	private static final String pre = "AccidentMapView: ";
	private GeoPoint curCenter_ = null;
	private int viewHeight_;
	private int viewWidth_;
	private AccidentList routes_;

	public AccidentMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setBuiltInZoomControls(true);
		pinGroup_ = new PinOverlay(context, this);
		getOverlays().add(pinGroup_);
		viewHeight_ = getHeight();
		viewWidth_ = getWidth();
		routes_ = new AccidentList(this);
		curCenter_ = getMapCenter();
	}

	public PinOverlay getOverlay() {
		return pinGroup_;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		Log.v(tag, pre + "Compute Scroll called");
		
		if (getZoomLevel() > 5) {
			if (distanceChanged() > 20) {
				curCenter_ = getMapCenter();
				
				// This can change per draw, so we always get a fresh one
				Projection p = getProjection();

				GeoPoint upperRight = p.fromPixels(viewWidth_, viewHeight_);
				GeoPoint upperLeft = p.fromPixels(0, viewHeight_);
				GeoPoint lowerLeft = p.fromPixels(0, 0);
				GeoPoint lowerRight = p.fromPixels(viewWidth_, 0);

				long maxTime = routes_.getLatestTime();
				HTTPGetter.doAccidentGet(lowerLeft, lowerRight, upperLeft,
						upperRight, maxTime, routes_);
			}
		}

	}

	private float distanceChanged() {
		double startLat = curCenter_.getLatitudeE6() / 1.0E6;
		double startLng = curCenter_.getLongitudeE6() / 1.0E6;
		double endLat = getMapCenter().getLatitudeE6() / 1.0E6;
		double endLng = getMapCenter().getLongitudeE6() / 1.0E6;

		float[] results = new float[1];
		Location.distanceBetween(startLat, startLng, endLat, endLng, results);
		return results[0];
	}
}
