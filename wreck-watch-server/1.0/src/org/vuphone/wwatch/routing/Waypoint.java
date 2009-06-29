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

/**
 * A wrapper class that encapsulates vital information about a point along
 * a route. 
 * @author Krzysztof Zienkiewicz
 *
 */

public class Waypoint {
	private double longitude_ 	= 0;
	private double latitude_	= 0;
	private long timeStamp_		= 0;

	/**
	 * Construct a Waypoint from longitude, latitude, and time data.
	 * 
	 * @param lon	Longitude in degrees
	 * @param lat	Latitude in degrees
	 * @param time	UTC time in milliseconds since January 1, 1970. 
	 */
	public Waypoint(double lat, double lon, long time) {
		longitude_ = lon;
		latitude_ = lat;
		timeStamp_ = time;
	}

	/**
	 * Returns the latitude in degrees.
	 * @return
	 */
	public double getLatitude() {
		return latitude_;
	}

	/**
	 * Returns the longitude in degrees.
	 * @return
	 */
	public double getLongitude() {
		return longitude_;
	}

	/**
	 * Returns UTC time in milliseconds since January 1, 1970.
	 * @return
	 */
	public long getTime() {
		return timeStamp_;
	}

	/**
	 * Returns a human readable version of this Waypoint
	 * @return
	 */
	public String toString() {
		return "[" + longitude_ + ", " + latitude_ + ", " + timeStamp_ + "]";
	}

}


