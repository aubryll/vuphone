/**
 * 
 */
package org.vuphone.wwatch.android.mapview;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.http.HTTPGetter;

import android.util.Log;

/**
 * @author Hamilton Turner
 * 
 */
public class CacheUpdater {
	/** Used for logging */
	private String pre = "CacheFullUpdater: ";
	private static final String tag = VUphone.tag;

	/** The time in milliseconds between updates */
	private static final int period_ = 1000 * 15;

	/** The timer used to run the update */
	private Timer timer_;

	private Cache cache_;

	protected CacheUpdater(Cache c) {
		timer_ = new Timer();
		cache_ = c;
	}

	private TimerTask getNewTask() {
		return new TimerTask() {
			public void run() {
				performUpdate();
				timer_.schedule(getNewTask(), period_);
			}
		};
	}

	protected void start() {
		Log.i(tag, pre + "Starting Full Update");
		
		// To be sure that timer dies
		timer_.cancel();

		timer_ = new Timer();
		timer_.schedule(getNewTask(), 0);
	}

	protected void stop() {
		Log.i(tag, pre + "Stopping Full Update");
		timer_.cancel();
	}
	
	protected void forceUpdate() {
		Log.i(tag, pre + "Forcing update");
		stop();
		start();
	}

	private void performUpdate() {
		Log.d(tag, pre + "Performing full update");

		final List<Waypoint> points = HTTPGetter.doWreckGet(cache_.getRegion(),
				cache_.getLatestTime());
		if (points == null) {
			Log.w(tag, pre + "Unable to do update: HTTPGetter returned null");
			return;
		}

		long latestTime = 0;
		// Update the latest time
		for (Waypoint wp : points)
			if (wp.getTime() > latestTime)
				latestTime = wp.getTime();

		CacheUpdate cu = new CacheUpdate(points, latestTime,
				CacheUpdate.TYPE_FULL_UPDATE);

		cache_.acceptCacheUpdate(cu);
	}
}
