package edu.vanderbilt.vuphone.android.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

	/** Used for logging */
	private static final String pre = "DBOpenHelper: ";

	/** Used for CREATE TABLE */
	protected static final String RESTAURANT_TABLE_NAME = DBAdapter.RESTAURANT_TABLE_NAME;
	
	/** The index column */
	public static final String COLUMN_ID = DBAdapter.COLUMN_ID;

	/** The other column names */
	public static final String COLUMN_NAME = DBAdapter.COLUMN_NAME;
	public static final String COLUMN_LATITUDE = DBAdapter.COLUMN_LATITUDE;
	public static final String COLUMN_LONGITUDE = DBAdapter.COLUMN_LONGITUDE;
	public static final String COLUMN_DESCRIPTION = DBAdapter.COLUMN_DESCRIPTION;
	public static final String COLUMN_FAVORITE = DBAdapter.COLUMN_FAVORITE;

	protected static final String COLUMN_HOUR = DBAdapter.COLUMN_HOUR;

	public static final String COLUMN_STARTTIME = DBAdapter.COLUMN_STARTTIME;
	public static final String COLUMN_ENDTIME = DBAdapter.COLUMN_ENDTIME;
	public static final String COLUMN_STARTTIME2 = DBAdapter.COLUMN_STARTTIME2;
	public static final String COLUMN_ENDTIME2 = DBAdapter.COLUMN_ENDTIME2;

	/** Used to create database */
	private static final String RESTAURANT_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ RESTAURANT_TABLE_NAME
			+ " (                                       "
			+ COLUMN_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,       "
			+ COLUMN_NAME
			+ " TEXT NOT NULL,                         "
			+ COLUMN_LATITUDE
			+ " REAL NOT NULL,                "
			+ COLUMN_LONGITUDE
			+ " REAL NOT NULL,                  "
			+ COLUMN_DESCRIPTION
			+ " TEXT NOT NULL,              "
			+ COLUMN_FAVORITE
			+ " INTEGER NOT NULL,"
			+ COLUMN_HOUR
			+ " BLOB NOT NULL);              ";

	// create table restaurants (_id integer primary key autoincrement,
	// name text not null, latitude real not null, longitude real not null,
	// description text not null, favorite integer not null, hour blob not null);

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

		db.execSQL("DROP TABLE IF EXISTS " + RESTAURANT_TABLE_NAME);
		onCreate(db);
	}
}
