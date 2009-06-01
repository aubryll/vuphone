package org.vuphone.wwatch.android;

import android.location.Location;

/**
 * A class that contains data concerning the change between
 * two waypoints. 
 * @author Krzysztof Zienkiewicz
 *
 */
public class ChangeData {
	private float distance_	= 0;	// meters
	private double speed_		= 0;	// Real speed in meters/second
	private long time_			= 0;	// Real time in milliseconds.
	
	/**
	 * Creates a ChangeData object from the two Waypoints provided
	 * @param origin
	 * @param destination
	 */
	public ChangeData(Waypoint origin, Waypoint destination) {
		float[] result = new float[1];
		Location.distanceBetween(origin.getLatitude(), origin.getLongitude(), 
				destination.getLatitude(), destination.getLongitude(), result);
		distance_ = result[0];
		
		time_ = (destination.getTime() - origin.getTime()) / 1000; // seconds
		speed_ = distance_ / time_;
	}
	
	public double getDistance() {
		return distance_;
	}
	
	public double getSpeed() {
		return speed_;
	}
	
	public long getTime() {
		return time_;
	}
}