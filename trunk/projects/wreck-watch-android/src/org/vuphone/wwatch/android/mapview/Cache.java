package org.vuphone.wwatch.android.mapview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.http.HTTPGetter;
import org.vuphone.wwatch.android.mapview.pinoverlays.PinController;

import android.content.Context;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

/**
 * A data structure to hold routes that have been retrieved from the server. The
 * map moving causes an HTTP Post operation, which completes and returns data to
 * this class.
 * 
 * This class calls on the Overlay when it has new data to be displayed. The map
 * determines which data it would like to retrieve and makes the appropriate
 * call
 * 
 */
public class Cache {
	/** Used for logging */
	private String pre = "Cache: ";
	private static final String tag = VUphone.tag;

	private String pre() {
		return pre + Thread.currentThread().getName() + ": ";
	}

	/** The cached routes. This is synchronized internally */
	private List<Waypoint> points_ = new ArrayList<Waypoint>();

	/**
	 * The Overlay displaying the wrecks or routes. This is used to set a flag
	 * on the Overlay indicating that there is new Route data available, and the
	 * Overlay should request is at their convenience
	 */
	private PinController overlay_;

	/**
	 * Used to set context on the waypoints we receive. This allows the
	 * waypoints to access resources and find their drawables
	 */
	private Context context_;

	private CacheExpander expander_;

	/**
	 * The current top left and bottom right of the rectangle of cached Routes.
	 * This will expand as the user scrolls around, and more Routes are cached.
	 * Use getter and setter to synchronize access. (These can be null, so a
	 * synchronization block is not an option)
	 */
	private GeoRegion region_ = null;

	/**
	 * The maximum zoom level at which an update will be requested. This
	 * prevents the user from zooming all the way out and requesting every wreck
	 */
	private int maxZoom = 8;

	/** Used to periodically update the map. */
	private TimerTask periodicUpdate_;

	/** Used to run the periodic update */
	private Timer updateTimer_;

	/** The time in milliseconds between updates */
	private static final int updateTime_ = 1000 * 15; // 3 seconds

	/** Keeps track of the current largest time that we can use for full updates */
	private Long latestTime_ = (long) 0;

	/**
	 * 
	 * @param overlay
	 */
	public Cache(PinController overlay, Context context) {
		overlay_ = overlay;
		context_ = context;
	}

	public synchronized void acceptCacheUpdate(final CacheUpdate cu) {
		Log.i(tag, pre() + "Receiving Cache Update");
		if (region_ == null) {
			Log.i(tag, pre() + "Rejecting CacheUpdate because"
					+ " cache region is null");
			return;
		}

		switch (cu.getType()) {
		case CacheUpdate.TYPE_EXPAND_DOWN:
			setBottomRight(new GeoPoint(cu.getNewValue(), getRegion()
					.getBottomRight().getLongitudeE6()));
			break;
		case CacheUpdate.TYPE_EXPAND_LEFT:
			setTopLeft(new GeoPoint(getRegion().getTopLeft().getLatitudeE6(),
					cu.getNewValue()));
			break;
		case CacheUpdate.TYPE_EXPAND_RIGHT:
			setBottomRight(new GeoPoint(getRegion().getBottomRight()
					.getLatitudeE6(), cu.getNewValue()));
			break;
		case CacheUpdate.TYPE_EXPAND_UP:
			setTopLeft(new GeoPoint(cu.getNewValue(), getRegion().getTopLeft()
					.getLongitudeE6()));
			break;
		default:
			Log.e(tag, pre() + "CacheUpdate not understood");
			return;
		}

		addWrecks(cu.getWrecks());

		if (cu.getLatestTime() < latestTime_)
			latestTime_ = cu.getLatestTime();

	}

	/**
	 * Handles parsing the server response, merging the data, and notifying the
	 * overlay that there are new data points
	 */
	private void addWrecks(List<Waypoint> wrecks) {
		// Do we have any new routes to add
		if (wrecks.size() == 0)
			return;

		// Set all Waypoints context
		for (Waypoint wp : wrecks)
			wp.setContext(context_);

		synchronized (points_) {
			points_.addAll(wrecks);
		}
		overlay_.updateWrecks(points_);
	}

	/**
	 * Called to clear the entire cache, and restart caching fully.
	 */
	public synchronized void clearCache() {
		Log.e(tag, pre() + "Cache clear is not implemented yet");
		latestTime_ = new Long(0);
		
		synchronized (points_) {
			points_ = new ArrayList<Waypoint>();
		}
		
		region_ = null;
	}

	/**
	 * A thread safe method to access the current region of the cache
	 * 
	 * @return
	 */
	protected synchronized GeoRegion getRegion() {
		return region_;
	}

