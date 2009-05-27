package org.vuphone.wwatch.android;

import android.location.Location;



/**
 * A wrapper class that encapsulates vital information about a point along
 * a route. 
 * @author Krzysztof Zienkiewicz
 *
 */

public class Waypoint {
	private double longitude_ 	= 0;
	private double latitude_	= 0;
	private long timeStamp_		= 0;
	
	/**
	 * Construct a Waypoint from longitude, latitude, and time data.
	 * 
	 * @param lon	Longitude in degrees
	 * @param lat	Latitude in degrees
	 * @param time	UTC time in milliseconds since January 1, 1970. 
	 */
	public Waypoint(double lon, double lat, long time) {
		longitude_ = lon;
		latitude_ = lat;
		timeStamp_ = time;
	}
	
	/**
	 * Construct a Waypoint using data stored in the given Location object.
	 * @param loc	Location object
	 * @throws NullPointerException	If loc is null
	 */
	public Waypoint(Location loc) throws NullPointerException {
		if (loc == null)
			throw new NullPointerException("NULL Location in Waypoint constructor");
		
		longitude_ = loc.getLongitude();
		latitude_ = loc.getLatitude();
		timeStamp_ = loc.getTime();
	}
	
	/**
	 * Returns the latitude in degrees.
	 * @return
	 */
	public double getLatitude() {
		return latitude_;
	}
	
	/**
	 * Returns the longitude in degrees.
	 * @return
	 */
	public double getLongitude() {
		return longitude_;
	}
	
	/**
	 * Returns UTC time in milliseconds since January 1, 1970.
	 * @return
	 */
	public long getTime() {
		return timeStamp_;
	}
	
	/**
	 * Returns a human readable version of this Waypoint
	 * @return
	 */
	public String toString() {
		return "[" + longitude_ + ", " + latitude_ + ", " + timeStamp_ + "]";
	}

}
