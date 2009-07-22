/**
 * 
 */
package org.vuphone.vandyupon.android.filters;

import com.google.android.maps.GeoPoint;

/**
 * @author Hamilton Turner
 * 
 */
public class PositionFilter {
	private int lowerLat_;
	private int lowerLon_;
	private int upperLat_;
	private int upperLon_;

	
	public PositionFilter(GeoPoint center, int radius) {
		
	}
	
	public PositionFilter(int lowerLat, int upperLat, int lowerLon, int upperLon) {
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

	public long getLowerLatitude() {
		return lowerLat_;
	}

	public long getLowerLongitude() {
		return lowerLon_;
	}

	public long getUpperLatitude() {
		return upperLat_;
	}

	public long getUpperLongitude() {
		return upperLon_;
	}
}
