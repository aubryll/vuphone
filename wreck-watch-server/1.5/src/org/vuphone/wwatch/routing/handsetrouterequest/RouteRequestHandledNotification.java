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
package org.vuphone.wwatch.routing.handsetrouterequest;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.routing.Route;
import org.vuphone.wwatch.routing.Waypoint;

public class RouteRequestHandledNotification extends Notification {
	
	private Route route_ = new Route();
	private String response_;
	
	public RouteRequestHandledNotification(){
		super("routerequesthandled");
	}
	
	public void addWaypoint(Waypoint w){
		route_.addWaypoint(w);
	}
	
	public void addWaypoint(double lat, double lon, long time){
		route_.addWaypoint(lat, lon, time);
	}
	
	public Route getRoute(){
		return route_;
	}
	
	@Override
	public String getResponseString(){
		return response_;
	}
	
	public void setResponse(String resp){
		response_ = resp;
	}

}
