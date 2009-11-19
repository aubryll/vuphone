package edu.vanderbilt.vuphone.android.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.vanderbilt.vuphone.android.objects.RestaurantHours;

public class DBAdapter {

	/** Used for logging */
	private static final String pre = "DBAdapter: ";

	/** Used for database updates */
	private static final int DB_VERSION = 1;

	/** The filename where the database is stored */
	private static final String DB_NAME = "dining.db";

	/** The main table name */
	protected static final String RESTAURANT_TABLE_NAME = "restaurants";
	protected static final String HOUR_TABLE_NAME = "hours";

	/** The index column */
	public static final String COLUMN_ID = "_id";

	/** The other column names */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_FAVORITE = "favorite";

	protected static final String COLUMN_HOUR = "hour";

	public static final String COLUMN_STARTTIME = "startTime";
	public static final String COLUMN_ENDTIME = "endTime";
	public static final String COLUMN_STARTTIME2 = "startTime2";
	public static final String COLUMN_ENDTIME2 = "endTime2";

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
	 * Create a new restaurant using the name, latitude, longitude, description,
	 * favorite, mondayID, tuesdayID, wednesdayId, thursdayId, fridayId,
	 * saturdayId, sundayId provided. If the restaurant is successfully created
	 * return the new rowId for that restaurant, otherwise return a -1 to
	 * indicate failure.
	 * 
	 * @param name
	 *            the name of the restaurant
	 * @param latitude
	 *            the latitude of the restaurant location
	 * @param longitude
	 *            the longitude of the restaurant location
	 * @param description
	 *            the restaurant description
	 * @param favorite
	 *            1 indicates a favorite, and 0 if otherwise
	 * 
	 * @return rowId or -1 if failed
	 */
	public long createRestaurant(String name, double latitude,
			double longitude, String description, boolean favorite,
			RestaurantHours hours) {

		ContentValues initialValues = new ContentValues(5);
		initialValues.put(COLUMN_NAME, name);
		initialValues.put(COLUMN_LATITUDE, latitude);
		initialValues.put(COLUMN_LONGITUDE, longitude);
		initialValues.put(COLUMN_DESCRIPTION, description);
		initialValues.put(COLUMN_FAVORITE, favorite);

		XStream xstream = new XStream(new DomDriver());
		String hoursStr = xstream.toXML(hours);
		initialValues.put(COLUMN_HOUR, hoursStr);

		return database_.insert(RESTAURANT_TABLE_NAME, null, initialValues);

	}

	/**
	 * Delete the restaurant with the given rowId
	 * 
	 * @param rowId
	 *            id of restaurant to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteRestaurant(long rowId) {

		return database_.delete(RESTAURANT_TABLE_NAME, COLUMN_ID + "=" + rowId,
				null) > 0;
	}

	/**
	 * Return a Cursor over the list of all restaurants in the database
	 * 
	 * @return This cursor allows you to reference these columns COLUMN_ID,
	 *         COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
	 *         COLUMN_DESCRIPTION, COLUMN_FAVORITE
	 */

	public Cursor fetchAllRestaurants() {

		return database_.query(RESTAURANT_TABLE_NAME, new String[] { COLUMN_ID,
				COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
				COLUMN_DESCRIPTION, COLUMN_FAVORITE }, null, null, null, null,
				null);
	}

	/**
	 * Return a Cursor positioned at the restaurant that matches the given rowId
	 * 
	 * @param rowId
	 *            id of restaurant to retrieve
	 * @return Cursor positioned to matching restaurant which lists COLUMN_ID,
	 *         COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
	 *         COLUMN_DESCRIPTION, COLUMN_FAVORITE, if found
	 * @throws SQLException
	 *             if restaurant could not be found/retrieved
	 */

	public Cursor fetchRestaurant(long rowId) throws SQLException {
		Cursor mCursor =

		database_.query(true, RESTAURANT_TABLE_NAME, new String[] { COLUMN_ID,
				COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
				COLUMN_DESCRIPTION, COLUMN_FAVORITE }, COLUMN_ID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor.moveToFirst()) {
			return mCursor;
		} else {
			SQLException sqlException = new SQLException();
			throw sqlException;
		}
	}

	/**
	 * Update the restaurant using the details provided. The restaurant to be
	 * updated is specified using the rowId, and it is altered to use the name,
	 * latitude, longitude, description, favorite values passed in
	 * 
	 * @param rowId
	 *            id of note to update
	 * @param name
	 *            value to set restaurant name to
	 * @param latitude
	 *            value to set restaurant latitude to
	 * @param latitude
	 *            value to set restaurant longitude to
	 * @param description
	 *            value to set restaurant description to
	 * @param favorite
	 *            value to set restaurant favorite to
	 * @return true if the restaurant was successfully updated, false otherwise
	 */
	public boolean updateRestaurant(long rowId, String name, long latitude,
			long longitude, String description, int favorite) {

		ContentValues args = new ContentValues(5);
		args.put(COLUMN_NAME, name);
		args.put(COLUMN_LATITUDE, latitude);
		args.put(COLUMN_LONGITUDE, longitude);
		args.put(COLUMN_DESCRIPTION, description);
		args.put(COLUMN_FAVORITE, favorite);

		return database_.update(RESTAURANT_TABLE_NAME, args, COLUMN_ID + "="
				+ rowId, null) > 0;
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

	public boolean isRestaurantOpen(long rowId) {
		Cursor c = database_.query(RESTAURANT_TABLE_NAME,
				new String[] { COLUMN_HOUR }, COLUMN_ID + "=" + rowId, null,
				null, null, null);

		// check to make sure we could move to first aka this row existed
		if (c.moveToFirst()) {
			String xml = c.getString(c.getColumnIndex(COLUMN_HOUR));
			XStream xs = new XStream(new DomDriver());
			RestaurantHours hours = (RestaurantHours) xs.fromXML(xml);
			// Move the isOpen call to the hours class
			// be sure to leave it accessible from the restaurant class as well
			return hours.isOpen();
		} else {
			String errorMessage = "Restaurant does not exist at that row.";
			IllegalArgumentException error = new IllegalArgumentException(errorMessage);
			throw error;
		}
	}
}
