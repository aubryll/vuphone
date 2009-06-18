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

import java.util.ArrayList;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;

import android.util.Log;

public class Route {

	private ArrayList<Waypoint> route_ = new ArrayList<Waypoint>();
	private int curIndex_;

	public Route() {

	}

	public void addWaypoint(Waypoint w) {
		route_.add(w);
	}

	public ArrayList<Waypoint> getRoute() {
		return route_;
	}

	public void addWaypoint(double lat, double lon, long time) {
		route_.add(new Waypoint(lat, lon, time));
	}

	/**
	 * This method will return a specific position along the route. It will NOT
	 * increment the location.
	 * 
	 * @param index
	 * @return
	 */
	public Waypoint getPoint(int index) {
		return route_.get(index);
	}

	public Waypoint getEndPoint() {
		if (route_.size() > 0)
			return route_.get(route_.size() - 1);
		
		Log.w(VUphone.tag, "Route: returning null end point, because route size is zero");
		return null;
	}

	/**
	 * This method is used to incrementally retrieve positions along a route.
	 * Sequential calls to this method will return points in the order they were
	 * added.
	 * 
	 * For instance, to replay a route for a user sequential calls to this
	 * method would achieve the correct progression of Waypoint objects.
	 * 
	 * All logic required to keep track of which point should be retrieved next
	 * is handled internally by the Route class.
	 * 
	 * @return Waypoint - The Waypoint object representing the current location
	 *         in the list. Will return a null value if we have reached the end.
	 */
	public Waypoint getNextPoint() {
		if (curIndex_ >= route_.size()) {
			return null;
		} else {
			return route_.get(curIndex_++);
		}

	}

	/**
	 * This method will reset the internal counter that keeps track of the
	 * current position. For example it could be used to allow you to replay a
	 * given route multiple times
	 */
	public void reset() {
		curIndex_ = 0;
	}

	/**
	 * This method returns the size of the route.
	 * 
	 * @return int
	 */
	public int getSize() {
		return route_.size();
	}

	/**
	 * This method returns the next Waypoint element in the list, but does not
	 * increment the internal counter. It will return null if we have reached
	 * the end of the list.
	 * 
	 * @return Waypoint
	 */
	public Waypoint peek() {
		if (curIndex_ >= route_.size()) {
			return null;
		} else {
			return route_.get(curIndex_);
		}
	}

}
