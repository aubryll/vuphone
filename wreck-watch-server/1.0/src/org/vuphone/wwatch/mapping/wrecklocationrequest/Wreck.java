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
package org.vuphone.wwatch.mapping.wrecklocationrequest;

import org.vuphone.wwatch.routing.Waypoint;

public class Wreck {
	
	private Waypoint location_;
	private int id_;
	
	public Wreck(){
	}
	
	public Wreck(Waypoint w, int id){
		location_ = w;
		id_ = id;
	}
	
	public void setLocation(double lat, double lon, long time){
		location_ = new Waypoint(lat, lon, time);
	}
	
	public void setLocation(Waypoint w){
		location_ = w;
	}
	
	public void setId(int id){
		id_ = id;
	}
	
	public Waypoint getLocation(){
		return location_;
	}
	
	public int getId(){
		return id_;
	}

}
