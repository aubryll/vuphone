package edu.vanderbilt.vuphone.android.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.vanderbilt.vuphone.android.objects.Menu;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;

public class DBAdapter {

	/** Used for logging */
	private static final String pre = "DBAdapter: ";

	/** Used for database updates */
	private static final int DB_VERSION = 3;

	/** The filename where the database is stored */
	private static final String DB_NAME = "dining.db";

	/** The main table name */
	protected static final String RESTAURANT_TABLE = "restaurants";

	/** The index column */
	public static final String COLUMN_ID = "_id";

	/** The other column names */
	public static final String COLUMN_NAME 			= "name";
	public static final String COLUMN_HOUR_SUN 		= "hourS";
	public static final String COLUMN_HOUR_MON 		= "hourM";
	public static final String COLUMN_HOUR_TUE 		= "hourT";
	public static final String COLUMN_HOUR_WED 		= "hourW";
	public static final String COLUMN_HOUR_THU 		= "hourTh";
	public static final String COLUMN_HOUR_FRI 		= "hourF";
	public static final String COLUMN_HOUR_SAT 		= "hourSa";
	public static final String COLUMN_MENU 			= "menu";
	public static final String COLUMN_DESCRIPTION 	= "description";
	public static final String COLUMN_TYPE 			= "type";
	public static final String COLUMN_ICON 			= "icon";
	public static final String COLUMN_LATITUDE 		= "latitude";
	public static final String COLUMN_LONGITUDE 	= "longitude";
	public static final String COLUMN_FAVORITE 		= "favorite";
	public static final String COLUMN_ON_THE_CARD 	= "onTheCard";
	public static final String COLUMN_OFF_CAMPUS 	= "offCampus";
	public static final String COLUMN_PHONE_NUMBER 	= "phoneNumber";
	public static final String COLUMN_URL 			= "url";
	
	/** Handle to the database instance */
	private SQLiteDatabase database_;

	/** Used to help open and update the database */
	DBOpenHelper openHelper_;

	private static class DBOpenHelper extends SQLiteOpenHelper {

		/** Used for logging */
		private static final String pre = "DBOpenHelper: ";


		/** Used to create database */
		private static final String RESTAURANT_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ RESTAURANT_TABLE		+ " ("
				+ COLUMN_ID				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ COLUMN_NAME 			+ " TEXT NOT NULL,"
				+ COLUMN_HOUR_SUN 		+ " INTEGER NOT NULL,"
				+ COLUMN_HOUR_MON 		+ " INTEGER NOT NULL,"
				+ COLUMN_HOUR_TUE 		+ " INTEGER NOT NULL,"
				+ COLUMN_HOUR_WED 		+ " INTEGER NOT NULL,"
				+ COLUMN_HOUR_THU 		+ " INTEGER NOT NULL,"
				+ COLUMN_HOUR_FRI 		+ " INTEGER NOT NULL,"
				+ COLUMN_HOUR_SAT 		+ " INTEGER NOT NULL,"
				+ COLUMN_FAVORITE 		+ " INTEGER NOT NULL,"
				+ COLUMN_ON_THE_CARD 	+ " INTEGER NOT NULL,"
				+ COLUMN_OFF_CAMPUS 	+ " INTEGER NOT NULL,"
				+ COLUMN_TYPE 			+ " TEXT NOT NULL,"
				+ COLUMN_LATITUDE 		+ " INTEGER NOT NULL,"
				+ COLUMN_LONGITUDE 		+ " INTEGER NOT NULL,"
				+ COLUMN_ICON 			+ " INTEGER NOT NULL,"
				+ COLUMN_DESCRIPTION 	+ " TEXT,"
				+ COLUMN_PHONE_NUMBER 	+ " TEXT,"
				+ COLUMN_URL 			+ " TEXT,"
				+ COLUMN_MENU 			+ " BLOB);";

		// create table restaurants (_id integer primary key autoincrement,
		// name text not null, latitude real not null, longitude real not null,
		// description text not null, favorite integer not null, hour blob not null);

