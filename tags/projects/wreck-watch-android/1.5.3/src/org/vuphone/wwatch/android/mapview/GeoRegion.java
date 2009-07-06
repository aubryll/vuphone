package org.vuphone.wwatch.android.mapview;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class GeoRegion {

	private final GeoPoint topLeft_;
	private final GeoPoint bottomRight_;

	public GeoRegion(GeoPoint topLeft, GeoPoint bottomRight) {
		if (topLeft == null || bottomRight == null)
			throw new IllegalArgumentException("Cannot be null");
		if ((topLeft.getLatitudeE6() <= bottomRight.getLatitudeE6())
				|| (topLeft.getLongitudeE6() >= bottomRight.getLongitudeE6()))
			throw new IllegalArgumentException("Malformed region");

		topLeft_ = topLeft;
		bottomRight_ = bottomRight;
	}

	public boolean contains(final GeoPoint p) {
		if (p.getLongitudeE6() > bottomRight_.getLongitudeE6())
			return false;
		else if (p.getLongitudeE6() < topLeft_.getLongitudeE6())
			return false;
		else if (p.getLatitudeE6() > topLeft_.getLatitudeE6())
			return false;
		else if (p.getLatitudeE6() < bottomRight_.getLatitudeE6())
			return false;
		else
			return true;
	}

	public int latitudeSpan() {
		return Math
				.abs(topLeft_.getLatitudeE6() - bottomRight_.getLatitudeE6());
	}

	public int longitudeSpan() {
		return Math.abs(bottomRight_.getLongitudeE6()
				- topLeft_.getLongitudeE6());
	}

	public GeoPoint getTopLeft() {
		return topLeft_;
	}

	public GeoPoint getBottomRight() {
		return bottomRight_;
	}

	public GeoRegion getSafeRegion(final double safeRegionSize) {
		// Assuming no crossing of the pacific or the poles:
		// In latitude, up is larger.
		// In longitude, right is larger.

		final int safeTLLatitude = topLeft_.getLatitudeE6()
				- (int) (safeRegionSize / 2 * latitudeSpan());
		final int safeTLLongitude = topLeft_.getLongitudeE6()
				+ (int) (safeRegionSize / 2 * longitudeSpan());
		final int safeBRLatitude = bottomRight_.getLatitudeE6()
				+ (int) (safeRegionSize / 2 * latitudeSpan());
		final int safeBRLongitude = bottomRight_.getLongitudeE6()
				- (int) (safeRegionSize / 2 * longitudeSpan());

		final GeoPoint safeBottomRight = new GeoPoint(safeBRLatitude,
				safeBRLongitude);
		final GeoPoint safeTopLeft = new GeoPoint(safeTLLatitude,
				safeTLLongitude);
		return new GeoRegion(safeTopLeft, safeBottomRight);
	}

	public boolean isPointAbove(final GeoPoint point) {
		if (point.getLatitudeE6() > topLeft_.getLatitudeE6())
			return true;
		return false;
	}

	public boolean isPointBelow(final GeoPoint point) {
		if (point.getLatitudeE6() < bottomRight_.getLatitudeE6())
			return true;
		return false;
	}

	public boolean isPointToRight(final GeoPoint point) {
		if (point.getLongitudeE6() > bottomRight_.getLongitudeE6())
			return true;
		return false;
	}

	public boolean isPointToLeft(final GeoPoint point) {
		if (point.getLongitudeE6() < topLeft_.getLongitudeE6())
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "TL: " + topLeft_ + "\nBR: " + bottomRight_;
	}
	
	/**
	 * Returns the area of this region in m^2
	 * @return
	 */
	public double getArea() {
		double startLat = topLeft_.getLatitudeE6();
		double finLat = startLat;
		double startLng = topLeft_.getLongitudeE6();
		double finLng = bottomRight_.getLongitudeE6();
		float[] distLng = new float[1];
		Location.distanceBetween(startLat, startLng, finLat, finLng, distLng);
		
		float[] distLat = new float[1];
		finLat = bottomRight_.getLatitudeE6();
		finLng = startLng;
		Location.distanceBetween(startLat, startLng, finLat, finLng, distLat);
		
		double area = distLat[0] * distLng[0];
		return area;
	}
}
