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

	private ArrayList<Waypoint> pointList_ = null;

	private double timeDilation_ = 1.0; // timeDilation_ * actual time =

	// simulated time

	// ie, 2.0 makes everything twice as fast

	/**
	 * Creates a WaypointTracker using the provided time dialation factor or 1.0
	 * by default
	 * 
	 * @param dialation
	 */
	public WaypointTracker(double dialation) {
		pointList_ = new ArrayList<Waypoint>();
		timeDilation_ = dialation;
	}

	public WaypointTracker() {
		this(1.0);
	}

	/**
	 * Adds a new Waypoint based on the Location
	 * 
	 * @param loc
	 * @throws NullPointerException
	 */
	public void addWaypoint(Location loc) throws NullPointerException {
		if (loc == null)
			throw new NullPointerException(
					"Null Location in WaypointTracker.addWaypoint()");

		Waypoint current = new Waypoint(loc);

		pointList_.add(current);
	}

	public double getDilation() {
		return timeDilation_;
	}

	/**
	 * Compute and return the total distance between two waypoints. The start
	 * will be the index of the first waypoint in the internal array, and the
	 * second waypoint will be found by adding one
	 * 
	 * @param start
	 * @return
	 */
	private double getDistanceBetween(int start) {
		int end = start + 1;
		if (start < 0 || start > pointList_.size() - 1 || end < 0
				|| end > pointList_.size() - 1 || end < start)
			throw new IllegalArgumentException(
					"The index bounds used are not valid: start:" + start
							+ ", end:" + end + ", size:" + pointList_.size());

		float[] result = new float[1];
		Location.distanceBetween(pointList_.get(start).getLatitude(), pointList_.get(start).getLongitude(),
				pointList_.get(end).getLatitude(), pointList_.get(end).getLongitude(), result);

		return result[0];
	}

	/**
	 * Computes and returns the dialted acceleration based on the most recent
	 * waypoints.
	 * 
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
			double deltaSpeed = this.getSpeedBetween(size - 2, size - 1)
					- this.getSpeedBetween(size - 3, size - 2);
			double deltaTime = this.getTimeBetween(size - 3, size - 1);
			double accel = deltaSpeed / deltaTime;

			return accel;
		}
	}

	/**
	 * Computes and returns the dialated speed based on the two most recent
	 * waypoints.
	 * 
	 * @return
	 */
	public double getLatestSpeed() {
		int size = pointList_.size();
		if (size < 2)
			return 0.0;
		return getSpeedBetween(size - 2, size - 1);
	}

	/**
	 * Compute and return the average dialated speed between two start and end
	 * waypoints
	 * 
	 * @param origin
	 *            Index of the starting
	 * @param destination
	 * @return
	 */
	public double getSpeedBetween(int start, int end) throws RuntimeException {
		if (start >= end)
			throw new RuntimeException("Invalid start and end indeces");
		double distance = getDistanceBetween(start);
		double time = getTimeBetween(start, end);

		return distance / time;
	}

	/**
	 * Compute and return the total dialated time between the start and end
	 * waypoints.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public double getTimeBetween(int start, int end) throws RuntimeException {
		if (start >= end)
			throw new RuntimeException("Invalid start and end indeces");
		long time = pointList_.get(end).getTime()
				- pointList_.get(start).getTime();
		return (double) time / timeDilation_;
	}

	public List<Waypoint> getList() {
		return pointList_;
	}

	public void setDilation(double d) {
		timeDilation_ = d;
	}

}
