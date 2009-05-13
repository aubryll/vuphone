package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

/**
 * Copied and modified from http://www.cs.princeton.edu/introcs/35purple/Polygon.java.html 
 * 
 * Used to represent a routing zone. Has helper methods for drawing, and 
 * determining if a GeoPoint falls within this zone. 
 * 
 * @author hamiltont
 * 
 */

/*************************************************************************
 * Compilation: javac Polygon.java Execution: java Polygon
 * 
 * An data type for polygons, possibly intersecting.
 * 
 * Centroid calculation assumes polygon is nonempty (o/w area = 0)
 * 
 *************************************************************************/

public class Zone {
	private ArrayList<GeoPoint> points; // the points, setting p[0] != p[N]
	private Projection projection_ = null;
	private String name_;

	public Zone() {
		points = new ArrayList<GeoPoint>();
	}

	public Zone(Projection p) {
		points = new ArrayList<GeoPoint>();
		projection_ = p;
	}

	/**
	 * Adds a point to this zone, checking first if the point is contained
	 * 
	 * @param point
	 * @return true if the point was added, false otherwise.
	 */
	public boolean addPoint(GeoPoint point) {
		return this.addPoint(point, true);
	}

	/**
	 * Adds a point to this zone.
	 * 
	 * @param point
	 *            The point to add
	 * @param checkIfContained
	 *            Determines whether or not the zone makes sure that it does not
	 *            already contain this point. If a developer has already ensured
	 *            that the point is not contained in this zone, this is a helper
	 *            method to save a redundant check
	 * 
	 * @return True if the point was added, false otherwise.
	 */
	public boolean addPoint(GeoPoint point, boolean checkIfContained) {
		if (checkIfContained)
			if (this.contains(point))
				return false;

		return points.add(point);
	}

	/**
	 * Helper function for getCenter that finds the area of the zone.
	 * 
	 * @return
	 */
	private double area() {
		double sum = 0.0;
		for (int i = 0; i < points.size(); i++) {
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
	 * Reference: http://exaflop.org/docs/cgafaq/cga2.html
	 * 
	 * @param search
	 * @return
	 */
	public boolean contains(GeoPoint search) {
		validateProjection();
		Point p = projection_.toPixels(search, null);
		return this.contains(p);
	}

	public boolean contains(Point search) {
		validateProjection();

		int crossings = 0;
		Point next;
		Point current;
		for (int i = 0; i < points.size(); i++) {
			next = projection_.toPixels(points.get(i + 1), null);
			current = projection_.toPixels(points.get(i), null);

			double slope = (next.x - current.x) / (next.y - current.y);

			boolean cond1 = (current.y <= search.y) && (search.y < next.y);
			boolean cond2 = (next.y <= search.y) && (search.y < current.y);
			boolean cond3 = search.x < slope * (search.y - current.y)
					+ current.x;

			if ((cond1 || cond2) && cond3)
				crossings++;
		}
		return (crossings % 2 != 0);
	}

	/**
	 * Gets the center of the polygon using the current projection.
	 * 
	 * @return A point which represents the screen location of the center of the
	 *         zone.
	 */
	public Point getCenter() {
		Double cx = 0.0, cy = 0.0;
		for (int i = 0; i < points.size(); i++) {
			Point next = projection_.toPixels(points.get(i + 1), null);
			Point current = projection_.toPixels(points.get(i), null);
			cx = cx + (current.x + next.x)
					* (current.y * next.x - current.x * next.y);
			cy = cy + (current.y + next.y)
					* (current.y * next.x - current.x * next.y);
		}
		cx /= (6 * area());
		cy /= (6 * area());
		return new Point(cx.intValue(), cy.intValue());
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
	 * Generates a Path which can then be used to draw the zone, based on the
	 * current projection.
	 * 
	 * @return the path
	 */
	public Path getPath() {

		Path path = new Path();
		path.incReserve(points.size() + 1);

		validateProjection();

		// We will use p for all our points
		Point p = projection_.toPixels(points.get(0), null);

		path.moveTo(p.x, p.y);

		for (int i = 1; i < points.size(); i++) {
			projection_.toPixels(points.get(i), p);
			path.lineTo(p.x, p.y);
		}

		// Adds one more line back to the start
		path.close();

		return path;
	}

	/**
	 * Helper method to remove the last point added to this zone.
	 */
	public void removeLastPoint() {
		if (points.size() > 0)
			points.remove(points.size() - 1);
	}

	/**
	 * Removes the given point from this zone.
	 * 
	 * @param p
	 */
	public void removePoint(GeoPoint p) {
		if (points.contains(p))
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
	 * Sets the current projection
	 * 
	 * @param p
	 */
	public void setProjection(Projection p) {
		projection_ = p;
	}

	/**
	 * Allows this class to be printed as a String.
	 */
	public String toString() {
		return "Zone: " + name_ + ", " + points.size();
	}

	/**
	 * Ensures the projection is at least usable.
	 */
	private void validateProjection() {
		if (projection_ == null)
			throw new RuntimeException(
					"Someone forgot to set the projection for Zone " + name_
							+ "!");
	}
}
