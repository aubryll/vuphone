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

	private double lat_;
	private double lon_;
	private String name_;
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
	
	public String getName(){
		return name_;
	}
	/**
	 * @param lat the lat_ to set
	 */
	public void setLat(double lat) {
		lat_ = lat;
	}
	/**
	 * @param lon the lon_ to set
	 */
	public void setLon(double lon) {
		lon_ = lon;
	}
	
	public void setName(String name){
		name_ = name;
	}
	
	
}
