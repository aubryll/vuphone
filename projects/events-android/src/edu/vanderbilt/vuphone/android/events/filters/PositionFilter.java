/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.filters;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.events.Constants;

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
 * 
 * @author Hamilton Turner
 * 
 */
public class PositionFilter implements Filter {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "PositionFilter: ";

	/** Hold the data. */
	private GeoPoint bottomLeft_;
	private GeoPoint topRight_;
	private int radiusInFeet_;

	/** Holds the time when we first requested GPS data */
	private long gpsStartTime_;
	
	/** Used to fire Toast when GPS first locks */
	private Context appContext_;

	/** Receives GPS updates */
	private LocationListener listener_ = new LocationListener() {

		public void onLocationChanged(Location location) {
			
			if (shouldTerminate_)
				manager_.removeUpdates(this);

			updatePosition(location, radiusInFeet_);			
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/** Used to keep track of current location */
	private LocationManager manager_;

	/** Used to signal that the updates on this filter should terminate. */
	private volatile boolean shouldTerminate_ = false;

	/**
	 * Initializes a PositionFilter that dynamically centers itself around the
	 * current location
	 */
	public PositionFilter(int radiusInFeet, Context c) {
		appContext_ = c.getApplicationContext();
		manager_ = (LocationManager) c
				.getSystemService(Context.LOCATION_SERVICE);

		gpsStartTime_ = System.currentTimeMillis();
		manager_.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3 * 1000, 1,
				listener_);

		// Get the best last known location, and use that to center
		Location last = manager_
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (last != null)
			updatePosition(last, radiusInFeet);
		else
			updatePosition(Constants.vandyCenter, radiusInFeet);
	}

	/** Center the Filter statically around a certain position */
	public PositionFilter(GeoPoint center, int radiusInFeet) {
		updatePosition(center, radiusInFeet);
	}

	/** Returns the current bottom left */
	public GeoPoint getBottomLeft() {
		return bottomLeft_;
	}

	/** Get the lower of the two latitudes */
	public long getLowerLatitude() {
		return bottomLeft_.getLatitudeE6();
	}

	/** Get the lower of the two longitudes */
	public long getLowerLongitude() {
		return bottomLeft_.getLongitudeE6();
	}

	/** Returns the current top right */
	public GeoPoint getTopRight() {
		return topRight_;
	}

	/** Get the upper of the two latitudes */
	public long getUpperLatitude() {
		return topRight_.getLatitudeE6();
	}

	/** Get the upper of the two longitudes */
	public long getUpperLongitude() {
		return topRight_.getLongitudeE6();
	}

	/**
	 * Flags this filter to stop updating itself, if it is. If this is a current
	 * location position filter, this will remove GPS updates, hopefully leaving
	 * this filter unattached to anything and ready to be garbage collected. If
	 * this is a static filter, this will do nothing
	 */
	public void stop() {
		shouldTerminate_ = true;
	}

	/** Helper method which converts the Location to a GeoPoint */
	private void updatePosition(Location loc, int radiusInFeet) {
		final GeoPoint geo = new GeoPoint((int) (loc.getLatitude() * 1E6),
				(int) (loc.getLongitude() * 1E6));
		updatePosition(geo, radiusInFeet);
	}

	/** Calculates the bounding box around the coordinates */
	private void updatePosition(GeoPoint center, int radiusInFeet) {
		Log.i(tag, pre + "Updating position to: " + center);
		radiusInFeet_ = radiusInFeet;
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

		topRight_ = new GeoPoint(upperLat, upperLon);
		bottomLeft_ = new GeoPoint(lowerLat, lowerLon);
		
		FilterManager.updateFilter(this);
	}

}
