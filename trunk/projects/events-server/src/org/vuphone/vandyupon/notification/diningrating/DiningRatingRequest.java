// Copyright 2010 Michael Fagan

package org.vuphone.vandyupon.notification.diningrating;

import org.vuphone.vandyupon.notification.Notification;

// This class will be used to hold a Notification as it pertains to a Dining Rating Request.
// It will be used to handle a request for a Rating at a particular location
public class DiningRatingRequest extends Notification{

	// This variable is for the integer mapped to the proper location
	private int loc_;
	// This variable is used to hold the ID of the device
	private String deviceID_;
	
	// Default Ctor
	public DiningRatingRequest(){
		super("diningratingrequest");
	}
	
	// Ctor for a Rating Request
	// Here we will take in only a location
	public DiningRatingRequest(int location, String deviceID){
		super("diningratingrequest");
		loc_ = location;
		deviceID_ = deviceID;
	}
	
	// Gets the location
	public int getLocation(){
		return loc_;
	}
	
	// Gets the Device ID
	public String getDeviceID(){
		return deviceID_;
	}
	
	// Sets the location
	public void setLocation(int location){
		loc_ = location;
	}
	
	// Sets the Device ID
	public void setDeviceID(String deviceID){
		deviceID_ = deviceID;
	}
	
}
