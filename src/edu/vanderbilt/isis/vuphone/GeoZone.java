package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;
import com.google.android.maps.GeoPoint;

public class GeoZone{
	private ArrayList<GeoPoint> points_;
	private String name_;
	
	GeoZone(){
		this("Default Name");
	}
	
	GeoZone(String name){
		points_ = new ArrayList<GeoPoint>();
		name_ = name;
	}
	
	public void addPoint(GeoPoint pt){
		if (!points_.contains(pt))
			points_.add(pt);
	}
	
	public void addPoints(GeoPoint[] pts){
		for (int i = 0; i < pts.length; ++i)
			addPoint(pts[i]);
	}
	
	public GeoPoint getPoint(int i){
		if (i < size())
			return points_.get(i);
		else
			return null;
	}
	
	public boolean contains(GeoPoint pt){
		return points_.contains(pt);
	}
	
	public void remove(GeoPoint pt){
		points_.remove(pt);
	}
	
	public GeoPoint[] getPoints(){
		// Force return array
		GeoPoint[] temp = new GeoPoint[0];
		return points_.toArray(temp);
	}
	
	public int size(){
		return points_.size();
	}
	
	public String toString(){
		return "GeoZone: " + name_ + ", " + points_.size();
	}
}