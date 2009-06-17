package org.vuphone.wwatch.android;

import android.location.Location;

import com.google.android.maps.GeoPoint;

/**
 * A wrapper class that encapsulates vital information about a point along a
 * route. Useful because the location object stores gobs of extra information.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */

public class Waypoint {
	private long timeStamp_ = 0;
	private GeoPoint point_;

	/**
	 * Construct a Waypoint from longitude, latitude, and time data.
	 * 
	 * @param lon
	 *            Longitude in degrees
	 * @param lat
	 *            Latitude in degrees
	 * @param time
	 *            UTC time in milliseconds since January 1, 1970.
	 */
	public Waypoint(double lon, double lat, long time) {
		timeStamp_ = time;
		point_ = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
	}

	/**
	 * Construct a Waypoint using data stored in the given Location object.
	 * 
	 * @param loc
	 *            Location object
	 */
	public Waypoint(Location loc) {
		if (loc == null)
			throw new IllegalArgumentException(
					"NULL Location in Waypoint constructor not allowed");

		point_ = new GeoPoint((int) (loc.getLatitude() * 1E6), (int) (loc
				.getLongitude() * 1E6));

		timeStamp_ = loc.getTime();
	}

	public Waypoint() {
		// To avoid null pointer exceptions
		point_ = new GeoPoint(0, 0);
	}

	public GeoPoint getGeoPoint() {
		return point_;
	}

	/**
	 * Returns the latitude in microdegrees.
	 * 
	 * @return
	 */
	public double getLatitude() {
		return point_.getLatitudeE6();
	}

	/**
	 * Returns the longitude in microdegrees.
	 * 
	 * @return
	 */
	public double getLongitude() {
		return point_.getLongitudeE6();
	}

	/**
	 * Returns UTC time in milliseconds since January 1, 1970.
	 * 
	 * @return
	 */
	public long getTime() {
		return timeStamp_;
	}

	/**
	 * Returns a human readable version of this Waypoint
	 * 
	 * @return
	 */
	public String toString() {
		return "[" + getLongitude() + ", " + getLatitude() + ", " + timeStamp_
				+ "]";
	}

	/**
	 * 
	 * @param lat
	 *            new latitude in degrees
	 */
	public void setLatitude(double lat) {
		point_ = new GeoPoint((int) (lat * 1E6), point_.getLongitudeE6());
	}

	public void setLongitude(double lon) {
		point_ = new GeoPoint(point_.getLatitudeE6(), (int) (lon * 1E6));
	}

	public void setTime(long time) {
		timeStamp_ = time;
	}

}
