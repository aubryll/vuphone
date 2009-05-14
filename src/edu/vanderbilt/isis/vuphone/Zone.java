package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

import edu.vanderbilt.isis.vuphone.tools.PrecisionPoint;

/**
 * Used to represent a routing zone. Has helper methods for drawing, and
 * determining if a GeoPoint falls within this zone..
 * 
 * @author hamiltont
 * 
 * 
 *         Important parts of this class derive from
 *         http://www.cs.princeton.edu/introcs/35purple/Polygon.java.html
 *         http://alienryderflex.com/polygon/
 */
public class Zone {
	private ArrayList<GeoPoint> points; // should always ensure p[0] == p[N]
	private Projection projection_ = null;
	private String name_ = "Default";
	public static final String TAG = "ZONE";

	public Zone() {
		points = new ArrayList<GeoPoint>();
		name_ = "Default Zone Name";
	}

	public Zone(Projection p) {
		this();
		projection_ = p;

		if (projection_ == null)
			throw new RuntimeException(
					"VUPHONE - Someone passed null for Zone projection!");

	}

	// TODO - description - this is a terribly implemented observer pattern, but
	// it works for now
	private ZoneMapView zmv_;

	public void addFinalizedObserver(ZoneMapView zmv) {
		zmv_ = zmv;
	}

	/**
	 * Adds a point to this zone, checking first if the point is contained
	 * 
	 * @param point
	 * @return true if the point was added, false otherwise.
	 */
	public boolean addPoint(GeoPoint point) {
		// This function ensures that internally the values at points.get(0) and
		// points.get(points.size() - 1) will always be identical

		// Handle the edge case
		if (this.getSize() == 0) {
			points.add(point);
			return points.add(point);
		}

		Point p = projection_.toPixels(point, null);
		Point start = projection_.toPixels(points.get(0), null);
		Log.v(TAG, "Trying to add " + p.toString());

		// Check if they are trying to touch the start pin
		double distanceFromStart = Math.pow(start.x - p.x, 2)
				+ Math.pow(start.y - p.y, 2);
		distanceFromStart = Math.sqrt(distanceFromStart);
		// TODO - this is crap code, only works for one zone, refactor
		if (distanceFromStart < 10)
		{
			zmv_.zoneFinalizedEvent();
			return true;
		}

		if (intersects(p))
			return false;

		Log.v(TAG, "Adding point");

		// If we are here, we know there are at least 2 elements in points
		// Remove the end point, add the current one, add back the end
		points.remove(points.size() - 1);
		points.add(point);
		return points.add(points.get(0));
	}

	/**
	 * Helper function for getCenter that finds the area of the zone.
	 * 
	 * @return
	 */
	private double area() {
		double sum = 0.0;

		for (int i = 0; i < this.getSize(); i++) {
			Point next = projection_.toPixels(points.get(i + 1), null);
			Point current = projection_.toPixels(points.get(i), null);

			sum = sum + (current.x * next.y) - (current.y * next.x);
		}
		return Math.abs(0.5 * sum);
	}

	/**
	 * Checks to see if the given search point is contained within this zone,
	 * according to the current projection. Unreliable if point is on boundary
	 * of zone.
	 * 
	 * Reference: http://alienryderflex.com/polygon/
	 * 
	 * @param search
	 * @return
	 */
	public boolean contains(GeoPoint search) {
		Point p = projection_.toPixels(search, null);
		return this.contains(p);
	}

	public boolean contains(Point search) {
		if (this.getSize() < 3)
			return false;

		int left;
		int right = this.getSize() - 1;
		boolean oddNodes = false;

		for (left = 0; left < this.getSize(); left++) {

			PrecisionPoint currentLeft = new PrecisionPoint(projection_
					.toPixels(points.get(left), null));
			PrecisionPoint currentRight = new PrecisionPoint(projection_
					.toPixels(points.get(right), null));

			if (currentLeft.y < search.y && currentRight.y >= search.y
					|| currentRight.y < search.y && currentLeft.y >= search.y) {
				if (currentLeft.x + (search.y - currentLeft.y)
						/ (currentRight.y - currentLeft.y)
						* (currentRight.x - currentLeft.x) < search.x) {
					oddNodes = !oddNodes;
				}
			}
			right = left;
		}

		return oddNodes;
	}

	/**
	 * Called before a point is added to our internal list.
	 * 
	 * @param pointToAdd
	 * @return true if the two lines formed by adding this point will intersect
	 *         any of the existing lines, and false otherwise.
	 */
	private boolean intersects(Point pointToAdd) {
		Log.v(TAG, "Entering intersects");

		if (this.getSize() == 0)
			return false;
		if (this.getSize() == 1)
			return pointToAdd.equals(projection_.toPixels(points.get(0), null));
		// TODO - make this work for the case of adding the 3rd point, currently
		// this will not work if they place the 3rd directly between the first
		// two
		if (this.getSize() == 2)
			return false;

		// First check that the line from start to pointToAdd is ok
		PrecisionPoint firstPoint = new PrecisionPoint(projection_.toPixels(
				points.get(0), null));
		PrecisionPoint lastPoint = new PrecisionPoint(projection_.toPixels(
				points.get(getSize() - 1), null));
		PrecisionPoint addPoint = new PrecisionPoint(pointToAdd);

		PrecisionPoint start = new PrecisionPoint(), end = new PrecisionPoint();
		for (int i = 0; i < this.getSize() - 1; i++) {
			Log.v(TAG, "Check " + i);
			start.set(projection_.toPixels(points.get(i), null));
			end.set(projection_.toPixels(points.get(i + 1), null));

			// Does the line from the first point to the add point
			// if (intersectsHelper(firstPoint, addPoint, start, end))
			// return true;
			// Does the line from the current point to the added point
			// intersect any current lines?
			if (intersectsHelper(lastPoint, addPoint, start, end)) {
				Log.v(TAG, "returning true");
				return true;
			}
		}

		Log.v(TAG, "Returning false");
		return false;
	}

