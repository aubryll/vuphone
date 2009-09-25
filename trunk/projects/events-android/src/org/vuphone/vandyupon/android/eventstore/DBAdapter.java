/**
 * 
 */
package org.vuphone.vandyupon.android.eventstore;

import java.util.ArrayList;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.filters.PositionFilter;
import org.vuphone.vandyupon.android.filters.TagsFilter;
import org.vuphone.vandyupon.android.filters.TimeFilter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.maps.GeoPoint;

/**
 * Used to hide the database details from the other classes
 * 
 * @author Hamilton Turner
 * 
 */
public class DBAdapter {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "DBAdapter: ";

	/** Used for database updates */
	private static final int DB_VERSION = 1;

	/** The filename where the database is stored */
	private static final String DB_NAME = "events.db";

	/** The main table name */
	protected static final String TABLE_NAME = "events";

	/** The index column */
	public static final String COLUMN_ID = "_id";

	/** The other column names */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_START_TIME = "startTime";
	public static final String COLUMN_END_TIME = "endTime";
	public static final String COLUMN_UPDATED_TIME = "updatedTime";
	public static final String COLUMN_LOCATION_LAT = "latitude";
	public static final String COLUMN_LOCATION_LON = "longitude";
	public static final String COLUMN_IS_OWNER = "owner";
	public static final String COLUMN_SERVER_ID = "serverId";

	/** Handle to the database instance */
	private SQLiteDatabase database_;

	/** Used to help open and update the database */
	DBOpenHelper openHelper_;

	public DBAdapter(Context context) {
		openHelper_ = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
	}

	/** Used to close the database when done */
	public void close() {
		database_.close();
	}

	/**
	 * Returns all entries that match the passed filters. Pass in null for any
	 * filters that you are not interested in filtering by
	 * 
	 * @param p
	 *            filter for the event location. Can be null
	 * @param t
	 *            filter for the event time. Can be null
	 * @param tags
	 *            filter for the properties an event must have to show up. Can
	 *            be null
	 * @return a Cursor containing these columns: COLUMN_NAME,
	 *         COLUMN_START_TIME, COLUMN_END_TIME, COLUMN_LOCATION_LAT,
	 *         COLUMN_LOCATION_LON, COLUMN_IS_OWNER, COLUMN_SERVER_ID
	 */
	public Cursor getAllEntries(PositionFilter p, TimeFilter t, TagsFilter tags) {
		String[] returnValues = { COLUMN_NAME, COLUMN_START_TIME,
				COLUMN_END_TIME, COLUMN_LOCATION_LAT, COLUMN_LOCATION_LON,
				COLUMN_IS_OWNER, COLUMN_SERVER_ID };
		StringBuffer sb = new StringBuffer();
		ArrayList<String> sArgs = new ArrayList<String>();
		if (p != null) {
			sb.append(COLUMN_LOCATION_LAT);
			sb.append(" BETWEEN ? AND ? ");
			sb.append(" AND ");
			sb.append(COLUMN_LOCATION_LON);
			sb.append(" BETWEEN ? AND ? ");

			sArgs.add(Long.toString(p.getLowerLatitude()));
			sArgs.add(Long.toString(p.getUpperLatitude()));

			sArgs.add(Long.toString(p.getLowerLongitude()));
			sArgs.add(Long.toString(p.getUpperLongitude()));
		}

		// To match all events, the event start time must be <= the filter end
		// time, AND the event end time must be >= the filter start time
		if (t != null) {
			if (sb.length() != 0)
				sb.append(" AND ");

			sb.append(COLUMN_START_TIME);
			sb.append(" <= ? AND ");
			sb.append(COLUMN_END_TIME);
			sb.append(" >= ?");

			sArgs.add(Long.toString(t.getEndTime().getTimeInMillis()));
			sArgs.add(Long.toString(t.getStartTime().getTimeInMillis()));
		}

		if (tags != null) {
			// TODO - implement tags
		}

		Log.d(tag, pre + "Generated WHERE clause: " + sb.toString());
		String[] selectionArgs = new String[sArgs.size()];
		int i = 0;
		StringBuffer temp = new StringBuffer("Args: ");
		for (String s : sArgs) {
			selectionArgs[i++] = s;
			temp.append(s);
			temp.append(" ");
		}
		Log.d(tag, pre + "Generated " + temp.toString());

		Cursor c = database_.query(TABLE_NAME, returnValues, sb.toString(),
				selectionArgs, null, null, null);
		Log.d(tag, pre + "Found " + c.getCount() + " results");
		return c;
	}

