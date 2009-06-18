package org.vuphone.wwatch.android;

import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * A wrapper class that encapsulates vital information about a point along a
 * route. Useful because the location object stores gobs of extra information.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */

public class Waypoint extends OverlayItem {
	private long timeStamp_ = 0;
	private final GeoPoint point_;

	public Waypoint(Location loc) {
		this(new GeoPoint((int) (loc.getLatitude() * 1E6), (int) (loc
				.getLongitude() * 1E6)), loc.getTime());
	}

	public Waypoint(GeoPoint point, long time) {
		super(point, "title", "snippet");
		point_ = point;
		timeStamp_ = time;
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
	
	public GeoPoint getGeoPoint() {
		return point_;
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

	public void setTime(long time) {
		timeStamp_ = time;
	}

}
