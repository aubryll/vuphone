package org.vuphone.wwatch.android;

import java.util.ArrayList;
import java.util.List;

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
	
	private double timeDilation_ = 1.0;	// timeDilation_  * actual time = simulated time
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
		timeDilation_ = dialation;
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
	
	/**
	 * Compute and return the total distance between the start
	 * and end waypoints. 
	 * @param start
	 * @param end
	 * @return
	 */
	public double getDistanceBetween(int start, int end) throws RuntimeException{
		if (start >= end)
			throw new RuntimeException("Invalid start and end indeces");
		double distance = 0;
		for (int index = start; index < end; ++index)
			distance += deltaList_.get(index).getDistance();
		return distance;
	}
	
	/**
	 * Computes and returns the dialted acceleration based on the most recent
	 * waypoints.
	 * @return
	 */
	public double getLatestAcceleration() {
		// TODO - Bound check
		int size = pointList_.size();
		
		switch (size) {
		case 0:
		case 1:
			return 0;
		case 2:
			// Assumes the starting speed is 0.
			return this.getSpeedBetween(0, 1) / this.getTimeBetween(0, 1);
		default: // 3 and more
			double deltaSpeed = this.getSpeedBetween(size - 2, size - 1) - this.getSpeedBetween(size - 3, size - 2);
			double deltaTime = this.getTimeBetween(size - 3, size - 1);
			double accel = deltaSpeed / deltaTime;
		
			return accel;
		}
	}
	
	/**
	 * Computes and returns the dialated speed based on the two
	 * most recent waypoints.
	 * @return
	 */
	public double getLatestSpeed() {
		int size = pointList_.size();
		if (size <= 1)
			return 0.0;
		return this.getSpeedBetween(size - 2, size - 1);
	}
	
	/**
	 * Compute and return the average dialated speed between the start
	 * and end waypoints
	 *   
	 * @param origin		Index of the starting 
	 * @param destination
	 * @return
	 */
	public double getSpeedBetween(int start, int end) throws RuntimeException {
		if (start >= end)
			throw new RuntimeException("Invalid start and end indeces");
		double distance = this.getDistanceBetween(start, end);
		double time = this.getTimeBetween(start, end);
		
		return distance / time;
	}
	
	/**
	 * Compute and return the total dialated time between the start
	 * and end waypoints.
	 * @param start
	 * @param end
	 * @return
	 */
	public double getTimeBetween(int start, int end) throws RuntimeException {
		if (start >= end)
			throw new RuntimeException("Invalid start and end indeces");
		long time = pointList_.get(end).getTime() - pointList_.get(start).getTime();
		return (double) time / timeDilation_;
	}
	
	public List<Waypoint> getList() {
		return pointList_;
	}
	
	public void setDilation(double d) {
		timeDilation_ = d;
	}
	
}
