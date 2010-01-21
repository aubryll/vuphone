/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import android.database.Cursor;

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;

/**
 * Used to represent one event.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventPin implements Comparable<EventPin> {
	/** Used for logging */
	// private static final String tag = Constants.tag;
	// private static final String pre = "EventOverlayItem: ";

	// String[] returnValues = { COLUMN_NAME, COLUMN_START_TIME,
	// COLUMN_END_TIME, COLUMN_LOCATION_LAT, COLUMN_LOCATION_LON,
	// COLUMN_IS_OWNER, COLUMN_SERVER_ID };

	private final String startTime, endTime;
	private final boolean isOwner;
	private final long rowId;
	private final GeoPoint location_;

	protected EventPin(GeoPoint point, String name, String startTime,
			String endTime, boolean isOwner, long row) {

		location_ = point;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isOwner = isOwner;
		rowId = row;
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
		EventPin eoi = new EventPin(location, name, startTime, endTime,
				isOwner, row);

		return eoi;
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

	/**
	 * Cheap equals method that only checks if the locations are the exact same.
	 * This allows the HashSet used in EventOverlay to only contain a single
	 * point per location
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof EventPin) {
			if (((EventPin) o).getLocation().equals(location_))
				return true;
			return false;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int hash = 16 * location_.hashCode() + location_.hashCode();
		return hash;
	}

	public int compareTo(EventPin other) {
		// Return higher items first
		if (location_.getLatitudeE6() > other.getLocation().getLatitudeE6())
			return -1;
		else if (location_.getLatitudeE6() < other.getLocation()
				.getLatitudeE6())
			return 1;
		return 0;
	}

}
