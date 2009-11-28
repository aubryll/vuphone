package com.zienkikk.android.grapher;

import android.graphics.Point;


/**
 * A class that defines the graph configurations that let GraphRenderer know how to draw the data. 
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class GraphConfig {

	/**
	 * The clip of Euclidean space of the graph that will be displayed.
	 */
	public Rectangle area;
	
	/**
	 * The top-left corner of the graph area, relative to the view.
	 */
	public Point offset;
	/**
	 * The amount of padding to the right and below the graph.
	 */
	public Point border;
	
	/**
	 * The respective axes will have tick marks and value strings at multiples of these values.
	 */
	public float markX; 
	/**
	 * The respective axes will have tick marks and value strings at multiples of these values.
	 */
	public float markY;
	
	
	public GraphConfig() {
		area = new Rectangle(0, -2, 10, 2);
		
		markX = 1;
		markY = 0.5f;
		
		offset = new Point(30, 30);
		border = new Point(10, 30);
	}
	
	/**
	 * Returns a deep copy of this object
	 * 
	 * @return
	 */
	public GraphConfig copy() {
		GraphConfig copy = new GraphConfig();
		copy.area.set(area);
		copy.offset.set(offset.x, offset.y);
		copy.border.set(border.x, border.y);
		copy.markX = markX;
		copy.markY = markY;
		
		return copy;
	}
}
