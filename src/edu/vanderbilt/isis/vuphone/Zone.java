package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Path;
import android.graphics.Point;

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
		// This function ensures that internally the values at points.get(0) and
		// points.get(points.size() - 1) will always be identical

		// Handle the edge case
		if (this.getSize() == 0) {
			points.add(point);
			return points.add(point);
		}

		// Check if requested
		if (checkIfContained)
			if (this.contains(point))
				return false;

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
		

		if (this.getSize() == 1)
			return pointToAdd.equals(projection_.toPixels(points.get(0), null));
		// TODO - make this work for the case of adding the 3rd point, currently
		// this will not work if they place the 3rd directly between the first
		// two
		if (this.getSize() == 2)
			return false;

		// First check that the line from start to pointToAdd is ok
		PrecisionPoint firstPoint = new PrecisionPoint(projection_.toPixels(points.get(0), null));
		PrecisionPoint lastPoint = new PrecisionPoint(projection_.toPixels(points.get(getSize() - 1), null));
		PrecisionPoint addPoint = new PrecisionPoint(pointToAdd);
		
		PrecisionPoint start = new PrecisionPoint(), end = new PrecisionPoint();
		for (int i = 0; i < this.getSize() - 1; i++) {
			start.set(projection_.toPixels(points.get(i), null));
			end.set(projection_.toPixels(points.get(i+1), null));
			
			// Does the line from the first point to the add point 
			//if (intersectsHelper(firstPoint, addPoint, start, end))
			//	return true;
			// Does the line from the current point to the added point
			// intersect any current lines?
			if (intersectsHelper(lastPoint, addPoint, start, end))
				return true;
		}
		
		return false;
	}

	private boolean intersectsHelper(PrecisionPoint start1,
			PrecisionPoint end1, PrecisionPoint start2, PrecisionPoint end2) {
		
		double Ax = start1.x;
		double Ay = start2.y;
		double Bx = end1.x;
		double By = end1.y;
		double Cx = start2.x;
		double Cy = start2.y;
		double Dx = end2.x;
		double Dy = end2.y;

		double distAB, theCos, theSin, newX, ABpos;

		// Fail if either line is undefined.
		if (Ax == Bx && Ay == By || Cx == Dx && Cy == Dy)
			return false;

		// (1) Translate the system so that point A is on the origin.
		Bx -= Ax;
		By -= Ay;
		Cx -= Ax;
		Cy -= Ay;
		Dx -= Ax;
		Dy -= Ay;

		// Discover the length of segment A-B.
		distAB = Math.sqrt(Bx * Bx + By * By);

		// (2) Rotate the system so that point B is on the positive X axis.
		theCos = Bx / distAB;
		theSin = By / distAB;
		newX = Cx * theCos + Cy * theSin;
		Cy = Cy * theCos - Cx * theSin;
		Cx = newX;
		newX = Dx * theCos + Dy * theSin;
		Dy = Dy * theCos - Dx * theSin;
		Dx = newX;

		// Fail if the lines are parallel.
		if (Cy == Dy)
			return false;

		// (3) Discover the position of the intersection point along line A-B.
		ABpos = Dx + (Cx - Dx) * Dy / (Dy - Cy);

		// Success.
		return true;
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

		// Adds one more line back to the start
		path.close();

		return path;
	}

	/**
	 * Helper method to remove the last point added to this zone.
	 */
	public void removeLastPoint() {
		// Minus two, because 1 is typical, and there is one end point
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
