/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import edu.vanderbilt.vuphone.android.events.Constants;
import edu.vanderbilt.vuphone.android.events.R;
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
		TimeFilterListener {

	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventOverlay: ";

	/** Used for filtering events */
	private PositionFilter positionFilter_;
	private TimeFilter timeFilter_;
	private TagsFilter tagsFilter_;

	/** Used to get events that match the current filters */
	private final DBAdapter database_;
	
	private final MapView mapView_;

	/** Map icon */
	private final Drawable mapIcon_;

	private ArrayList<EventPin> items_;

	public EventOverlay(PositionFilter positionFilter, TimeFilter timeFilter,
			TagsFilter tagsFilter, MapView mapView) {

		mapIcon_ = mapView.getResources().getDrawable(R.drawable.map_marker_v);
		mapIcon_.setBounds(0, 0, mapIcon_.getIntrinsicWidth(), mapIcon_
				.getIntrinsicHeight());

		database_ = new DBAdapter(mapView.getContext());
		database_.openReadable();

		items_ = new ArrayList<EventPin>();
		mapView_ = mapView;
		
		receiveNewFilters(positionFilter, timeFilter, tagsFilter);

	}

	/* Timing */
	private long mStartTime = -1;
	private int mCounter;
	private int mFps;
	private int count = 0;

	@Override
	public void draw(android.graphics.Canvas canvas, MapView mapView,
			boolean shadow) {
		
		// Begin timing
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

		final Projection projection = mapView.getProjection();
		Point point = new Point();
		Log.v(tag, pre + "Count: " + ++count + ", fps: " + mFps);

		synchronized (items_) {
			Log.v(tag, pre + "Num items: " + items_.size());
			for (EventPin pin : items_) {
				projection.toPixels(pin.getLocation(), point);

				// TODO Drawing shadows does not
				// work w/o boundCenterBottom properly called
				// drawAt(canvas, d, po.x, po.y, true);
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
		final ArrayList<EventPin> newItems_ = new ArrayList<EventPin>();
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
	 * Fetched the event from the database, and adds it to the map
	 * 
	 * @param rowId
	 */
	protected void addItem(long rowId) {
		
		final Cursor c = database_.getSingleRowCursor(rowId);
		EventPin pin = EventPin.getItemFromRow(c);
		synchronized (items_) {
			items_.add(pin);
		}
		
		mapView_.postInvalidate();
	}

}
