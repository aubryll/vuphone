package org.vuphone.wwatch.android.mapview;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.http.HTTPGetter;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class CacheExpander extends Thread {
	/** Used for logging */
	private String pre = "CacheExpander: ";
	private static final String tag = VUphone.tag;

	private String pre() {
		return pre + Thread.currentThread().getName() + ": ";
	}

	/**
	 * Contains all of the regions that the map has viewed. Handles them one by
	 * one to determine if they need a cache update
	 */
	private BlockingQueue<GeoRegion> expansions_ = new LinkedBlockingQueue<GeoRegion>();

	/** A flag indicating whether or not this thread should terminate */
	private volatile boolean shouldTerminate_ = false;

	/**
	 * The maximum cache expansion (in percent) that is allowed in any one
	 * direction. If an expansion would exceed this value, then the cache is
	 * cleared
	 */
	// TODO - find a good maxArea value
	private double maxArea = 0;

	/**
	 * The safe region is defined as the rectangle within the currently cached
	 * rectangle (defined by topLeft and bottomRight) that the user can scroll
	 * around inside without any actions. If the user scrolls around outside
	 * this region, this will trigger a request to increase the size of the
	 * rectangular region, to pre()-cache the points.
	 * 
	 * Although this is not an accurate translation, the scale factor provided
	 * here can be easily understood by assuming that the number represents the
	 * percentage of the cache area that is remaining for buffering. The lower
	 * this number, the larger the safe region will be, and the more likely that
	 * a user will actually encounter the edge of the map. The smaller, the less
	 * likely that the user will encounter the edge of the cache region before
	 * an update returns. by removing safeRegionSize percentage from each of the
	 * four sides of the real size.
	 * 
	 * Should be 0 < safeRegionSize_ < 1
	 */
	private double safeRegionSize = 0.2;

	private Cache cache_;

	protected CacheExpander(Cache cache) {
		cache_ = cache;
	}

	// TODO - Implement scanning the queue to see if an update should be
	// performed, before we fire the expensive POST
	public void run() {
		try {
			while (!shouldTerminate_) {
				final GeoRegion ce = expansions_.take();
				Log.d(tag, pre() + "Took a GeoRegion");
				Log.d(tag, pre() + " Count is " + expansions_.size());

				// This allows more points to make it into the queue, and
				// ultimately saves on rapid back-to-back HTTP operations
				// resulting from a single large scroll operation that posts a
				// lot of GeoRegions
				sleep(1000);

				// TODO - wrap this in an if, and if the expansion was needed,
				// then check the area to see if we should ask for a new cache
				expandIfNeeded(ce);
			}
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Checks the region to see if it is contained with
	 * 
	 * @param ce
	 * @return true if an expansion was needed, false otherwise
	 */
	private boolean expandIfNeeded(final GeoRegion ce) {
		// NOTE: Please do not remove the getSCR() or getCR() shortcut calls.
		// They need to be made, so we always fetch the latest cache

		boolean updateNeeded = false;
		if (getSCR().isPointBelow(ce.getBottomRight())
				&& isFarthestDown(ce.getBottomRight())) {

			Log.d(tag, pre() + "Expanding down");
			updateNeeded = true;
			final GeoRegion expandRegion = findRegionBelow(getCR(), ce
					.getBottomRight());

			final CacheUpdate update = getRoutes(expandRegion,
					CacheUpdate.TYPE_EXPAND_DOWN);
			sendCacheUpdate(update);
		}

		if (getSCR().isPointToRight(ce.getBottomRight())
				&& isFarthestRight(ce.getBottomRight())) {
			Log.d(tag, pre() + "Expanding right");
			updateNeeded = true;
			final GeoRegion expandRegion = findRegionToRight(getCR(), ce
					.getBottomRight());

			final CacheUpdate update = getRoutes(expandRegion,
					CacheUpdate.TYPE_EXPAND_RIGHT);
			sendCacheUpdate(update);
		}

		if (getSCR().isPointAbove(ce.getTopLeft())
				&& isFarthestUp(ce.getTopLeft())) {
			Log.d(tag, pre() + "Expanding up");
			updateNeeded = true;
			final GeoRegion expandRegion = findRegionAbove(getCR(), ce
					.getTopLeft());

			final CacheUpdate update = getRoutes(expandRegion,
					CacheUpdate.TYPE_EXPAND_UP);
			sendCacheUpdate(update);
		}

		if (getSCR().isPointToLeft(ce.getTopLeft())
				&& isFarthestLeft(ce.getTopLeft())) {
			Log.d(tag, pre() + "Expanding left");
			updateNeeded = true;
			final GeoRegion expandRegion = findRegionToLeft(getCR(), ce
					.getTopLeft());

			final CacheUpdate update = getRoutes(expandRegion,
					CacheUpdate.TYPE_EXPAND_LEFT);
			sendCacheUpdate(update);
		}

		if (updateNeeded) {
			Log.d(tag, pre() + "New Region - " + getCR());
		}
		Log.d(tag, pre() + "Done handling point");
		return updateNeeded;
	}

	private boolean isFarthestRight(final GeoPoint point) {
		int right = point.getLongitudeE6();
		Iterator<GeoRegion> i = expansions_.iterator();
		while (i.hasNext())
			if (right < i.next().getBottomRight().getLongitudeE6())
				return false;
		return true;
	}

	private boolean isFarthestLeft(final GeoPoint point) {
		int left = point.getLongitudeE6();
		Iterator<GeoRegion> i = expansions_.iterator();
		while (i.hasNext())
			if (left > i.next().getBottomRight().getLongitudeE6())
				return false;
		return true;
	}

	private boolean isFarthestDown(final GeoPoint point) {
		int down = point.getLatitudeE6();
		Iterator<GeoRegion> i = expansions_.iterator();
		while (i.hasNext())
			if (down > i.next().getBottomRight().getLatitudeE6())
				return false;
		return true;
	}

	private boolean isFarthestUp(final GeoPoint point) {
		int up = point.getLatitudeE6();
		Iterator<GeoRegion> i = expansions_.iterator();
		while (i.hasNext())
			if (up < i.next().getBottomRight().getLatitudeE6())
				return false;
		return true;
	}

	public void terminate() {
		shouldTerminate_ = true;
	}

	private GeoRegion getCR() {
		return cache_.getRegion();
	}

	private GeoRegion getSCR() {
		return cache_.getRegion().getSafeRegion(safeRegionSize);
	}

	private CacheUpdate getRoutes(final GeoRegion region,
			final int cacheUpdateType) {
		final List<Route> routes = HTTPGetter.doWreckGet(region, 0);
		if (routes == null) {
			Log.w(tag, pre() + "Unable to do update: HTTPGetter returned null");
			return null;
		}

		long time = 0;
		for (Route r : routes)
			if (r.getWreck().getTime() > time)
				time = r.getWreck().getTime();

		int value;
		switch (cacheUpdateType) {
		case CacheUpdate.TYPE_EXPAND_DOWN:
			value = region.getBottomRight().getLatitudeE6();
			break;
		case CacheUpdate.TYPE_EXPAND_LEFT:
			value = region.getTopLeft().getLongitudeE6();
			break;
		case CacheUpdate.TYPE_EXPAND_RIGHT:
			value = region.getBottomRight().getLongitudeE6();
			break;
		case CacheUpdate.TYPE_EXPAND_UP:
			value = region.getTopLeft().getLatitudeE6();
			break;
		default:
			return null;
		}

		final CacheUpdate cu = new CacheUpdate(routes, time, cacheUpdateType,
				value);

		return cu;
	}

	// final double newLatSpan = Math.abs(getTopLeft().getLatitudeE6()
	// - newLat);
	// double ratio = Math.abs(newLatSpan / latitudeSpan);
	private GeoRegion findRegionBelow(final GeoRegion cacheRegion,
			final GeoPoint pointBelowRegion) {

		// Find the buffer space needed to keep our safe region scaled
		final int buffer = (int) ((double) cacheRegion.latitudeSpan() * safeRegionSize);

		final int cacheBottom = cacheRegion.getBottomRight().getLatitudeE6();
		final int cacheLeft = cacheRegion.getTopLeft().getLongitudeE6();
		final GeoPoint topLeft = new GeoPoint(cacheBottom, cacheLeft);

		final int newBottom = pointBelowRegion.getLatitudeE6() - buffer;
		final int cacheRight = cacheRegion.getBottomRight().getLongitudeE6();
		final GeoPoint bottomRight = new GeoPoint(newBottom, cacheRight);

		return new GeoRegion(topLeft, bottomRight);
	}

	// double ratio = Math.abs(newLatSpan / latitudeSpan);
	// final double newLatSpan = Math.abs(newLat
	// - getBottomRight().getLatitudeE6());
	private GeoRegion findRegionAbove(final GeoRegion cacheRegion,
			final GeoPoint pointAboveRegion) {

		// Find the buffer space needed to keep our safe region scaled
		final int buffer = (int) ((double) cacheRegion.latitudeSpan() * safeRegionSize);

		// Find top left
		final int updatedTop = pointAboveRegion.getLatitudeE6() + buffer;
		final int cacheLeft = cacheRegion.getTopLeft().getLongitudeE6();
		final GeoPoint topLeft = new GeoPoint(updatedTop, cacheLeft);

		// Find bottom right
		final int cacheTop = cacheRegion.getTopLeft().getLatitudeE6();
		final int cacheRight = cacheRegion.getBottomRight().getLongitudeE6();
		final GeoPoint bottomRight = new GeoPoint(cacheTop, cacheRight);

		return new GeoRegion(topLeft, bottomRight);
	}

	// final double newLngSpan = Math.abs(newLng
	// - getTopLeft().getLongitudeE6());
	// double ratio = Math.abs(newLngSpan / longitudeSpan);
	private GeoRegion findRegionToRight(final GeoRegion cacheRegion,
			final GeoPoint pointToRight) {

		// Find the buffer space needed to keep our safe region scaled
		final int buffer = (int) ((double) cacheRegion.longitudeSpan() * safeRegionSize);

		// Find top left
		final int cacheTop = cacheRegion.getTopLeft().getLatitudeE6();
		final int cacheRight = cacheRegion.getBottomRight().getLongitudeE6();
		final GeoPoint topLeft = new GeoPoint(cacheTop, cacheRight);

		// Find bottom right
		final int cacheBottom = cacheRegion.getBottomRight().getLatitudeE6();
		final int updatedRight = pointToRight.getLongitudeE6() + buffer;
		final GeoPoint bottomRight = new GeoPoint(cacheBottom, updatedRight);

		return new GeoRegion(topLeft, bottomRight);
	}

	// final double newLngSpan = Math.abs(getBottomRight()
	// .getLongitudeE6() - newLng);
	// double ratio = Math.abs(newLngSpan / longitudeSpan);
	private GeoRegion findRegionToLeft(final GeoRegion cacheRegion,
			final GeoPoint pointToleft) {

		// Find the buffer space needed to keep our safe region scaled
		final int buffer = (int) ((double) cacheRegion.longitudeSpan() * safeRegionSize);

		final int cacheTop = cacheRegion.getTopLeft().getLatitudeE6();
		final int newLeft = pointToleft.getLongitudeE6() - buffer;
		final GeoPoint topLeft = new GeoPoint(cacheTop, newLeft);

		final int cacheBottom = cacheRegion.getBottomRight().getLatitudeE6();
		final int cacheLeft = cacheRegion.getTopLeft().getLongitudeE6();
		final GeoPoint bottomRight = new GeoPoint(cacheBottom, cacheLeft);

		return new GeoRegion(topLeft, bottomRight);
	}

	private void sendCacheUpdate(final CacheUpdate cu) {
		if (cu == null)
			Log.w(tag, pre() + "No CacheUpdate returned, "
					+ "likely a HTTP error occurred");
		else {
			Log.d(tag, pre() + "Delivering Cache Update");
			cache_.acceptCacheUpdate(cu);
		}
	}

	public void putPossibleExpansion(final GeoRegion gr) {
		try {
			expansions_.put(gr);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
			Log.e(tag, pre() + "Unable to put a GeoRegion");
		}
	}

}
