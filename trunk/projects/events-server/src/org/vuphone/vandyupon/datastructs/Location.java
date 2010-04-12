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
package org.vuphone.vandyupon.datastructs;

public class Location {

	private int locationid_;
	private String name_;
	private double lat_;
	private double lon_;

	public Location() {

	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Location) {
			Location otherLoc = (Location) o;
			if ((otherLoc.getLat() != getLat())
					|| (otherLoc.getLon() != getLon())
					|| (otherLoc.getName().equalsIgnoreCase(getName()) == false)
					|| (otherLoc.getLocationid() != getLocationid()))
				return false;
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int)(getLat() * getLon()) + locationid_;
	}

	public Location(double lat, double lon) {
		lat_ = lat;
		lon_ = lon;
	}

	public Location(String name, double lat, double lon) {
		name_ = name;
		lat_ = lat;
		lon_ = lon;
	}

	public Location(int locationid, String name, double lat, double lon) {
		locationid_ = locationid;
		name_ = name;
		lat_ = lat;
		lon_ = lon;
	}

	/**
	 * @return the id
	 */
	public int getLocationid() {
		return locationid_;
	}

	/**
	 * @return the name_
	 */
	public String getName() {
		return name_;
	}

	/**
	 * @return the lat_
	 */
	public double getLat() {
		return lat_;
	}

	/**
	 * @return the lon_
	 */
	public double getLon() {
		return lon_;
	}

	/**
	 * @param name
	 *            the name_ to set
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * @param lat
	 *            the lat_ to set
	 */
	public void setLat(double lat) {
		lat_ = lat;
	}

	/**
	 * @param lon
	 *            the lon_ to set
	 */
	public void setLon(double lon) {
		lon_ = lon;
	}

}
