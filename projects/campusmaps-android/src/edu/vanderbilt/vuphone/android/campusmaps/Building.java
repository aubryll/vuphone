package edu.vanderbilt.vuphone.android.campusmaps;

import com.google.android.maps.GeoPoint;

public class Building {
	private GeoPoint point_;
	private String name_;
	private String desc_ = "";
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
		url_ = "http://www.vanderbilt.edu/map/" + url.toLowerCase();
	}

	public String getImageURL() {
		return url_;
	}

	public String toString() {
		return getName();
	}
}
