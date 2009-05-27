package org.vuphone.wwatch.android;

import java.util.ArrayList;

import android.location.Location;

/**
 * A class responsible for handling recent points along a route and performing 
 * fundamental operations on the data. 
 * 
 * @author Krzysztof Zienkiewicz
 *
 */

public class WaypointTracker {
	
	private ArrayList<Waypoint> pointList_ 		= null;
	private ArrayList<ChangeData> deltaList_	= null;
	
	private double timeDialation_ = 1.0;	// 1 simulated second equals timeDialation_ real seconds
											// ie, 2.0 makes everything twice as fast

	/**
	 * Creates a WaypointTracker using the provided time dialation
	 * factor or 1.0 by default
	 * 
	 * @param dialation
	 */
	public WaypointTracker(double dialation) {
		pointList_ = new ArrayList<Waypoint>();
		deltaList_ = new ArrayList<ChangeData>();
		timeDialation_ = dialation;
	}

	public WaypointTracker() {
		this(1.0);
	}
	
	/**
	 * Adds a new Waypoint based on the Location
	 * @param loc
	 * @throws NullPointerException
	 */
	public void addWaypoint(Location loc) throws NullPointerException {
		if (loc == null)
			throw new NullPointerException("Null Location in WaypointTracker.addWaypoint()");
		
		Waypoint current = new Waypoint(loc);
		
		if (pointList_.size() >= 1) {
			Waypoint last = pointList_.get(pointList_.size() - 1);
			deltaList_.add(new ChangeData(last, current));
		}
		
		pointList_.add(current);
	}
	
	public double getLatestSpeed() {
		if (pointList_.size() <= 1)
			return 0.0;
		
		double spd = deltaList_.get(deltaList_.size() - 1).getSpeed(); 
		
		return spd * timeDialation_;
	}
}
