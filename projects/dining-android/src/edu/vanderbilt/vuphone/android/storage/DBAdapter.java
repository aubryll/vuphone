package edu.vanderbilt.vuphone.android.storage;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.vanderbilt.vuphone.android.objects.Menu;
import edu.vanderbilt.vuphone.android.objects.Restaurant;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;

public class DBAdapter {

	/** Used for logging */
	private static final String pre = "DBAdapter: ";

	/** Used for database updates */
	private static final int DB_VERSION = 1;

	/** The filename where the database is stored */
	private static final String DB_NAME = "dining.db";

	/** The main table name */
	protected static final String RESTAURANT_TABLE = "restaurants";

	/** The index column */
	public static final String COLUMN_ID = "_id";

	/** The other column names */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_FAVORITE = "favorite";

	/**
	 * Variable to indicate the Hour column. As the rest of the application
	 * should never use the hour column, this variable is hidden from anyone.
	 * The rest of the application uses the RestaurantHours object to interface
	 * with this column. The data stored in this column is simply an XML
	 * representation of the data in a single RestaurantHours object.
	 * 
	 * I promise I'll be REEEALLYY CAREFUL! 
	 * 	(also added a static helper method so I dont blow up the universe)
	 */
	/*protected*/ public static final String COLUMN_HOUR = "hour";

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
	 *            true indicates this restaurant is a favorite
	 * @param hours
	 *            the RestaurantHours object that represents the hours this
	 *            Restaurant is open
	 * 
	 * @return rowId or -1 if failed
	 */
	public long createRestaurant(String name, double latitude,
			double longitude, String description, boolean favorite,
			RestaurantHours hours) {

		ContentValues initialValues = new ContentValues(6);
		initialValues.put(COLUMN_NAME, name);
		initialValues.put(COLUMN_LATITUDE, latitude);
		initialValues.put(COLUMN_LONGITUDE, longitude);
		initialValues.put(COLUMN_DESCRIPTION, description);
		initialValues.put(COLUMN_FAVORITE, favorite);

		XStream xstream = new XStream(new DomDriver());
		String hoursStr = xstream.toXML(hours);
		initialValues.put(COLUMN_HOUR, hoursStr);

		return database_.insert(RESTAURANT_TABLE, null, initialValues);
	}

	/**
	 * Delete the restaurant with the given rowId
	 * 
	 * @param rowId
	 *            id of restaurant to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteRestaurant(long rowId) {

		return database_
				.delete(RESTAURANT_TABLE, COLUMN_ID + "=" + rowId, null) > 0;
	}

	/**
	 * not needed by DBWrapper
	 * Get a list of all Restaurant IDs in the database
	 * 
	 * @return A List of Longs, where each Long is the ID of one of the
	 *         restaurants in the database. Use the fetchRestaurant() call to
	 *         get the Restaurant object
	 * 
	 * @see DBAdapter.fetchRestaurant(long rowId)
	 */
	public List<Long> fetchAllRestaurantIDs() {
		Cursor c = database_.query(RESTAURANT_TABLE,
				new String[] { COLUMN_ID }, null, null, null, null, null);

		ArrayList<Long> restaurantIds = new ArrayList<Long>();

		// If there are no restaurants, return empty list
		if (c.moveToFirst() == false)
			return restaurantIds;

		do {
			restaurantIds.add(c.getLong(c.getColumnIndex(COLUMN_ID)));
		} while (c.moveToNext());
		
		c.close();

		return restaurantIds;
	}

	/**
	 * not needed by DBWrapper
	 * Return a Restaurant object for the given restaurant id.
	 * 
	 * @param rowId
	 *            id of restaurant to retrieve
	 * @return Restaurant that that ID represents in the database
	 * 
	 * @throws SQLException
	 *             if restaurant could not be found/retrieved
	 * 
	 * @TODO - Create a RestaurantNotFound exception, and throw that instead
	 */
	public Restaurant fetchRestaurant(long rowId) throws SQLException {
		Cursor c = database_.query(true, RESTAURANT_TABLE, new String[] {
				COLUMN_ID, COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
				COLUMN_DESCRIPTION, COLUMN_FAVORITE, COLUMN_HOUR }, COLUMN_ID + "=" + rowId,
				null, null, null, null, null);
		
		if (c.moveToFirst() == false) 
			throw new SQLException("Restaurant was not found");
			
		String xml = c.getString(c.getColumnIndex(COLUMN_HOUR));
		RestaurantHours hours = getRestaurantHoursFromXml(xml);

		String name = c.getString(c.getColumnIndex(COLUMN_NAME));
		int latitude = c.getInt(c.getColumnIndex(COLUMN_LATITUDE));
		int longitude = c.getInt(c.getColumnIndex(COLUMN_LONGITUDE));
		boolean fav = (c.getInt(c.getColumnIndex(COLUMN_FAVORITE)) == 1);
		
		c.close();
		
		// need to implement the rest of the elements of this constructor
		return new Restaurant(name, hours, fav, latitude, longitude, null, null, null, 0x0, true, false, null, null);
	}
	
    /**
     * Return a Cursor over the list of all restaurants in the database
     * 
     * @param columns
     * 			An array of column names required to be traversable by the 
     * 			returned Cursor
     * @return This cursor allows you to reference these columns COLUMN_ID,
     *         COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
     *         COLUMN_DESCRIPTION, COLUMN_FAVORITE. Note that this does not
     *         allow you to reference the restaurant hours information
     */
    public Cursor getCursor(String [] columns) {
    	return database_.query(RESTAURANT_TABLE, columns, null, null, null, null, null);
    }
    
    
    /**
     * @param columns
     * 			An array of column names required to be traversable by the 
     * 			returned Cursor
     * @param rowId
     * 			the rowID of the restaurant to be traversed
     * @return A cursor to traverse over Restaurant with rowID.
     */
    public Cursor getCursor(String [] columns, long rowId) {
    	return database_.query(true, RESTAURANT_TABLE, columns, COLUMN_ID + "=" + rowId, null, null, null, null, null);
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

		return database_.update(RESTAURANT_TABLE, args,
				COLUMN_ID + "=" + rowId, null) > 0;
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

	/**
	 * Generates RestaurantHours from database hours xml data
	 * @param xmlFromDatabase
	 * 		contents of a database COLUMN_HOUR
	 * @return
	 *		equivalent RestaurantHours object
	 */
	public static RestaurantHours getRestaurantHoursFromXml(String xmlFromDatabase) {
		XStream xs = new XStream(new DomDriver());
		return (RestaurantHours) xs.fromXML(xmlFromDatabase);
	}
}