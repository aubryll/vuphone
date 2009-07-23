/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.eventstore.DBAdapter;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * @author Hamilton Turner
 * 
 */
public class EventOverlayItem extends OverlayItem {
	private static final String tag = Constants.tag;
	private static final String pre = "EventOverlayItem: ";
	
	/**
	 * @param point
	 * @param title
	 * @param snippet
	 */
	private EventOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}

	public static EventOverlayItem getItemFromRow(Cursor c) {
		int lat = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LOCATION_LAT));
		int lon = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LOCATION_LON));
		String name = c.getString(c.getColumnIndex(DBAdapter.COLUMN_NAME));

		final GeoPoint location = new GeoPoint(lat, lon);
		return new EventOverlayItem(location, name, "");
	}

	@Override
	public Drawable getMarker(int bitStateset) {
		Log.v(tag, pre + "returning null");
		return null;
	}
}
