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

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.campusmaps.R;

public class Building {
	private GeoPoint point_ = new GeoPoint(36142830, -86804437);
	private String name_ = "error";
	private String desc_ = null;
	private String url_ = null;

	public Building(GeoPoint point, String name) {
		point_ = point;
		name_ = name;
	}

	public Building(int latitude, int longitude, String name, String desc,
			String url) {
		point_ = new GeoPoint(latitude, longitude);
		name_ = name;
		desc_ = desc;
		url_ = url;
	}

	public Building(GeoPoint gp, String name, String desc, String url) {
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

	public boolean create() {
		return DBWrapper.create(this);
	}

	public static ArrayList<Long> getIDs() {
		return DBWrapper.getIDs();
	}

	public static Building get(long rowID) {
		return DBWrapper.get(rowID);
	}

	public static String getName(long rowID) {
		return DBWrapper.getName(rowID);
	}

	public static double getLat(long rowID) {
		return DBWrapper.getLat(rowID);
	}

	public static double getLon(long rowID) {
		return DBWrapper.getLon(rowID);
	}

	public static String getDesc(long rowID) {
		return DBWrapper.getDesc(rowID);
	}

	public static String getImageURL(long rowID) {
		return DBWrapper.getURL(rowID);
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

	public static boolean create(Building b) {
		return DBWrapper.create(b);
	}

	public static boolean update(long rowID, Building b) {
		return DBWrapper.update(rowID, b);
	}

	public static boolean delete(long rowID) {
		return DBWrapper.delete(rowID);
	}

}
