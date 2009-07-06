package org.vuphone.wwatch.android.mapview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 * Note that many of the protected methods are synchronized. The concept is that
 * the Cache is entirely 'locked' whenever something important is happening.
 * This could definitely be optimized for more finer-grained locking, but
 * currently this will block only the CacheUpdater and the CacheExpander, so it
 * is not a critical change.
 * 
 */
public class Cache {
	/** Used for logging */
	private String pre = "Cache: ";
	private static final String tag = VUphone.tag;

	private String pre() {
		return pre + Thread.currentThread().getName() + ": ";
	}

	/** The cached wrecks */
	private List<Waypoint> points_ = Collections
			.synchronizedList(new ArrayList<Waypoint>());

	/**
	 * The Overlay displaying the wrecks. Data is pushed from the Cache onto the
	 * PinController
	 */
	private PinController overlay_;

	/**
	 * Used to set context on the waypoints we receive. This allows the
	 * waypoints to access resources and find their drawables
	 */
	private Context context_;

	/**
	 * Handles determining when the Cache needs to be expanded, performing the
	 * expansion, and returning the Cache an appropriate CacheUpdate. This is
	 * notified every time the map scrolls
	 */
	private CacheExpander expander_;

	/**
	 * Handles updating the full cache periodically. Returns CacheUpdates to the
	 * Cache
	 */
	private CacheUpdater updater_;

	/** The current top left and bottom right of the cached region. */
	private GeoRegion region_ = null;

	/**
	 * The maximum zoom level at which an update will be requested. This
	 * prevents the user from zooming all the way out and requesting every
	 * wreck. Higher values are closer to the earth, 1-20 range.
	 */
	private int maxZoom = 8;

	/**
	 * The current largest time that we can use for full updates. The server
	 * will only return points that occurred after this time, saving on
	 * resources.
	 */
	private Long latestTime_ = (long) 0;

	public Cache(PinController overlay, Context context) {
		overlay_ = overlay;
		context_ = context.getApplicationContext();

		expander_ = new CacheExpander(this);
		expander_.setDaemon(true);
		expander_.setName("Cache Expander");
		expander_.start();

		updater_ = new CacheUpdater(this);
	}

	protected synchronized void acceptCacheUpdate(final CacheUpdate cu) {
		Log.i(tag, pre() + "Receiving Cache Update");
		if (region_ == null) {
			Log.w(tag, pre() + "Rejecting CacheUpdate because"
					+ " cache region is null");
			return;
		}

		if (cu.getWrecks().size() == 0) {
			Log.w(tag, pre() + "Rejecting CacheUpdate because "
					+ "there are no points included");
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
		case CacheUpdate.TYPE_FULL_UPDATE:
			// no-op, but we do understand the update
			break;
		default:
			Log.e(tag, pre() + "CacheUpdate not understood");
			return;
		}

		addWrecks(cu.getWrecks());

		if (cu.getType() == CacheUpdate.TYPE_FULL_UPDATE)
			latestTime_ = cu.getLatestTime();
		else if (cu.getLatestTime() < latestTime_ && cu.getLatestTime() != 0)
			latestTime_ = cu.getLatestTime();
	}

	/**
	 * Handles merging the data, and pushing the new points to the overlay
	 */
	private synchronized void addWrecks(List<Waypoint> wrecks) {
		// Do we have any new routes to add
		if (wrecks.size() == 0)
			return;

		// Set all Waypoints context
		for (Waypoint wp : wrecks)
			wp.setContext(context_);

		points_.addAll(wrecks);

		overlay_.updateWrecks(points_);
	}

	/**
	 * Called to clear the entire cache, and restart caching fully.
	 */
	protected synchronized void clearCache() {
		latestTime_ = new Long(0);

		synchronized (points_) {
			points_ = new ArrayList<Waypoint>();
		}

		region_ = null;
	}

	protected synchronized Long getLatestTime() {
		return latestTime_;
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
	 * Called every time the map scrolls. This allows us to handle pre-caching
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
			updater_.forceUpdate();
			return;
		}

		expander_.putPossibleExpansion(region);

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
		updater_.start();
	}

	public void stop() {
		Log.i(tag, pre + "Cache stopped");

		expander_.quickPause();
		updater_.stop();
	}

}