	private boolean intersectsHelper(PrecisionPoint start1,
			PrecisionPoint end1, PrecisionPoint start2, PrecisionPoint end2) {

		// First find Ax+By=C values for the two lines
		double A1 = end1.y - start1.y;
		double B1 = start1.x - end1.x;
		double C1 = A1 * start1.x + B1 * start1.y;

		double A2 = end2.y - start2.y;
		double B2 = start2.x - end2.x;
		double C2 = A2 * start2.x + B2 * start2.y;

		double det = (A1 * B2) - (A2 * B1);

		if (det == 0) {
			// Lines are either parallel, are collinear (the exact same
			// segment), or are overlapping partially, but not fully
			// To see what the case is, check if the endpoints of one line
			// correctly satisfy the equation of the other (meaning the two
			// lines have the same y-intercept).
			// If no endpoints on 2nd line can be found on 1st, they are
			// parallel.
			// If any can be found, they are either the same segment,
			// overlapping, or two segments of the same line, separated by some
			// distance.
			// Remember that we know they share a slope, so there are no other
			// possibilities

			// Check if the segments lie on the same line
			// (No need to check both points)
			if ((A1 * start2.x) + (B1 * start2.y) == C1) {
				// They are on the same line, check if they are in the same
				// space
				// We only need to check one axis - the other will follow
				if ((Math.min(start1.x, end1.x) < start2.x)
						&& (Math.max(start1.x, end1.x) > start2.x))
					return true;

				// One end point is ok, now check the other
				if ((Math.min(start1.x, end1.x) < end2.x)
						&& (Math.max(start1.x, end1.x) > end2.x))
					return true;

				// They are on the same line, but there is distance between them
				return false;
			}

			// They are simply parallel
			return false;
		} else {
			// Lines DO intersect somewhere, but do the line segments intersect?
			double x = (B2 * C1 - B1 * C2) / det;
			double y = (A1 * C2 - A2 * C1) / det;

			// Make sure that the intersection is within the bounding box of
			// both segments
			if ((x > Math.min(start1.x, end1.x) && x < Math.max(start1.x,
					end1.x))
					&& (y > Math.min(start1.y, end1.y) && y < Math.max(
							start1.y, end1.y))) {
				// We are within the bounding box of the first line segment,
				// so now check second line segment
				if ((x > Math.min(start2.x, end2.x) && x < Math.max(start2.x,
						end2.x))
						&& (y > Math.min(start2.y, end2.y) && y < Math.max(
								start2.y, end2.y))) {
					// The line segments do intersect
					return true;
				}
			}

			// The lines do intersect, but the line segments do not
			return false;
		}
	}

	/**
	 * Gets the center of the polygon using the current projection. ONLY WORKS
	 * RELIABLY ON SIMPLE POLYGONS.
	 * 
	 * @return A point which represents the screen location of the center of the
	 *         zone.
	 */
	public Point getCenter() {
		if (this.getSize() == 0)
			return new Point(0, 0);
		if (this.getSize() < 3) {
			Point only = projection_.toPixels(points.get(0), null);
			return only;
		}

		Double cx = 0.0, cy = 0.0;
		for (int i = 0; i < this.getSize(); i++) {
			Point next = projection_.toPixels(points.get(i + 1), null);
			Point current = projection_.toPixels(points.get(i), null);
			cx = cx + (current.x + next.x)
					* (current.y * next.x - current.x * next.y);
			cy = cy + (current.y + next.y)
					* (current.y * next.x - current.x * next.y);
		}
		cx /= (6 * this.area());
		cy /= (6 * this.area());
		Point center = new Point(-1 * cx.intValue(), -1 * cy.intValue());
		if (center.equals(0, 0))
			center = projection_.toPixels(points.get(0), null);
		return center;
	}

	/**
	 * Gets the name of this zone
	 * 
	 * @return
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Gets all of the points currently in this zone
	 * 
	 * @return
	 */
	public List<GeoPoint> getPoints() {
		return points;
	}

	/**
	 * Returns the number of points that currently define this zone.
	 * 
	 * @return
	 */
	public int getSize() {
		if (points.size() == 0)
			return 0;

		// Subtract the end point
		return points.size() - 1;
	}

	/**
	 * Generates a Path which can then be used to draw the zone, based on the
	 * current projection.
	 * 
	 * @return the path
	 */
	public Path getPath() {

		Path path = new Path();
		path.incReserve(this.getSize() + 1);

		// We will use p for all our points
		Point p = projection_.toPixels(points.get(0), null);

		path.moveTo(p.x, p.y);

		for (int i = 1; i < this.getSize(); i++) {
			projection_.toPixels(points.get(i), p);
			path.lineTo(p.x, p.y);
		}

		return path;
	}

	/**
	 * Helper method to remove the last point added to this zone.
	 */
	public void removeLastPoint() {
		if (this.getSize() > 0)
			this.removePoint(points.get(this.getSize() - 1));
	}

	/**
	 * Removes the given point from this zone. This is purposefully a private
	 * function, ensuring that arbitrary removals cannot take place. If they
	 * could, we would have to validate the polygon formed after every removal.
	 * This way, we validate as we construct the polygon, and removing them in
	 * reverse order does not invalidate anything
	 * 
	 * @param p
	 */
	private void removePoint(GeoPoint p) {
		while (points.contains(p))
			points.remove(p);
	}

	/**
	 * Allows the name of the zone to be changed.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Allows this class to be printed as a String.
	 */
	public String toString() {
		return "Zone: " + name_ + ", " + this.getSize();
	}
}
