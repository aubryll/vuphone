package org.vuphone.wwatch.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

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

	private Drawable drawable_ = null;
	
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
	public int getLatitude() {
		return point_.getLatitudeE6();
	}
	
	public double getLatitudeDegrees() {
		return point_.getLatitudeE6() / 1000000.0;
	}

	/**
	 * Returns the longitude in microdegrees.
	 * 
	 * @return
	 */
	public int getLongitude() {
		return point_.getLongitudeE6();
	}

	public double getLongitudeDegrees() {
		return point_.getLongitudeE6() / 1000000.0;
	}
	
	/**
	 * Returns UTC time in milliseconds since January 1, 1970.
	 * 
	 * @return
	 */
	public long getTime() {
		return timeStamp_;
	}

	@Override
	public GeoPoint getPoint() {
		Log.v(VUphone.tag, "Getting point - " + point_.toString());
		return point_;
	}

	/**
	 * If setContext(Context) has been called, this returns an appropriate
	 * drawable. If it has not been called, it returns null, which indicates
	 * that the map should use the default drawable
	 */

	@Override
	public Drawable getMarker(int statebit) {
		return drawable_;
	}

	/**
	 * Returns a human readable version of this Waypoint
	 * 
	 * @return
	 */
	public String toString() {
		return "[" + getLongitudeDegrees() + ", " + getLatitudeDegrees() + ", " + timeStamp_
				+ "]";
	}

	public void setContext(Context c) {
		drawable_ = c.getResources().getDrawable(R.drawable.unhapppy);
		drawable_.setBounds(0, 0, drawable_.getIntrinsicWidth(), drawable_.getIntrinsicHeight());
		setMarker(drawable_);
	}

	public void setTime(long time) {
		timeStamp_ = time;
	}

}
