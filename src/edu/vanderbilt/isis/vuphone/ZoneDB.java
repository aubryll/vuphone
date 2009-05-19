package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

public class ZoneDB {
	private static final String DATABASE_NAME = "ZoneDB";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "ZoneDB";

	private static final String ZONES = "zones";
	private static final String CREATE_ZONES_TABLE = "CREATE TABLE IF NOT EXISTS zones ("
			+ "id INTEGER NOT NULL, "
			+ "name TEXT(100), "
			+ "PRIMARY KEY (id));";
	// For copy-paste
	// CREATE TABLE IF NOT EXISTS zones (id INTEGER NOT NULL, name TEXT(100),
	// PRIMARY KEY (id));
	private static final String GEOPOINTS = "geopoints";
	private static final String CREATE_GEOPOINTS_TABLE = "CREATE TABLE IF NOT EXISTS geopoints ("
			+ "id INTEGER NOT NULL, "
			+ "zoneid INTEGER NOT NULL, "
			+ "point_order INTEGER NOT NULL, "
			+ "latitude INTEGER NOT NULL, "
			+ "longitude INTEGER NOT NULL, " + "PRIMARY KEY (id));";
	// For copy-paste
	// CREATE TABLE IF NOT EXISTS geopoints (id INTEGER NOT NULL, zoneid INTEGER
	// NOT NULL, point_order INTEGER NOT NULL, latitude INTEGER NOT NULL,
	// longitude INTEGER NOT NULL, PRIMARY KEY (id));

	private SQLiteDatabase zoneDB_;
	private DatabaseHelper openHelper_;
	private final Context context_;
	private static ZoneDB instance_ = null;
	private Projection proj_;

	public static ZoneDB getInstance() {
		if (instance_ == null)
			throw new RuntimeException(
					"ZoneDB: Someone did not construct the instance!");
		return instance_;
	}

	/**
	 * Helps us create, update, and open/close databases
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_ZONES_TABLE);
			db.execSQL(CREATE_GEOPOINTS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS zones");
			db.execSQL("DROP TABLE IF EXISTS geopoints");
			onCreate(db);
		}
	}

	public ZoneDB(Context context) {
		context_ = context;
		instance_ = this;
	}

	public ZoneDB open() throws SQLException {
		openHelper_ = new DatabaseHelper(context_);
		zoneDB_ = openHelper_.getWritableDatabase();
		return this;
	}

	public void close() {
		openHelper_.close();
	}

	public long createZone(String name, List<GeoPoint> points) {
		ContentValues zone = new ContentValues(1);
		zone.put("name", name);
		long zoneID = zoneDB_.insert(ZONES, null, zone);

		// Ensure that the start/end point is not included twice
		int length = points.size();
		if (points.get(0).equals(points.get(points.size() - 1)))
			length--;

		// Store all points
		for (int i = 0; i < length; i++) {
			// zoneID, pointOrder, last, lng
			ContentValues geopoints = new ContentValues(4);
			geopoints.put("zoneid", zoneID);
			geopoints.put("point_order", i);
			geopoints.put("latitude", points.get(i).getLatitudeE6());
			geopoints.put("longitude", points.get(i).getLongitudeE6());
			zoneDB_.insert(GEOPOINTS, null, geopoints);
		}

		return zoneID;
	}

	public boolean deleteZone(long rowID) {
		boolean removed = zoneDB_.delete(ZONES, "id=" + rowID, null) > 0;
		boolean removedFully = false;
		if (removed) {
			removedFully = zoneDB_.delete(GEOPOINTS, "zoneid=" + rowID, null) > 0;
		}
		return removedFully;
	}

	public List<Zone> getAllZones() {
		 
		 Cursor zonesCursor = zoneDB_.rawQuery("SELECT name, zones.id, point_order, latitude, longitude FROM zones, geopoints WHERE zones.id=geopoints.zoneid", null);
				
		 //Zone[] zones = new Zone[zonesCursor.getCount()];
		 List<Zone> zones = new ArrayList<Zone>();
		
		 int count = 0; 
		 long lastID = 0;
		 while (zonesCursor.moveToNext()) {
			 long id = zonesCursor.getLong(zonesCursor.getColumnIndex("id"));
			 if (lastID != id)
			 {
				 String name = zonesCursor.getString(zonesCursor.getColumnIndex("name"));
				 zones.add(count, new Zone(proj_));
				 zones.get(count).setName(name);
				 zones.get(count).setID(id);
				 if (count != 0)
					 zones.get(count - 1).finalizePath();
				 count++;
				 lastID = id;
				 
				 Log.v("VUPHONE", "Completed zone " + (count-2));
				 Log.v("VUPHONE", "Started zone " + (count-1));
			 }
			 int latE6 = zonesCursor.getInt(zonesCursor.getColumnIndex("latitude"));
			 int lngE6 = zonesCursor.getInt(zonesCursor.getColumnIndex("longitude"));
			 GeoPoint p = new GeoPoint(latE6, lngE6);
			 
			 Log.v("VUPHONE", "Added point " + p);
			 zones.get(count - 1).addPoint(p, false);
		 } 
		 
		 if (count != 0)
			 zones.get(count - 1).finalizePath();
		 
		 return zones;
	}
	
	public void useProjection(Projection p)
	{
		proj_ = p;
	}
}