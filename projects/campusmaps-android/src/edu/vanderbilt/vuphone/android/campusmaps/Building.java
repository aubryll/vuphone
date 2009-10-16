package edu.vanderbilt.vuphone.android.campusmaps;

import com.google.android.maps.GeoPoint;

public class Building {
	private GeoPoint point_;
	private String name_;
	
	public Building(GeoPoint point, String name){
		point_ = point;
		name_ = name;
	}
	
	public GeoPoint getLocation(){
		return point_;
	}
	
	public String getName(){
		return name_;
	}
	
	public String toString(){
		return getName();
	}
}
