package org.vuphone.wwatch.android;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

/**
 * A class responsible for handling waypoints along the route, and for allowing
 * basic queries on those waypoints, particularly queries pertaining to the last
 * two points added
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */

public class WaypointTracker {

	private ArrayList<Waypoint> pointList_ = null;

	// timeDilation_ * actual time = simulated time
	// ie, 2.0 makes everything twice as fast. In terms of speed, this makes the
	// speed also twice as quick
	private double timeDilation_ = 1.0;

	/**
	 * Creates a WaypointTracker using the provided time dilation factor or 1.0
	 * by default
	 * 
	 * @param dilation
	 */
	public WaypointTracker(double dilation) {
		pointList_ = new ArrayList<Waypoint>();
		timeDilation_ = dilation;
	}

	public WaypointTracker() {
		this(1.0);
	}

	/**
	 * Adds a new Waypoint
	 * 
	 * @param loc
	 *            The location where the waypoint should be placed
	 * @throws NullPointerException
	 */
	public void addWaypoint(Location loc) {
		if (loc == null)
			throw new IllegalArgumentException(
					"Null Location in WaypointTracker.addWaypoint()");

		pointList_.add(new Waypoint(loc));
	}

	/**
	 * Get the dilation scale factor. A factor of 2.0 will make both speed and
	 * time twice as fast.
	 * 
	 * @return
	 */
	public double getDilation() {
		return timeDilation_;
	}

	/**
	 * Computes and returns the dilated acceleration based on the most recent
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
			return this.getSpeedAt(0) / this.getTimeBetween(0, 1);
		default: // 3 and more
			double deltaSpeed = this.getSpeedAt(size - 2)
					- this.getSpeedAt(size - 3);
			double deltaTime = this.getTimeBetween(size - 3, size - 1);
			double accel = deltaSpeed / deltaTime;

			return accel;
		}
	}

	/**
	 * Computes and returns the dilated speed based on the two most recent
	 * waypoints.
	 * 
	 * @return speed in meters / second
	 */
	public double getLatestSpeed() {
		int size = pointList_.size();
		if (size < 2)
			return 0.0;
		return getSpeedAt(size - 2);
	}
	
	/**
	 * Get a handle to the internal list of waypoints. 
	 * @return
	 */
	public List<Waypoint> getList() {
		return pointList_;
	}

	/**
	 * Compute and return the dilated speed between the point passed in, and the
	 * point immediately after it.
	 * 
	 * @param start
	 *            Index of the first point
	 * @return dilated speed in meters / second.
	 */
	private double getSpeedAt(int start) {
		int end = start + 1;
		if (start < 0 || start > pointList_.size() - 1 || end < 0
				|| end > pointList_.size() - 1 || end < start)
			throw new IllegalArgumentException(
					"The index bounds used are not valid: start:" + start
							+ ", end:" + end + ", size:" + pointList_.size());

		float[] result = new float[1];
		Location.distanceBetween(pointList_.get(start).getLatitude(),
				pointList_.get(start).getLongitude(), pointList_.get(end)
						.getLatitude(), pointList_.get(end).getLongitude(),
				result);

		double distance = result[0];
		double time = getTimeBetween(start, end);

		return distance / time;
	}

	/**
	 * Compute and return the total dilated time between the start and end
	 * waypoints.
	 * 
	 * @param start
	 *            index of the start waypoint
	 * @param end
	 *            index of the end waypoint
	 * @return dilated time in seconds
	 */
	private double getTimeBetween(int start, int end) {
		long time = pointList_.get(end).getTime()
				- pointList_.get(start).getTime();

		if (time < 0)
			throw new IllegalArgumentException("Start must be before end");

		time = time / 1000; // convert ms to seconds
		return (double) time / timeDilation_;
	}

	/**
	 * Set the dilation factor. 
	 * @param d
	 */
	public void setDilation(double d) {
		timeDilation_ = d;
	}

}
