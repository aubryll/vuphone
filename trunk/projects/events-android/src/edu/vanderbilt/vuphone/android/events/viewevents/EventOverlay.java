/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import java.util.TreeSet;

import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import edu.vanderbilt.vuphone.android.events.Constants;
import edu.vanderbilt.vuphone.android.events.R;
import edu.vanderbilt.vuphone.android.events.eventloader.EventLoader;
import edu.vanderbilt.vuphone.android.events.eventloader.LoadingListener;
import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;
import edu.vanderbilt.vuphone.android.events.filters.PositionFilter;
import edu.vanderbilt.vuphone.android.events.filters.PositionFilterListener;
import edu.vanderbilt.vuphone.android.events.filters.TagsFilter;
import edu.vanderbilt.vuphone.android.events.filters.TimeFilter;
import edu.vanderbilt.vuphone.android.events.filters.TimeFilterListener;

/**
 * 
 * @author Hamilton Turner
 * 
 */
public class EventOverlay extends Overlay implements PositionFilterListener,
		TimeFilterListener, LoadingListener {

	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventOverlay: ";

	/** Used for filtering events */
	private PositionFilter positionFilter_;
	private TimeFilter timeFilter_;
	private TagsFilter tagsFilter_;

	/** Used to get events that match the current filters */
	private final DBAdapter database_;

	/** Handle to the mapView that we are overlaid on */
	private final MapView mapView_;

	/** Standard Map pin icon */
	private final Drawable mapIcon_;

	/**
	 * The list of items to be drawn. Access should be synchronized. Sorted, to
	 * allow us to draw the pins from top to bottom of the screen
	 */
	private TreeSet<EventPin> items_;

	/* Timing, take out of production code */
	 private long mStartTime = -1;
	 private int mCounter;
	 private int mFps;
	 private int count = 0;

	public EventOverlay(PositionFilter positionFilter, TimeFilter timeFilter,
			TagsFilter tagsFilter, MapView mapView) {

		mapIcon_ = mapView.getResources().getDrawable(R.drawable.map_marker_v);
		mapIcon_.setBounds(-mapIcon_.getIntrinsicWidth() / 2, -mapIcon_
				.getIntrinsicHeight(), mapIcon_.getIntrinsicWidth() / 2, 0);

		database_ = new DBAdapter(mapView.getContext());
		database_.openReadable();

		items_ = new TreeSet<EventPin>();
		mapView_ = mapView;

		receiveNewFilters(positionFilter, timeFilter, tagsFilter);

		EventLoader.registerLoadingListener(this);

	}

	@Override
	public void draw(android.graphics.Canvas canvas, MapView mapView,
			boolean shadow) {

		if (shadow)
			return;

		 //Start timing code
		 if (mStartTime == -1) {
		 mStartTime = SystemClock.elapsedRealtime();
		 mCounter = 0;
		 }
		
		 final long now = SystemClock.elapsedRealtime();
		 final long delay = now - mStartTime;
		
		 if (delay > 1000l) {
		 mStartTime = now;
		 mFps = mCounter;
		 mCounter = 0;
		 }
		 ++mCounter;
		 //Done timing code

		final Projection projection = mapView.getProjection();
		final Point point = new Point();
		Log.v(tag, pre + "Count: " + ++count + ", fps: " + mFps);

		synchronized (items_) {
			for (EventPin pin : items_) {
				projection.toPixels(pin.getLocation(), point);

				// Note that technically this occasionally overlays the
				// shadow of one pin on top of another pin. Don't really care
				drawAt(canvas, mapIcon_, point.x, point.y, true);
				drawAt(canvas, mapIcon_, point.x, point.y, false);
			}
		}

	}

	/**
	 * Used to pass new filters into the overlay. Any of the variables can be
	 * null to keep the current filter. The DB is queried and the overlay list
	 * re-populated every time we call this, so rather than having three
	 * distinct methods we have one where multiple filters can be updated at
	 * once.
	 * 
	 * @param p
	 *            a new PositionFilter, or null
	 * @param t
	 *            a new TimeFilter, or null
	 * @param ts
	 *            a new PositionFilter, or null
	 */
	protected void receiveNewFilters(PositionFilter p, TimeFilter t,
			TagsFilter ts) {

		// Set filters
		if (p != null)
			positionFilter_ = p;
		if (t != null)
			timeFilter_ = t;
		if (ts != null)
			tagsFilter_ = ts;

		// Get new elements
		final TreeSet<EventPin> newItems_ = new TreeSet<EventPin>();
		Cursor c = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);
		while (c.moveToNext())
			newItems_.add(EventPin.getItemFromRow(c));
		c.close();

		// Replace old list
		synchronized (items_) {
			items_ = null;
			items_ = newItems_;
		}
	}

	/**
	 * @see edu.vanderbilt.vuphone.android.events.filters.PositionFilterListener#filterUpdated(edu.vanderbilt.vuphone.android.events.filters.PositionFilter)
	 */
	public void filterUpdated(PositionFilter filter) {
		Log.i(tag, pre + "PositionFilter was updated");
		receiveNewFilters(filter, null, null);
	}

	/**
	 * @see edu.vanderbilt.vuphone.android.events.filters.TimeFilterListener#filterUpdated(edu.vanderbilt.vuphone.android.events.filters.TimeFilter)
	 */
	public void filterUpdated(TimeFilter filter) {
		Log.i(tag, pre + "TimeFilter was updated");
		receiveNewFilters(null, filter, null);
	}

	/**
	 * Checks to see if the state indicates that a single event was added. If it
	 * does, then this method adds that event to the list of items to be drawn
	 * and requests an update
	 * 
	 * @see edu.vanderbilt.vuphone.android.events.eventloader.LoadingListener#OnEventLoadStateChanged(edu.vanderbilt.vuphone.android.events.eventloader.LoadingListener.LoadState,
	 *      java.lang.Long)
	 */
	public void OnEventLoadStateChanged(LoadState l, Long rowId) {
		if (l != LoadState.ONE_EVENT)
			return;

		final Cursor c = database_.getSingleRowCursor(rowId);
		EventPin pin = EventPin.getItemFromRow(c);
		synchronized (items_) {
			items_.add(pin);
		}

		mapView_.postInvalidate();
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		Log.v(tag, pre + "OnTap");
		return super.onTap(p, mapView);
	}

}
