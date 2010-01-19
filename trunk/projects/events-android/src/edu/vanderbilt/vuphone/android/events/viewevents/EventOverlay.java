/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;

import edu.vanderbilt.vuphone.android.events.Constants;
import edu.vanderbilt.vuphone.android.events.R;
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
	// TODO - toss all of this, and use a normal overlay. I have no good way to map the positions in the
	// 		hashMap to the items in the HashMap. I could use an arraylist, but this still does not fix the 
	// 		race condition between calling populate and calling draw. 
	
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventOverlay: ";

	/** Used for filtering events */
	private PositionFilter positionFilter_;
	private TimeFilter timeFilter_;
	private TagsFilter tagsFilter_;

	/** Holds the currently showing overlay items */
	private HashMap<Integer, EventOverlayItem> items_ = new HashMap<Integer, EventOverlayItem>();

	/** Used to get events that match the current filters */
	private DBAdapter database_;

	/** Handle to the map that we are on */
	EventViewerMap map;

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
			TagsFilter tagsFilter, Context context, EventViewerMap map) {
		super(boundCenterBottom(context.getResources().getDrawable(
				R.drawable.map_marker_v)));

		this.map = map;
		positionFilter_ = positionFilter;
		timeFilter_ = timeFilter;
		tagsFilter_ = tagsFilter;

		FilterManager.registerFilterListener(this);

		database_ = new DBAdapter(context);
		database_.openReadable();

		Cursor dbCursor = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);

		while (dbCursor.moveToNext())
			items_.put(new Integer(dbCursor.getInt(dbCursor
					.getColumnIndex(DBAdapter.COLUMN_ID))), EventOverlayItem
					.getItemFromRow(dbCursor));

		dbCursor.close();

		populate();
	}

	/**
	 * @see com.google.android.maps.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected EventOverlayItem createItem(int arg0) {
		synchronized (items_) {
			return items_.get(new Integer(arg0));
		}
	}

	@Override
	protected boolean onTap(int index) {

		Log.d(tag, pre + "onTap called with index " + index);

		synchronized (items_) {
			setFocus(items_.get(new Integer(index)));
		}

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
			FilterManager
					.unregisterFilterListener((PositionFilterListener) this);

		positionFilter_ = p;
		if (positionFilter_ != null)
			FilterManager.registerFilterListener((PositionFilterListener) this);

		timeFilter_ = t;
		tagsFilter_ = ts;

		Cursor c = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);
		mergeHashMapAndCursor(c);
		c.close();

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
		// TODO GO fix issue where clicking on a marker makes the Zoom In/Out
		// controls not work. Just
		// TODO remove the zoom in/out controls!
		// TODO

		int touchLeft = centerX - width / 2;
		int touchTop = centerY - height / 2;

		touchableBounds.set(touchLeft - buffer / 2, touchTop - buffer / 2,
				touchLeft + width + buffer / 2, touchTop + height + buffer / 2);

		return touchableBounds.contains(hitX, hitY);
	}

	/**
	 * @see com.google.android.maps.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		synchronized (items_) {
			return items_.size();	
		}
	}

	/**
	 * @see edu.vanderbilt.vuphone.android.events.filters.PositionFilterListener#filterUpdated(edu.vanderbilt.vuphone.android.events.filters.PositionFilter)
	 */
	public void filterUpdated(PositionFilter filter) {
		Log.i(tag, pre + "PositionFilter was updated");
		
		positionFilter_ = filter;
		Cursor c = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);
		mergeHashMapAndCursor(c);
		c.close();

		setLastFocusedIndex(-1);
		populate();

		map.invalidate();
	}

	/**
	 * @see edu.vanderbilt.vuphone.android.events.filters.TimeFilterListener#filterUpdated(edu.vanderbilt.vuphone.android.events.filters.TimeFilter)
	 */
	public void filterUpdated(TimeFilter filter) {
		Log.i(tag, pre + "TimeFilter was updated");
		

		timeFilter_ = filter;
		Cursor c = database_.getAllEntries(positionFilter_, timeFilter_,
				tagsFilter_);
		mergeHashMapAndCursor(c);
		c.close();

		setLastFocusedIndex(-1);
		populate();

		map.invalidate();
	}

	/**
	 * Fetched the event from the database, and adds it to the map
	 * 
	 * @param rowId
	 */
	protected void addItem(long rowId) {
		final Integer i = new Integer((int) rowId);
		if (items_.get(i) == null)
			items_.put(i, EventOverlayItem.getItemFromRow(database_.getSingleRowCursor(rowId)));
	}
	
	private void mergeHashMapAndCursor(Cursor c) {
		ArrayList<Integer> ids = new ArrayList<Integer>(c.getCount());

		// Easiest way to understand this is to draw a Venn diagram. The end
		// result is that the HashMap mirrors the ArrayList. To get there, if 
		// it is in the AL, but not the HM, you add it to the HM. If it is in
		// the HM, but not the AL, you remove it from the HM
		while (c.moveToNext())
			ids.add(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_ID)));
		
		Integer id;
		synchronized (items_) {
			Iterator<Integer> currentIds = items_.keySet().iterator();

			while (currentIds.hasNext()) {
				id = currentIds.next();
				if (ids.contains(id) == false)
					// its not in the list we want
					items_.remove(id);
				else if (items_.containsKey(id) == false) {
					final int lat = c.getInt(c
							.getColumnIndex(DBAdapter.COLUMN_LOCATION_LAT));
					final int lon = c.getInt(c
							.getColumnIndex(DBAdapter.COLUMN_LOCATION_LON));

					final GeoPoint point = new GeoPoint(lat, lon);
					final String name = c.getString(c
							.getColumnIndex(DBAdapter.COLUMN_NAME));
					final String startTime = c.getString(c
							.getColumnIndex(DBAdapter.COLUMN_START_TIME));
					final String endTime = c.getString(c
							.getColumnIndex(DBAdapter.COLUMN_END_TIME));
					final int isOwnerInt = c.getInt(c
							.getColumnIndex(DBAdapter.COLUMN_IS_OWNER));
					final long row = c.getLong(c
							.getColumnIndex(DBAdapter.COLUMN_ID));
					items_.put(id, new EventOverlayItem(point, name, startTime,
							endTime, isOwnerInt, row));
				}
			}

		}
		
		c.close();
	}
}
