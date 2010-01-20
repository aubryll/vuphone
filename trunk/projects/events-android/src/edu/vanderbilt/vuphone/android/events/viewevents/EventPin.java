/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;

/**
 * Used to represent one event.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventPin {
	/** Used for logging */
	// private static final String tag = Constants.tag;
	// private static final String pre = "EventOverlayItem: ";

	// String[] returnValues = { COLUMN_NAME, COLUMN_START_TIME,
	// COLUMN_END_TIME, COLUMN_LOCATION_LAT, COLUMN_LOCATION_LON,
	// COLUMN_IS_OWNER, COLUMN_SERVER_ID };

	String startTime, endTime;
	boolean isOwner;
	long rowId;
	GeoPoint location_;

	protected EventPin(GeoPoint point, String name, String startTime,
			String endTime, boolean isOwner, long row) {

		location_ = point;
		setProperties(startTime, endTime, isOwner, row);
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
	public static EventPin getItemFromRow(Cursor c) {
		int lat = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LOCATION_LAT));
		int lon = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LOCATION_LON));
		String name = c.getString(c.getColumnIndex(DBAdapter.COLUMN_NAME));
		String startTime = c.getString(c
				.getColumnIndex(DBAdapter.COLUMN_START_TIME));
		String endTime = c.getString(c
				.getColumnIndex(DBAdapter.COLUMN_END_TIME));
		int isOwnerInt = c.getInt(c.getColumnIndex(DBAdapter.COLUMN_IS_OWNER));
		long row = c.getLong(c.getColumnIndex(DBAdapter.COLUMN_ID));

		boolean isOwner = (isOwnerInt == 1);

		final GeoPoint location = new GeoPoint(lat, lon);
		EventPin eoi = new EventPin(location, name, startTime, endTime, isOwner, row);

		return eoi;
	}

	/** Used to set the properties for this event */
	private void setProperties(String start, String end, boolean owner, long row) {
		startTime = start;
		endTime = end;
		isOwner = owner;
		rowId = row;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	
	public GeoPoint getLocation() {
		return location_;
	}

	public boolean getIsOwner() {
		return isOwner;
	}

	public long getDBRowId() {
		return rowId;
	}
}
