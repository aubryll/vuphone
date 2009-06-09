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
package org.vuphone.wwatch.routing;

import org.vuphone.wwatch.notification.Notification;

public class RouteNotification extends Notification {
	
	private Route route_;
	private String person_;
	
	public RouteNotification(){
		super("route");
		route_ = new Route();
	}
	
	public void setPerson(String aid){
		person_ = aid;
	}
	
	public String getPerson(){
		return person_;
	}
	
	public Route getRoute(){
		return route_;
	}
	
	public void addWaypoint(double lat, double lon, long time){
		route_.addWaypoint(lat, lon, time);
	}
	
	public void addWaypoint(Waypoint w){
		route_.addWaypoint(w);
	}

}