		/**
		 * @see android.database.sqlite.SQLiteOpenHelper#SQLiteOpenHelper(Context,
		 *      String, CursorFactory, int)
		 */
		public DBOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		/**
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("i", pre + "Creating a new DB");
			db.execSQL(RESTAURANT_CREATE);
		}

		/**
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
		 *      int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log the version upgrade.
			Log.w("Warning", pre + "Upgrading from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS " + RESTAURANT_TABLE);
			onCreate(db);
		}
	}

	
	
	public DBAdapter(Context context) {
		openHelper_ = new DBOpenHelper(context);
	}

	/** Used to close the database when done */
	public void close() {
		openHelper_.close();
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
	public long createRestaurant(Restaurant r) {

		ContentValues initialValues = new ContentValues(6);
		initialValues.put(COLUMN_NAME 			, r.getName());
		initialValues.put(COLUMN_HOUR_SUN 		, r.getHours().flatten(Calendar.SUNDAY));
		initialValues.put(COLUMN_HOUR_MON 		, r.getHours().flatten(Calendar.MONDAY));
		initialValues.put(COLUMN_HOUR_TUE 		, r.getHours().flatten(Calendar.TUESDAY)); 
		initialValues.put(COLUMN_HOUR_WED 		, r.getHours().flatten(Calendar.WEDNESDAY)); 
		initialValues.put(COLUMN_HOUR_THU 		, r.getHours().flatten(Calendar.THURSDAY)); 
		initialValues.put(COLUMN_HOUR_FRI 		, r.getHours().flatten(Calendar.FRIDAY)); 
		initialValues.put(COLUMN_HOUR_SAT 		, r.getHours().flatten(Calendar.SATURDAY));
		initialValues.put(COLUMN_DESCRIPTION 	, r.getDescription());
		initialValues.put(COLUMN_TYPE 			, r.getType());
		initialValues.put(COLUMN_ICON 			, r.getIcon());
		initialValues.put(COLUMN_LATITUDE 		, r.getLat());
		initialValues.put(COLUMN_LONGITUDE 		, r.getLon());
		initialValues.put(COLUMN_FAVORITE 		, (r.favorite()?1:0));
		initialValues.put(COLUMN_ON_THE_CARD 	, (r.onTheCard()?1:0));
		initialValues.put(COLUMN_OFF_CAMPUS 	, (r.offCampus()?1:0));
		initialValues.put(COLUMN_PHONE_NUMBER 	, r.getPhoneNumber());
		initialValues.put(COLUMN_URL 			, r.getUrl());
				
		// TODO remove comments when menu functional
/*		if (r.getMenu() != null) {
			XStream xstream = new XStream(new DomDriver());
			String menuf = xstream.toXML(r.getMenu());
			initialValues.put(COLUMN_MENU, menuf);
		}*/

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
	 * 
	 * TODO add the other fields that might be updated as they are implemented
	 */
	public boolean updateRestaurant(long rowId, Restaurant updated) {

		ContentValues args = new ContentValues(5);
		args.put(COLUMN_NAME 			, updated.getName());                                     
		args.put(COLUMN_HOUR_SUN 		, updated.getHours().flatten(Calendar.SUNDAY));           
		args.put(COLUMN_HOUR_MON 		, updated.getHours().flatten(Calendar.MONDAY));           
		args.put(COLUMN_HOUR_TUE 		, updated.getHours().flatten(Calendar.TUESDAY));          
		args.put(COLUMN_HOUR_WED 		, updated.getHours().flatten(Calendar.WEDNESDAY));        
		args.put(COLUMN_HOUR_THU 		, updated.getHours().flatten(Calendar.THURSDAY));         
		args.put(COLUMN_HOUR_FRI 		, updated.getHours().flatten(Calendar.FRIDAY));           
		args.put(COLUMN_HOUR_SAT 		, updated.getHours().flatten(Calendar.SATURDAY));         
		args.put(COLUMN_DESCRIPTION 	, updated.getDescription());                              
		args.put(COLUMN_TYPE 			, updated.getType());                                     
		args.put(COLUMN_ICON 			, updated.getIcon());                                     
		args.put(COLUMN_LATITUDE 		, updated.getLat());                                      
		args.put(COLUMN_LONGITUDE 		, updated.getLon());                                      
		args.put(COLUMN_FAVORITE 		, (updated.favorite()?1:0));                              
		args.put(COLUMN_ON_THE_CARD 	, (updated.onTheCard()?1:0));                             
		args.put(COLUMN_OFF_CAMPUS 		, (updated.offCampus()?1:0));                             
		args.put(COLUMN_PHONE_NUMBER 	, updated.getPhoneNumber());                              
		args.put(COLUMN_URL 			, updated.getUrl());                                      
	
	
		
		return database_.update(RESTAURANT_TABLE, args,
				COLUMN_ID + "=" + rowId, null) > 0;
	}
	
	// these methods allow individual columns to be updated, without 
	// having to pull the rest of the Restaurant from storage
	public boolean updateColumn(long rowId, String column, int value) {
		ContentValues args = new ContentValues(1);
		args.put(column, value);
		return database_.update(RESTAURANT_TABLE, args, 
				COLUMN_ID + "=" + rowId, null) > 0;
	}
	public boolean updateColumn(long rowId, String column, long value) {
		ContentValues args = new ContentValues(1);
		args.put(column, value);
		return database_.update(RESTAURANT_TABLE, args, 
				COLUMN_ID + "=" + rowId, null) > 0;
	}
	public boolean updateColumn(long rowId, String column, boolean value) {
		ContentValues args = new ContentValues(1);
		args.put(column, value);
		return database_.update(RESTAURANT_TABLE, args, 
				COLUMN_ID + "=" + rowId, null) > 0;
	}
	public boolean updateColumn(long rowId, String column, String value) {
		ContentValues args = new ContentValues(1);
		args.put(column, value);
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
	public static Menu getMenuFromXml(String xmlFromDatabase) {
		XStream xs = new XStream(new DomDriver());
		return (Menu) xs.fromXML(xmlFromDatabase);
	}
	
//
//	/**
//	 * not needed by DBWrapper
//	 * Get a list of all Restaurant IDs in the database
//	 * 
//	 * @return A List of Longs, where each Long is the ID of one of the
//	 *         restaurants in the database. Use the fetchRestaurant() call to
//	 *         get the Restaurant object
//	 * 
//	 * @see DBAdapter.fetchRestaurant(long rowId)
//	 */
//	public List<Long> fetchAllRestaurantIDs() {
//		Cursor c = database_.query(RESTAURANT_TABLE,
//				new String[] { COLUMN_ID }, null, null, null, null, null);
//
//		ArrayList<Long> restaurantIds = new ArrayList<Long>();
//
//		// If there are no restaurants, return empty list
//		if (c.moveToFirst() == false)
//			return restaurantIds;
//
//		do {
//			restaurantIds.add(c.getLong(c.getColumnIndex(COLUMN_ID)));
//		} while (c.moveToNext());
//		
//		c.close();
//
//		return restaurantIds;
//	}
//
//	/**
//	 * not needed by DBWrapper
//	 * Return a Restaurant object for the given restaurant id.
//	 * 
//	 * @param rowId
//	 *            id of restaurant to retrieve
//	 * @return Restaurant that that ID represents in the database
//	 * 
//	 * @throws SQLException
//	 *             if restaurant could not be found/retrieved
//	 * 
//	 * @TODO - Create a RestaurantNotFound exception, and throw that instead
//	 */
//	public Restaurant fetchRestaurant(long rowId) throws SQLException {
//		Cursor c = database_.query(true, RESTAURANT_TABLE, new String[] {
//				COLUMN_ID, COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
//				COLUMN_DESCRIPTION, COLUMN_FAVORITE, COLUMN_HOUR }, COLUMN_ID + "=" + rowId,
//				null, null, null, null, null);
//		
//		if (c.moveToFirst() == false) 
//			throw new SQLException("Restaurant was not found");
//			
//		String xml = c.getString(c.getColumnIndex(COLUMN_HOUR));
//		RestaurantHours hours = getRestaurantHoursFromXml(xml);
//
//		String name = c.getString(c.getColumnIndex(COLUMN_NAME));
//		int latitude = c.getInt(c.getColumnIndex(COLUMN_LATITUDE));
//		int longitude = c.getInt(c.getColumnIndex(COLUMN_LONGITUDE));
//		boolean fav = (c.getInt(c.getColumnIndex(COLUMN_FAVORITE)) == 1);
//		
//		c.close();
//		
//		// need to implement the rest of the elements of this constructor
//		return new Restaurant(name, hours, fav, latitude, longitude, null, null, null, 0x0, true, false, null, null);
//	}
}