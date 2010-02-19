// Copyright 2010 Michael Fagan

package org.vuphone.vandyupon.notification.diningrating;

import org.vuphone.vandyupon.notification.Notification;

// This class will be used to hold a Notification as it pertains to a Dining Rating.
public class DiningRating extends Notification{

	// This variable is for the integer mapped to the proper location
	private int loc_;
	// This variable is used to hold the rating to be added to the Database
	private int rating_;
	// This variable is used to hold the ID of the device
	private String deviceID_;
	
	// Default Ctor
	public DiningRating(){
		super("diningrating");
	}
	
	// Ctor for a Rating Call
	// Here we will take in a location, a rating, and a device ID.
	public DiningRating(int location, int rating, String deviceID){
		super("diningrating");
		loc_ = location;
		rating_ = rating;
		deviceID_ = deviceID;
	}
	
	// Gets the location
	public int getLocation(){
		return loc_;
	}
	
	// Gets the rating
	public int getRating(){
		return rating_;
	}
	
	// Gets the Device ID
	public String getDeviceID(){
		return deviceID_;
	}
	
	// Sets the location
	public void setLocation(int location){
		loc_ = location;
	}
	
	// Sets the rating
	public void setRating(int rating){
		rating_ = rating;
	}
	
	// Sets the Device ID
	public void setDeviceID(String deviceID){
		deviceID_ = deviceID;
	}
	
}
