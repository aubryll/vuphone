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
package org.vuphone.wwatch.inforeq;

import java.util.ArrayList;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.routing.Waypoint;

public class InfoHandledNotification extends Notification {
	
	/*
	 * This will use a static array because the number of accidents
	 * will be known at time of construction thanks to the knowledge 
	 * of the number of row returned from the SQL query.
	 */
	private ArrayList<Waypoint> accidents_;
	
	

	public InfoHandledNotification() {
		super("infohandled");
		accidents_ = new ArrayList<Waypoint>();
		
	}
	
	public void addWaypoint(double lat, double lon){
		accidents_.add(new Waypoint(lat, lon, 0));
		
	}
	
	public void addWaypoint(Waypoint w){
		accidents_.add(w);
	}
	
	public ArrayList<Waypoint> getAccidents(){
		return accidents_;
	}
	
	
	

}
