// Copyright 2010 Michael Fagan

package org.vuphone.vandyupon.notification.diningpost;

import org.vuphone.vandyupon.notification.Notification;

// This class will be used to hold a Notification as it pertains to a Dining Rating.
// We will used this type for both a Request and a Call(Addition of a rating)
public class DiningPost extends Notification{

	// This variable is used to determine if this structure will be used for a "request" and a "rating"
	private String postType_;
	// This variable is for the integer mapped to the proper location
	private int loc_;
	// This variable is used to hold the rating to be added to the Database
	private int rating_;
	private int dbUserID_;
	private String sourceUserID_;
	
	// Default Ctor
	public DiningPost(){
		super("diningrating");
	}
	
	// Ctor for a Rating Call
	// Here we will take in both a location and a rating.
	// Since we have both, we will assume we a doing a rating call(we are adding a rating to the db)
	// Since we are making the above assumption, we will set the postType_ to "rating"
	public DiningPost(int location, int rating){
		super("diningrating");
		postType_ = "rating";
		loc_ = location;
		rating_ = rating;
	}
	
	// Ctor for a Rating Request
	// Here we will take in only a location
	// Since we have just location, we will assume we are doing a rating request(we are 
	// returning the average to the db)
	// Since we are making the above assumption, we will set the postType_ to "request"
	public DiningPost(int location){
		super("diningrating");
		postType_ = "request";
		loc_ = location;
		rating_ = -1;
	}
	
	// Gets the type
	public String getType(){
		return postType_;
	}
	
	// Gets the location
	public int getLocation(){
		return loc_;
	}
	
	// Gets the rating
	public int getRating(){
		return rating_;
	}
	
	public int getDBUserID(){
		return dbUserID_;
	}
	
	public String getSourceUserID(){
		return sourceUserID_;
	}
	
	// Sets the type
	public void setType(String type){
		postType_ = type;
	}
	
	// Sets the location
	public void setLocation(int location){
		loc_ = location;
	}
	
	// Sets the rating
	public void setRating(int rating){
		rating_ = rating;
	}
	
	public void setDBUserID(int dbUserID){
		dbUserID_ = dbUserID;
	}
	
	public void setSourceUserID(String sourceUserID){
		sourceUserID_ = sourceUserID;
	}
	
}
