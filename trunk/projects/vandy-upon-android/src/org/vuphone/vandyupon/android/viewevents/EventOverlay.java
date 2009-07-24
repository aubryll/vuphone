/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.eventstore.DBAdapter;
import org.vuphone.vandyupon.android.filters.PositionFilter;
import org.vuphone.vandyupon.android.filters.TagsFilter;
import org.vuphone.vandyupon.android.filters.TimeFilter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.google.android.maps.ItemizedOverlay;

/**
 * Contains the {@link EventOverlayItem}s. Holds a handle to the database, and
 * holds the current display filters. Every time the filters are updated, the
 * database is re-queried
 * 
 * @author Hamilton Turner
 * 
 */
public class EventOverlay extends ItemizedOverlay<EventOverlayItem> {
	/** Used for logging */
//	private static final String tag = Constants.tag;
//	private static final String pre = "EventOverlay: ";

	/** Used for filtering events */
	private PositionFilter positionFilter_;
	private TimeFilter timeFilter_;
	private TagsFilter tagsFilter_;
	
	/** Used to get events that match the current filters */
	private DBAdapter database_;
	
	/** Used to point to the current row in the database */
	private Cursor eventCursor_;

	private static ShapeDrawable defaultDrawable_;

	static {
		defaultDrawable_ = new ShapeDrawable(new OvalShape());
		defaultDrawable_.setIntrinsicWidth(20);
		defaultDrawable_.setIntrinsicHeight(20);
	}

	public EventOverlay(PositionFilter positionFilter, TimeFilter timeFilter,
			TagsFilter tagsFilter, Context context) {
		super(boundCenterBottom(defaultDrawable_));

		positionFilter_ = positionFilter;
		timeFilter_ = timeFilter;
		tagsFilter_ = tagsFilter;

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

	/**
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		return eventCursor_.getCount();
	}

	/**
	 * Used to pass new filters into the overlay. Any of the variables can be
	 * null to keep the current filter
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
		if (p != null)
			positionFilter_ = p;
		if (t != null)
			timeFilter_ = t;
		if (ts != null)
			tagsFilter_ = ts;
		
		eventCursor_ = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);

		populate();
	}

}
