/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;

import edu.vanderbilt.vuphone.android.events.R;
import edu.vanderbilt.vuphone.android.events.Constants;
import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;
import edu.vanderbilt.vuphone.android.events.filters.FilterManager;
import edu.vanderbilt.vuphone.android.events.filters.PositionFilter;
import edu.vanderbilt.vuphone.android.events.filters.PositionFilterListener;
import edu.vanderbilt.vuphone.android.events.filters.TagsFilter;
import edu.vanderbilt.vuphone.android.events.filters.TimeFilter;
import edu.vanderbilt.vuphone.android.events.filters.TimeFilterListener;

/**
 * Contains the {@link EventOverlayItem}s. Holds a handle to the database, and
 * holds the current display filters. Every time the filters are updated, the
 * database is re-queried
 * 
 * @author Hamilton Turner
 * 
 */
public class EventOverlay extends ItemizedOverlay<EventOverlayItem> implements
		PositionFilterListener, TimeFilterListener {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventOverlay: ";

	/** Used for filtering events */
	private PositionFilter positionFilter_;
	private TimeFilter timeFilter_;
	private TagsFilter tagsFilter_;

	/** Used to get events that match the current filters */
	private DBAdapter database_;

	/** Used to point to the current row in the database */
	private Cursor eventCursor_;

	private static ShapeDrawable defaultDrawable_;

	private Rect touchableBounds = new Rect();

	static {
		defaultDrawable_ = new ShapeDrawable(new RectShape());
		defaultDrawable_.setBounds(10, 10, 10, 10);
		defaultDrawable_.setIntrinsicHeight(50);
		defaultDrawable_.setIntrinsicWidth(20);

	}

	/**
	 * 
	 * @param positionFilter
	 *            Can be NULL.
	 * @param timeFilter
	 *            Can be NULL.
	 * @param tagsFilter
	 *            Can be NULL.
	 * @param context
	 */
	public EventOverlay(PositionFilter positionFilter, TimeFilter timeFilter,
			TagsFilter tagsFilter, Context context) {
		super(boundCenterBottom(context.getResources().getDrawable(R.drawable.map_marker_v)));

		positionFilter_ = positionFilter;
		timeFilter_ = timeFilter;
		tagsFilter_ = tagsFilter;

		if (positionFilter_ != null)
			FilterManager.registerFilterListener((PositionFilterListener)this);

		database_ = new DBAdapter(context);
		database_.openReadable();
		eventCursor_ = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);

		populate();
	}

	/**
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected EventOverlayItem createItem(int arg0) {
		eventCursor_.moveToNext();
		return EventOverlayItem.getItemFromRow(eventCursor_);
	}

	@Override
	protected boolean onTap(int index) {
		Log.d(tag, pre + "onTap called with index " + index);
		return true;
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

		if (positionFilter_ != null)
			FilterManager.unregisterFilterListener((PositionFilterListener)this);

		positionFilter_ = p;
		if (positionFilter_ != null)
			FilterManager.registerFilterListener((PositionFilterListener)this);

		timeFilter_ = t;
		tagsFilter_ = ts;

		eventCursor_ = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);

		populate();
	}

	/**
	 * Used to create a more accurate hittest. The default implementation has a
	 * minimum marker size of 100x100.
	 */
	
	private static int buffer = 15;

	@Override
	protected boolean hitTest(EventOverlayItem item,
			android.graphics.drawable.Drawable marker, int hitX, int hitY) {

		Rect bounds = marker.getBounds();

		int width = bounds.width();
		int height = bounds.height();
		int centerX = bounds.centerX();
		int centerY = bounds.centerY();
		
		// TODO
		// TODO
		// TODO GO fix issue where clicking on a marker makes the Zoom In/Out controls not work. Just
		// TODO remove the zoom in/out controls!
		// TODO
		

		int touchLeft = centerX - width / 2;
		int touchTop = centerY - height / 2;

		touchableBounds.set(touchLeft - buffer/2, touchTop - buffer/2, touchLeft + width + buffer/2, touchTop
				+ height + buffer/2);

		return touchableBounds.contains(hitX, hitY);
	}

	/**
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return eventCursor_.getCount();
	}

	/** 
	 * @see edu.vanderbilt.vuphone.android.events.filters.PositionFilterListener#filterUpdated(edu.vanderbilt.vuphone.android.events.filters.PositionFilter)
	 */
	public void filterUpdated(PositionFilter filter) {
		Log.i(tag, pre + "Filter was updated");
		eventCursor_.close();
		eventCursor_ = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);

		setLastFocusedIndex(-1);
		populate();
	}

	/** 
	 * @see edu.vanderbilt.vuphone.android.events.filters.TimeFilterListener#filterUpdated(edu.vanderbilt.vuphone.android.events.filters.TimeFilter)
	 */
	public void filterUpdated(TimeFilter filter) {
	}
}
