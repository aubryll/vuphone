package edu.vanderbilt.isis.vuphone.tools;

import android.graphics.Point;

public class PrecisionPoint extends Point {
	public float x;
	public float y;
	
	public PrecisionPoint() {
	}
	
	public PrecisionPoint(Point p) {
		this.set(p);
	}
	
	public void set(Point p)
	{
		x = p.x;
		y = p.y;
	}
	
	public String toString() {
		return "X: " + x + "; Y: " + y;
	}

}
