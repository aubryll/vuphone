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
package org.vuphone.assassins.landmineremove;

import org.vuphone.assassins.notification.Notification;

/**
 * This class represents a container for information related to a land mine
 * notification.
 * 
 * @author Chris Thompson
 * 
 */
public class LandMineRemoveNotification extends Notification {

	private double lat_;
	private double lon_;
	private double radius_;

	public LandMineRemoveNotification() {
		super("landMineRemove");

	}

	public void setLatitude(double lat) {
		lat_ = lat;
	}

	public void setLongitude(double lon) {
		lon_ = lon;
	}

	public double getLatitude() {
		return lat_;
	}

	public double getLongitude() {
		return lon_;
	}

	public double getRadius() {
		return radius_;
	}

	public void setRadius(double radius) {
		this.radius_ = radius;
	}

	/** Simple Override allowing us to print out nice messages */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Lat: " + lat_ + "\n Lon: " + lon_ + "\n Radius: " 
				+ radius_);
		
		return sb.toString();

	}

	/**
	 * Returns the string that this notification would like to be returned in
	 * the Http reply
	 * 
	 * @see org.vuphone.wwatch.notification#getResponseString()
	 */
	@Override
	public String getResponseString() {
		return "";
	}


}
