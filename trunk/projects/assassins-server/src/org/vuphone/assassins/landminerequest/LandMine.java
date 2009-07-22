/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.landminerequest;

/**
 * This is the server side land mine class.  It's sole purpose is 
 * to store the lat, lon, and radius information for a specific 
 * land mine in one location so that it is easy to tell which lat, 
 * lon, and radius go with each other.  It will never be
 * activated or deactivated like the client side version will be
 * so it does not need those methods.
 * 
 * @author Scott Campbell
 */
public class LandMine {

	private double latitude_;
	private double longitude_;
	private double radius_;
	
	public LandMine(double lat, double lon, double radius) {
		latitude_ = lat;
		longitude_ = lon;
		radius_ = radius;
	}

	public double getLatitude() {
		return latitude_;
	}

	public void setLatitude(double latitude) {
		this.latitude_ = latitude;
	}

	public double getLongitude() {
		return longitude_;
	}

	public void setLongitude(double longitude) {
		this.longitude_ = longitude;
	}

	public double getRadius() {
		return radius_;
	}

	public void setRadius(double radius) {
		this.radius_ = radius;
	}
}
