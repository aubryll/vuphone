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
package org.vuphone.assassins.gamearearequest;

/**
 * This class is intended to be the place where the server stores the
 * authoritative location of the playing area for the currently running
 * Assassins game.  It needs to exist the whole time the server is running
 * without being destroyed and re-created, which seems to be what happens
 * when you try to set it with the TestPoster.  Therefore, try to always
 * set it via the Godfather Activity on the device.
 * 
 * @author - Scott Campbell
 * 
 */
public class ServerGameArea {

	private double latitude_;
	private double longitude_;
	private float radius_;
	
	private static ServerGameArea instance_;
	
	private ServerGameArea() {
		latitude_ = 0.0;
		longitude_ = 0.0;
		radius_ = 0.0f;
	}
	
	public static ServerGameArea getInstance() {
		if (instance_ == null) {
			instance_ = new ServerGameArea();
		}
		return instance_;
	}
	
	// TODO - ensure that only the Godfather can call this method
	// when he is initially setting up the game area.
	public void setGameArea(double lat, double lon, float rad) {
		latitude_ = lat;
		System.out.println("Setting latitude_ to "+lat);
		longitude_ = lon;
		radius_ = rad;
	}

	public double getLatitude() {
		System.out.println("Returning latitude_ = "+latitude_);
		return latitude_;
	}

	public double getLongitude() {
		return longitude_;
	}

	public float getRadius() {
		return radius_;
	}

}
