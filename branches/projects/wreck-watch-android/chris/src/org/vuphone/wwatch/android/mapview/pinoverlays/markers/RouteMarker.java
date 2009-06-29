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
package org.vuphone.wwatch.android.mapview.pinoverlays.markers;

import org.vuphone.wwatch.android.Waypoint;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;

import com.google.android.maps.GeoPoint;

public class RouteMarker extends Waypoint {
	
	public RouteMarker(double lat, double lon, long time) {
		super(lat, lon, time);
	}
	
	public RouteMarker(GeoPoint p, long time){
		super(p, time);
	}
	
	public RouteMarker(Location loc){
		super(loc);
	}
	
	public RouteMarker(Waypoint w){
		super(w.getPoint(), w.getTime());
	}

	@Override
	public Drawable getMarker(int statebit){
		Drawable temp = new ShapeDrawable(new OvalShape());
		temp.setBounds(new Rect(0, 0, 5, 5));
		
		return temp;
	}

}
