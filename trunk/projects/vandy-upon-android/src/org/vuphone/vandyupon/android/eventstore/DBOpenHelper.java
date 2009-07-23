/**
 * 
 */
package org.vuphone.vandyupon.android.eventstore;

import org.vuphone.vandyupon.android.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * Used to help open the database, or create it if it does not already exist.
 * 
 * @author Hamilton Turner
 * 
 */
public class DBOpenHelper extends SQLiteOpenHelper {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "DBOpenHelper: ";

	/** Used for CREATE TABLE */
	protected static final String TABLE_NAME = DBAdapter.TABLE_NAME;

	/** The index column */
	public static final String COLUMN_ID = DBAdapter.COLUMN_ID;

	/** The other column names */
	public static final String COLUMN_NAME = DBAdapter.COLUMN_NAME;
	public static final String COLUMN_START_TIME = DBAdapter.COLUMN_START_TIME;
	public static final String COLUMN_END_TIME = DBAdapter.COLUMN_END_TIME;
	public static final String COLUMN_UPDATED_TIME = DBAdapter.COLUMN_UPDATED_TIME;
	public static final String COLUMN_LOCATION_LAT = DBAdapter.COLUMN_LOCATION_LAT;
	public static final String COLUMN_LOCATION_LON = DBAdapter.COLUMN_LOCATION_LON;
	public static final String COLUMN_IS_OWNER = DBAdapter.COLUMN_IS_OWNER;
	public static final String COLUMN_SERVER_ID = DBAdapter.COLUMN_SERVER_ID;

	/** Used to create database */
	private static final String DB_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " (                                       "
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,       "
			+ COLUMN_NAME + " TEXT NOT NULL,                         "
			+ COLUMN_START_TIME + " INTEGER NOT NULL,                "
			+ COLUMN_END_TIME + " INTEGER NOT NULL,                  "
			+ COLUMN_UPDATED_TIME + " INTEGER NOT NULL,              "
			+ COLUMN_LOCATION_LAT + " INTEGER NOT NULL,              "
			+ COLUMN_LOCATION_LON + " INTEGER NOT NULL,              "
			+ COLUMN_IS_OWNER + " INTEGER NOT NULL DEFAULT 0,        "
			+ COLUMN_SERVER_ID + " INTEGER NOT NULL);                ";

	// CREATE TABLE events (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT
	// NOT NULL, startTime INTEGER NOT NULL, endTime INTEGER NOT NULL,
	// updatedTime INTEGER NOT NULL, latitude INTEGER NOT NULL, longitude
	// INTEGER NOT NULL, owner INTEGER NOT NULL DEFAULT 0, serverId INTEGER NOT
	// NULL)

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#SQLiteOpenHelper(Context,
	 *      String, CursorFactory, int)
	 */
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(tag, pre + "Creating a new DB");
		db.execSQL(DB_CREATE);
	}

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log the version upgrade.
		Log.w(tag, pre + "Upgrading from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
