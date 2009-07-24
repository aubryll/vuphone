/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.eventstore.DBAdapter;

import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * Used to represent one event.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventOverlayItem extends OverlayItem {
	/** Used for logging */
//	private static final String tag = Constants.tag;
//	private static final String pre = "EventOverlayItem: ";

	private EventOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}

	/**
	 * Used to create an EventOverlayItem
	 * 
	 * @param c
	 *            a Cursor that is assumed to be on a valid database row, which
	 *            will be used to fetch the values used to populate the
	 *            EventOverlayItem
	 * @return
	 */
	public static EventOverlayItem getItemFromRow(Cursor c) {
		int lat = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LOCATION_LAT));
		int lon = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LOCATION_LON));
		String name = c.getString(c.getColumnIndex(DBAdapter.COLUMN_NAME));

		final GeoPoint location = new GeoPoint(lat, lon);
		return new EventOverlayItem(location, name, "");
	}

	/** @see com.google.android.maps.OverlayItem#getMarker(int) */
	@Override
	public Drawable getMarker(int bitStateset) {
		return null;
	}
}
