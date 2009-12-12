package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;

import android.database.Cursor;
import edu.vanderbilt.vuphone.android.dining.Main;
import edu.vanderbilt.vuphone.android.storage.DBAdapter;




/** 
 * @author austin
 *	This class abstracts database access and caches multiple like accesses.
 *	Accessed through the Restaurant class
 *
 * TODO: Restaurant efficient update (esp for user changing favorites)
 * 		 Restaurant delete
 * 		 caching only parts of restaurants
 * 		 reflect any new functionality in static methods in the Restaurant class
 * 
 */
public class DBWrapper {

	
	private static final int CLOSED = 0;
	private static final int READABLE = 1;
	private static final int WRITABLE = 2;
	private static DBAdapter adapter;
	private static int state;
	//private static boolean changed = false; // set to true every time the Database is changed
	private static ArrayList<Long> IDs;
	private static ArrayList<Restaurant> cache;
	private static boolean mainDataCached = false;
	private static boolean mapDataCached = false;
	private static ArrayList<Boolean> cached;
	//private static int cached = 0;
	// IDs and cache the same size, parallel arrays ftw
	

	@SuppressWarnings("unchecked")
	public static ArrayList<Long> getIDs() {
		if (!mainDataCached) {
			cacheMainData();
		}
		return (ArrayList<Long>)IDs.clone();
	}
	
	
	public static Restaurant get(long rowID) { 
		int i = cacheRestaurant(rowID);
		return cache.get(i);
	}
	
	public static String getName(long rowID) {
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).getName();
	}		
	public static int getLat(long rowID) {
		cacheMapData();
		return cache.get(IDs.indexOf(rowID)).getLat();
	}		
	public static int getLon(long rowID) {
		cacheMapData();
		return cache.get(IDs.indexOf(rowID)).getLon();
	}
	public static RestaurantHours getHours(long rowID) {
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).getHours();
	}
	public static String getType(long rowID) { // TODO implement this
		return "";
	}
	public static boolean favorite(long rowID) {
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).favorite();
	}
	public static boolean onTheCard(long rowID) { // TODO implement these
		return true;
	}
	public static boolean offCampus(long rowID) {
		return false;
	}
	
	
	public static boolean create(Restaurant r) {
		makeWritable();
		// TODO update this step as the underlying DBAdapter method is update
		long rID = adapter.createRestaurant(r.getName(), r.getLat(), r.getLon(), 
				r.getDescription(), r.favorite(), r.getHours());
		if (mainDataCached && rID != -1) {
			IDs.add(rID);
			cache.add(r);
		}
		return (rID != -1);
	}

	public static boolean delete(long rowID) {
		makeWritable();
		if (adapter.deleteRestaurant(rowID)) {
			int i = getIDs().indexOf(rowID);
			if (i>=0) {
				cache.remove(i);
				IDs.remove(i);
			}
			return true;
		} else return false;
	}
	
	private static void cacheMainData() {
		if (mainDataCached)
			return;
		resetCache();
		makeReadable();
		Cursor c = adapter.getCursor(new String[] {DBAdapter.COLUMN_ID, DBAdapter.COLUMN_NAME, 
			DBAdapter.COLUMN_FAVORITE, DBAdapter.COLUMN_HOUR});
		if (c.moveToFirst()) {
			
			do {
				Restaurant current = new Restaurant();
				IDs.add(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_ID)));
				current.setName(c.getString(c.getColumnIndex(DBAdapter.COLUMN_NAME)));
				current.setFavorite(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_FAVORITE)) == 1);
				current.setHours(DBAdapter.getRestaurantHoursFromXml(c.getString(c.getColumnIndex(DBAdapter.COLUMN_HOUR))));
				cache.add(current);
				cached.add(false);
			} while (c.moveToNext());
		}
		mainDataCached = true;
		close();
	}
	
	private static void cacheMapData() {
		if (mapDataCached)
			return;
		if (!mainDataCached)
			cacheMainData();
		makeReadable();							// TODO remove COLUMN_ID check once this is debugged
		Cursor c = adapter.getCursor(new String[] {DBAdapter.COLUMN_ID, DBAdapter.COLUMN_LATITUDE, DBAdapter.COLUMN_LONGITUDE});
		if (c.moveToFirst()) {
			int i = 0;
			do {
				// here too
				if (IDs.get(i) != c.getInt(c.getColumnIndex(DBAdapter.COLUMN_ID)))
					throw new RuntimeException("ticker does not match actual DB column");
				cache.get(i++).setLocation(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LATITUDE)), 
						c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LONGITUDE)));
			} while (c.moveToNext());
		}
		mapDataCached = true;
		close();
	}
	
	private static int cacheRestaurant(long rowID) {
		if (!mainDataCached)
			cacheMainData();
		int i = IDs.indexOf(rowID);
		if (cached.get(i))
			return i;
		makeReadable();							// TODO remove COLUMN_ID check once this is debugged
		String [] columnsToRead;
		if (mapDataCached)
			columnsToRead = new String [] {DBAdapter.COLUMN_DESCRIPTION};
		else columnsToRead = new String [] {DBAdapter.COLUMN_LATITUDE, DBAdapter.COLUMN_LONGITUDE, DBAdapter.COLUMN_DESCRIPTION};
		Cursor c = adapter.getCursor(columnsToRead, rowID);
		if (!c.moveToFirst()) 
			throw new RuntimeException("Cannot cache restaurant which doesnt exist");
		if (i < 0)
			throw new RuntimeException("error, descrepency between database and memory cache");
		
		Restaurant r = cache.get(i);
		if (!mapDataCached)
			r.setLocation(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LATITUDE)), 
					c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LONGITUDE)));
		
		// TODO add other fields as DB implementation is updated
		r.setDescription(c.getString(c.getColumnIndex(DBAdapter.COLUMN_DESCRIPTION)));
		close();
		cached.set(i, true);
		return i;
	}
	
