package edu.vanderbilt.isis.vuphone.tools;

import android.graphics.Point;

public class PrecisionPoint extends Point {
	
	public double x;
	public double y;
	
	public PrecisionPoint() {
	}
	
	public PrecisionPoint(Point p) {
		this.set(p);
	}
	
	public PrecisionPoint(double x, double y){
		this.x = x;
		this.y = y;
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
