/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Adam Albright
 * @date Oct 16, 2009
 * 
 * Copyright 2009 VUPhone Team
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 *  implied. See the License for the specific language governing 
 *  permissions and limitations under the License. 
 */

package edu.vanderbilt.vuphone.android.campusmaps.storage;

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.campusmaps.R;

public class Building implements Comparable<Building> {
	private GeoPoint point_ = null;
	private String name_ = null;
	private String desc_ = null;
	private String url_ = null;
	private long id_ = 0;

	public Building(long id, GeoPoint gp, String name, String desc, String url) {
		id_ = id;
		point_ = gp;
		name_ = name;
		desc_ = desc;
		url_ = url;
	}

	public GeoPoint getLocation() {
		return point_;
	}

	public String getName() {
		return name_;
	}

	public String getDescription() {
		return desc_;
	}

	public String getImageURL() {
		return url_;
	}

	public long getID() {
		return id_;
	}

	public void setDescription(String desc) {
		desc_ = desc;
	}

	public void setImageURL(String url) {
		url_ = R.string.building_image_url + url.toLowerCase();
	}

	public String toString() {
		return getName();
	}

	public int compareTo(Building another) {
		return getName().compareTo(another.getName());
	}

}