//	private static void setRestaurantField(Restaurant r, String DBColumn, Cursor c) {
//		if (DBColumn.equals(DBAdapter.COLUMN_NAME))
//			r.setName((String)c.getString(c.getColumnIndexOrThrow(DBColumn));
//		else if (DBColumn.equals(DBAdapter.COLUMN_LATITUDE))
//			r.setLatitude((Integer)setTo);
//		else if (DBColumn.equals(DBAdapter.COLUMN_LONGITUDE))
//			r.setLongidute((Integer)setTo);
//		else if (DBColumn.equals(DBAdapter.COLUMN_DESCRIPTION))
//			r.setDescription((String)setTo);
//		else if (DBColumn.equals(DBAdapter.COLUMN_FAVORITE))
//			r.setFavorite((Boolean)setTo);
//	}
	
	private static void resetCache() {
		cache = new ArrayList<Restaurant>();
		IDs = new ArrayList<Long>();
		cached = new ArrayList<Boolean>();
		mainDataCached = false;
		mapDataCached = false;
	}
	
	private static void initialize() {
		if (adapter == null) {
			adapter = new DBAdapter(Main.applicationContext);
			state = CLOSED;
		}
	}
	private static void makeWritable() {
		initialize();
		switch (state) {
		case READABLE:
			adapter.close();
			adapter.openWritable();
			state = WRITABLE;
			return;
		case WRITABLE:
			return;
		case CLOSED:
			adapter.openWritable();
			state = WRITABLE;
			return;
		}
	}
	private static void makeReadable() {
		initialize();
		switch (state) {
		case READABLE:
			return;
		case WRITABLE:
			adapter.close();
			adapter.openReadable();
			state = READABLE;
			return;
		case CLOSED:
			adapter.openReadable();
			state = READABLE;
			return;
		}
	}
	// closes the underlying database adapter 
	// no read/writes are to be performed in the 
	// near future
	public static void close() {
		initialize();
		switch (state) {
		case READABLE:
			adapter.close();
			state = CLOSED;
			return;
		case WRITABLE:
			adapter.close();
			state = CLOSED;
			return;
		case CLOSED:
			return;
		}
	}
}
