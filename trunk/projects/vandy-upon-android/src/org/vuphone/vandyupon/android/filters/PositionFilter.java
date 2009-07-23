/**
 * 
 */
package org.vuphone.vandyupon.android.filters;

import com.google.android.maps.GeoPoint;

/**
 * Represents a central location, and a radius around that location. Events must
 * fall within this radius to be included in results
 * 
 * This class is intended to be used on the client device to assist in database
 * queries. It converts the radius and the center point to a bounding rectangle,
 * because a database query can easily be constructed for a rectangular region,
 * but it is very difficult in SQLite to do a database query for a circular
 * radius (although in MySQL it is not very hard to do this query, because of
 * the support for trigonometric functions in the query. See
 * http://www.artfulsoftware.com/infotree/queries.php?&bw=1280#109 )
 * 
 * @author Hamilton Turner
 * 
 */
public class PositionFilter {
	/** Used for logging */
	//private static final String tag = Constants.tag;
	//private static final String pre = "PositionFilter: ";

	/** Hold the data */
	private int lowerLat_;
	private int lowerLon_;
	private int upperLat_;
	private int upperLon_;

	public PositionFilter(GeoPoint center, int radiusInFeet) {
		double radiusInMeters = (double) radiusInFeet * 0.3048;

		// I have no idea how these calculations work, but I copied them from a
		// government website so I feel good about that
		// http://www.nga.mil/MSISiteContent/StaticFiles/Calculators/degree.html

		// Convert latitude to radians
		float lat = (float) ((double) center.getLatitudeE6() / 1E6);
		float conv_factor = (float) ((2.0 * Math.PI) / 360.0);
		lat = lat * conv_factor;

		// Set up "Constants"
		double m1 = 111132.92;
		double m2 = -559.82;
		double m3 = 1.175;
		double m4 = -0.0023;
		double p1 = 111412.84;
		double p2 = -93.5;
		double p3 = 0.118;

		// Calculate the length of a degree of latitude and longitude in meters
		double latlen = m1 + (m2 * Math.cos(2 * lat))
				+ (m3 * Math.cos(4 * lat)) + (m4 * Math.cos(6 * lat));
		double longlen = (p1 * Math.cos(lat)) + (p2 * Math.cos(3 * lat))
				+ (p3 * Math.cos(5 * lat));

		int latDegreeChange = (int) ((radiusInMeters / latlen) * 1E6);
		int lonDegreeChange = (int) ((radiusInMeters / longlen) * 1E6);

		int lowerLat = center.getLatitudeE6() - latDegreeChange;
		int upperLat = center.getLatitudeE6() + latDegreeChange;
		int lowerLon = center.getLongitudeE6() - lonDegreeChange;
		int upperLon = center.getLongitudeE6() + lonDegreeChange;

		if (lowerLat >= upperLat)
			throw new IllegalArgumentException(
					"Lower Latitude cannot be >= Upper latitude");
		if (lowerLon >= upperLon)
			throw new IllegalArgumentException(
					"Lower Longitude cannot be >= Upper longitude");

		lowerLat_ = lowerLat;
		upperLat_ = upperLat;
		lowerLon_ = lowerLon;
		upperLon_ = upperLon;
	}

	/** Get the lower of the two latitudes */
	public long getLowerLatitude() {
		return lowerLat_;
	}

	/** Get the lower of the two longitudes */
	public long getLowerLongitude() {
		return lowerLon_;
	}

	/** Get the upper of the two latitudes */
	public long getUpperLatitude() {
		return upperLat_;
	}

	/** Get the upper of the two longitudes */
	public long getUpperLongitude() {
		return upperLon_;
	}
}
