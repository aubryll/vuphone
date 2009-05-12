package edu.vanderbilt.isis.vuphone;

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
 * An immutable datat type for polygons, possibly intersecting.
 * 
 * Centroid calculation assumes polygon is nonempty (o/w area = 0)
 * 
 *************************************************************************/

public class Zone {
	private final int N; // number of points in the polygon
	private final GeoPoint[] points; // the points, setting p[0] = p[N]
	private Projection projection_ = null;
	private final String name_;

	public Zone(GeoPoint[] points, String name) {
		this(points, name, true);
	}

	public Zone(GeoPoint[] points, String name, boolean includesEndPoint) {
		name_ = name;
		projection_ = null;

		if (includesEndPoint)
			N = points.length - 1;
		else
			N = points.length;

		// defensive copy
		this.points = new GeoPoint[N + 1];
		for (int i = 0; i < N; i++)
			this.points[i] = points[i];
		this.points[N] = points[0];
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
		for (int i = 0; i < N; i++) {
			next = projection_.toPixels(points[i + 1], null);
			current = projection_.toPixels(points[i], null);

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
	 * Gets the name of this zone
	 * @return
	 */
	public String getName() {
		return name_;
	}
	
	/**
	 * Generates a Path which can then be used to draw the zone, based on the
	 * current projection.
	 * 
	 * @return the path
	 */
	public Path getPath() {

		Path path = new Path();
		path.incReserve(N + 1);

		validateProjection();

		// We will use p for all our points
		Point p = projection_.toPixels(points[0], null);

		path.moveTo(p.x, p.y);

		for (int i = 1; i < N; i++) {
			projection_.toPixels(points[i], p);
			path.lineTo(p.x, p.y);
		}

		// Adds one more line back to the start
		path.close();

		return path;
	}
	
	/**
	 * Sets the current projection
	 * @param p
	 */
	public void setProjection(Projection p) {
		projection_ = p;
	}

	/**
	 * Ensures the projection is at least usable. 
	 */
	private void validateProjection() {
		if (projection_ == null)
			throw new RuntimeException(
					"Someone forgot to set the projection for Zone " + name_ + "!");
	}
}
