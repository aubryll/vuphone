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

package edu.vanderbilt.vuphone.android.campusmaps;

import com.google.android.maps.GeoPoint;

public class Building {
	private GeoPoint point_;
	private String name_;
	private String desc_;
	private String url_ = null;

	public Building(GeoPoint point, String name) {
		point_ = point;
		name_ = name;
	}

	public GeoPoint getLocation() {
		return point_;
	}

	public String getName() {
		return name_;
	}

	public void setDescription(String desc) {
		desc_ = desc;
	}

	public String getDescription() {
		return desc_;
	}

	public void setImageURL(String url) {
		url_ = R.string.building_image_url + url.toLowerCase();
	}

	public String getImageURL() {
		return url_;
	}

	public String toString() {
		return getName();
	}
}