	/**
	 * Used to fetch the largest updated time in our database. This value can be
	 * sent to the server, allowing the server to only return new entries, and
	 * not re-send all the data
	 * 
	 * @return the latest updated time, or 0 if the database is empty
	 */
	public long getLargestUpdatedTime() {
		Cursor result = database_.rawQuery("SELECT MAX(" + COLUMN_UPDATED_TIME
				+ ") FROM " + TABLE_NAME, null);
		if (result.moveToFirst())
			return result.getLong(0);
		else
			return 0;
	}

	/**
	 * Passed any event, this method will ensure that the latest version of that
	 * event is stored in the database
	 * 
	 * @param name
	 *            event name
	 * @param startTime
	 *            event start time
	 * @param endTime
	 *            event end time
	 * @param location
	 *            event location
	 * @param updateTime
	 *            the last time the event was updated
	 * @param owner
	 *            true if this phone is the owner of the event, false otherwise
	 * @param serverId
	 *            The unique id used by the server to identify this event
	 * @return true if the event was inserted, false if there was some problem
	 */
	public boolean insertOrUpdateEvent(String name,
			long startTime, long endTime,
			GeoPoint location, long updateTime, boolean owner,
			long serverId) {

		String[] resultColumns = { COLUMN_UPDATED_TIME };
		String[] selectionArgs = { Long.toString(serverId) };
		Cursor event = database_.query(TABLE_NAME, resultColumns,
				COLUMN_SERVER_ID + "=?", selectionArgs, null, null, null);

		// Build the row of content values
		ContentValues rowValues = new ContentValues(7);
		rowValues.put(COLUMN_NAME, name);
		rowValues.put(COLUMN_START_TIME, startTime);
		rowValues.put(COLUMN_END_TIME, endTime);
		rowValues.put(COLUMN_LOCATION_LAT, location.getLatitudeE6());
		rowValues.put(COLUMN_LOCATION_LON, location.getLongitudeE6());
		if (owner)
			rowValues.put(COLUMN_IS_OWNER, 1);
		else
			rowValues.put(COLUMN_IS_OWNER, 0);
		rowValues.put(COLUMN_UPDATED_TIME, updateTime);
		rowValues.put(COLUMN_SERVER_ID, serverId);

		if (event.getCount() == 0) {
			// insert event
			long id;
			try {
				id = database_.insertOrThrow(TABLE_NAME, null, rowValues);
			} catch (SQLException e) {
				e.printStackTrace();
				Log.w(tag, pre + "Unable to insert event into database");
				Log.w(tag, pre + "Error: " + e.getMessage());
				return false;
			}

			if (id == -1) {
				Log.w(tag, pre + "Unknown error occurred"
						+ " when inserting into database");
				return false;
			}

			return true;

		} else if (event.getCount() == 1) {
			// Check update time, perhaps update
			String[] whereArgs = { Long.toString(serverId),
					Long.toString(updateTime) };
			int rowsAffected = database_.update(TABLE_NAME, rowValues,
					COLUMN_SERVER_ID + "=? AND " + COLUMN_UPDATED_TIME + "<?",
					whereArgs);
			if (rowsAffected == 0)
				Log.w(tag, pre + "No rows affected! The server should"
						+ " not have returned this event to us!");
			else if (rowsAffected != 1)
				Log.w(tag, pre + rowsAffected
						+ " rows were affected on update! "
						+ "This should never happen!");
			return true;
		} else if (event.getCount() > 1) {
			// remove all rows, log warning, insert event
			Log.w(tag, pre + "More than one row with server id " + serverId);
			Log.w(tag, pre + "Removing all spare rows, and adding one back");
			String[] whereArgs = { Long.toString(serverId) };
			int count = database_.delete(TABLE_NAME, COLUMN_SERVER_ID + "=?",
					whereArgs);
			Log.w(tag, pre + "Removed " + count + " duplicate rows");

			long id;
			try {
				id = database_.insertOrThrow(TABLE_NAME, null, rowValues);
			} catch (SQLException e) {
				e.printStackTrace();
				Log.w(tag, pre + "Unable to insert event into database");
				Log.w(tag, pre + "Error: " + e.getMessage());
				return false;
			}

			if (id == -1) {
				Log.w(tag, pre + "Unknown error occurred"
						+ " when inserting into database");
				return false;
			}

			return true;
		}

		// We should never get here, means event.getCount was negative
		return false;
	}

	/** Used to open a readable database */
	public DBAdapter openReadable() throws SQLException {
		database_ = openHelper_.getReadableDatabase();
		return this;
	}

	/** Used to open a writable database */
	public DBAdapter openWritable() throws SQLException {
		database_ = openHelper_.getWritableDatabase();
		return this;
	}

}
