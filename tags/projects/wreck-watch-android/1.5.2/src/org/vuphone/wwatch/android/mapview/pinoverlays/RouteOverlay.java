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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;


public class RouteOverlay extends ItemizedOverlay<Waypoint>{
	
	private static final String tag = "VUPHONE";
	private static final String pre = "RouteOverlay: ";

	private PinController pc_;
	public RouteOverlay(PinController pc){
		super(new ShapeDrawable(new OvalShape()));
		pc_ = pc;
		populate();
	}

	/**
	 * Allows this class to return items in any manner we would like, if we
	 * wanted to impose our own ordering on drawing. We do not, so we just
	 * return the item as we have them stored
	 */
	@Override
	protected Waypoint createItem(int i) {
		return pc_.getRouteItem(i);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mv, boolean shadow){
		try{
			super.draw(canvas, mv, shadow);
			
			Projection proj = mv.getProjection();
			Paint paint = new Paint();
			paint.setStrokeWidth(5);
			
			Point start = new Point();
			Point end = new Point();
			for (int i = 0; i < pc_.getRouteSize() - 1; ++i) {
				proj.toPixels(pc_.getRouteItem(i).getPoint(), start);
				proj.toPixels(pc_.getRouteItem(i + 1).getPoint(), end);
				
				canvas.drawLine(start.x, start.y, end.x, end.y, paint);
			}
			
		}catch (IndexOutOfBoundsException e) {
			Log.w(tag, pre + "IndexOutOfBoundsException drawing RouteOverlay");
			
		}
	}

	/**
	 * Informs us a tap event occurred on item with index index. First tap
	 * display's that wrecks route, and second tap will pop up the gallery
	 * activity
	 */
	@Override
	protected boolean onTap(int index) {
		Log.d(tag, pre + "onTap called with index " + index);
		return false;
	}
	

	/**
	 * Get the number of OverlayItem objects currently in this Overlay.
	 * 
	 * @return number of items.
	 */
	@Override
	public int size() {
		Log.i(tag, pre + "Size called, returning " + pc_.getWreckSize());
		return pc_.getRouteSize();
	}


	public void populatePins(){
		super.populate();
	}


}