	/**
	 * Returns the route associated with a particular wreck
	 * 
	 * @param wreckPoint
	 * @return
	 */
	public Route getRoute(final Waypoint wreckPoint) {
		long id = wreckPoint.getAccidentId();
		return HTTPGetter.doRouteGet(id);
	}

	/**
	 * Called every time the map scrolls. This allows us to handle pre()-caching
	 * for the map
	 * 
	 * @param mv
	 *            The MapView that was scrolled
	 */
	public void onMapScroll(final MapView mv) {
		// Check that the zoom is within bounds
		if (mv.getZoomLevel() < maxZoom) {
			Log.d(tag, pre() + "Map zoom is above maxZoom, ignoring scroll");
			return;
		}

		// Find the current top left and bottom right of the map
		int mapHeight = mv.getHeight();
		int mapWidth = mv.getWidth();
		Projection p = mv.getProjection();

		final GeoPoint upperLeft = p.fromPixels(0, 0);
		final GeoPoint lowerRight = p.fromPixels(mapWidth, mapHeight);
		final GeoRegion region = new GeoRegion(upperLeft, lowerRight);

		// Check in case we have no cache
		if (region_ == null) {

			final double initialLatSpan = region.latitudeSpan();
			final double initialLngSpan = region.longitudeSpan();

			// Just double the initial size
			// TODO - get krzysztof to take a look at the math and find a closer
			// approximation
			// Note that the spans are so large, the significant digits on the
			// scale really matter
			final double scale = 0.35;
			final int initTLLat = upperLeft.getLatitudeE6()
					+ (int) (initialLatSpan * scale);
			final int initTLLng = upperLeft.getLongitudeE6()
					- (int) (initialLngSpan * scale);
			final int initLRLat = lowerRight.getLatitudeE6()
					- (int) (initialLatSpan * scale);
			final int initLRLng = lowerRight.getLongitudeE6()
					+ (int) (initialLngSpan * scale);

			final GeoPoint topLeft = new GeoPoint(initTLLat, initTLLng);
			final GeoPoint bottomRight = new GeoPoint(initLRLat, initLRLng);
			final GeoRegion gr = new GeoRegion(topLeft, bottomRight);
			Log.d(tag, pre() + "Initial lat span: " + gr.latitudeSpan());
			Log.d(tag, pre() + "Initial lng span: " + gr.longitudeSpan());
			setRegion(gr);
			Log.d(tag, pre() + "Assigned initial topLeft and bottomRight");
			Log.d(tag, pre() + "TL:" + getRegion().getTopLeft() + ", BR:"
					+ getRegion().getBottomRight());
			Log.d(tag, pre() + "Firing initial update");
			Thread t = new Thread(new Runnable() {
				public void run() {
					performFullUpdate(true);
				}
			});
			t.setDaemon(true);
			t.setName("Full Cache Update");
			t.start();
			return;
		}

		expander_.putPossibleExpansion(region);

	}

	/**
	 * Called when a full cache update is requested. Ensures that no two update
	 * calls can be running at the same time
	 * 
	 * @param critical
	 *            Whether or not this update must go through
	 */
	private synchronized void performFullUpdate(boolean critical) {

		Log.d(tag, pre() + "Performing full update");

		final List<Waypoint> points = HTTPGetter.doWreckGet(region_,
				latestTime_);
		if (points == null) {
			Log.w(tag, pre() + "Unable to do update: HTTPGetter returned null");
			return;
		}
		addWrecks(points);

		// Update the latest time
		for (Waypoint wp : points)
			if (wp.getTime() > latestTime_)
				latestTime_ = wp.getTime();

	}

	private synchronized void setBottomRight(final GeoPoint p) {
		Log.d(tag, pre() + "Setting BR to " + p);
		region_ = new GeoRegion(region_.getTopLeft(), p);
	}

	private synchronized void setTopLeft(final GeoPoint p) {
		Log.d(tag, pre() + "Setting TL to " + p);
		region_ = new GeoRegion(p, region_.getBottomRight());
	}

	private synchronized void setRegion(final GeoRegion r) {
		region_ = r;
	}

	public void start() {
		Log.i(tag, pre + "Cache started");
		if (expander_ != null)
			expander_.terminate();
		expander_ = new CacheExpander(this);
		expander_.setDaemon(true);
		expander_.setName("Cache Expander");
		expander_.start();
		
		periodicUpdate_ = new TimerTask() {
			public void run() {
				performFullUpdate(false);
			}
		};
		
		// Specifically request this as a daemon thread, so that it will die
		// with the JVM
		updateTimer_ = new Timer("Periodic Cache Updater", true);
		updateTimer_.scheduleAtFixedRate(periodicUpdate_, updateTime_,
				updateTime_);
	}

	public void stop() {
		Log.i(tag, pre + "Cache stopped");
		updateTimer_.cancel();
		periodicUpdate_.cancel();
		periodicUpdate_ = null;
		expander_.terminate();
	}

}
