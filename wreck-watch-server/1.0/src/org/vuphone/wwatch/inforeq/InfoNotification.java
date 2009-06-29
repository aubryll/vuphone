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

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.routing.Waypoint;

/**
 * This class is utilized by the WreckWatch server to store information
 * regarding the area for within which to search for accidents. The area is
 * represented as the vertices of a rectangle and are stored internally in an
 * array. The indices of that array are 0******1 * * * * * * * * 2******3 Each
 * point is accessed by a method corresponding to its position rather than by
 * its index within the array.
 * 
 * @author Chris Thompson
 * 
 */
public class InfoNotification extends Notification {

	private Waypoint topLeft_;
	private Waypoint bottomRight_;

	private long time_;

	public InfoNotification() {
		super("info");
	}

	public void setTime(long t) {
		time_ = t;
	}

	public long getTime() {
		return time_;
	}

	public void setTopLeftCorner(double lat, double lon) {
		topLeft_ = new Waypoint(lat, lon, 0);
	}

	public void setBottomRightCorner(double lat, double lon) {
		bottomRight_ = new Waypoint(lat, lon, 0);
	}

	/**
	 * In general (not counting the meridian or the equator) latitude more North
	 * is larger, while longitude more East is larger
	 * 
	 * @return
	 */
	public double getMaxLatitude() {
		return topLeft_.getLatitude();
	}

	public double getMinLatitude() {
		return bottomRight_.getLatitude();
	}

	public Double getMaxLongitude() {
		return bottomRight_.getLongitude();
	}

	public Double getMinLongitude() {
		return topLeft_.getLongitude();
	}

}
