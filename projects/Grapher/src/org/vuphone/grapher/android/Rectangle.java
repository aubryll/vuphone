package org.vuphone.grapher.android;

import android.graphics.PointF;

/**
 * A representation of a Euclidean rectangle. The coordinates (minX, minY) and (maxX, maxY) are used
 * as boundaries for the rectangle. Only positive x coordinates are supported.
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class Rectangle {

	public PointF min;
	public PointF max;
	
	public Rectangle(float minX, float minY, float maxX, float maxY) {
		if (minX < 0)
			minX = 0;
		min = new PointF(minX, minY);
		max = new PointF(maxX, maxY);
	}
	
	public float height() {
		return max.y - min.y;
	}
	
	/**
	 * Sets this rectangle to rect
	 * 
	 * @param rect
	 */
	public void set(Rectangle rect) {
		min.set(rect.min);
		max.set(rect.max);
	}
	
	public void shift(float dx, float dy) {
		min.offset(dx, dy);
		max.offset(dx, dy);
		
		if (min.x < 0)
			shift(-min.x, 0);
	}
	
	public void shift(PointF pt) {
		shift(pt.x, pt.y);
	}
	
	public float width() {
		return max.x - min.x;
	}
}
