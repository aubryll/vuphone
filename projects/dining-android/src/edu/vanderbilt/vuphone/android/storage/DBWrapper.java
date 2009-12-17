package edu.vanderbilt.vuphone.android.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

import android.database.Cursor;
import edu.vanderbilt.vuphone.android.dining.Main;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;




/** 
 * @author austin
 *	This class abstracts database access and caches multiple like accesses.
 *	Accessed through the Restaurant class
 *
 * TODO: Restaurant efficient update (esp for user changing favorites)
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
	private static boolean idsCached = false;
	private static boolean mainDataCached = false;
	private static boolean mapDataCached = false;
	private static ArrayList<Boolean> cached;
	//private static int cached = 0;
	// IDs and cache the same size, parallel arrays ftw
	
	private static Stack<UpdateItem> updated;
	

	@SuppressWarnings("unchecked")
	public static ArrayList<Long> getIDs() {
		cacheIDs();
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

	public static String getType(long rowID) { 
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).getType();
	}

	public static int getIcon(long rowID) {
		cacheMapData();
		return cache.get(IDs.indexOf(rowID)).getIcon();
	}

	public static boolean favorite(long rowID) {
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).favorite();
	}

	public static boolean onTheCard(long rowID) { 
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).onTheCard();
	}

	public static boolean offCampus(long rowID) {
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).offCampus();
	}
	
	
	public static boolean create(Restaurant r) {
		makeWritable();
		long rID = adapter.createRestaurant(r);
		if (idsCached && rID != -1) {
			IDs.add(rID);
		}
		if (mainDataCached && rID != -1) {
			cache.add(r);
		}
		return (rID != -1);
	}
	
	// updates the database and cache immediately with new values
	public static boolean update(long rowID, Restaurant updated) {
		cacheIDs();
		makeWritable();
		int i = IDs.indexOf(rowID);
		if (i<0)
			return false;
		boolean success =  adapter.updateRestaurant(rowID, updated);
		if (mainDataCached && success)
			cache.set(i, updated);  
		close();
		return success;
	}
	
	// updates only the local cache with new favorite, call commit() to write changes to database
	// or revert() to revert to old settings
	public static boolean setFavorite(long rowID, boolean favorite) {
		if (!mainDataCached)
			cacheMainData();
		int i = IDs.indexOf(rowID);
		if (i<0 || cache.get(i).favorite()==favorite)
			return false;
		initializeUpdateStack();
		updated.push(new UpdateItem(i, UpdateItem.FAVORITE, !favorite));
		cache.get(i).setFavorite(favorite);
		return true;
	}
	
	public static boolean commit() {
		if (updated.empty())
			return false;
		else
			makeWritable();
		while (!updated.empty()) {
			if (!updated.pop().commit())
				throw new RuntimeException("Unable to commit all changes, undefined database behavior");
		}
		close();
		return true;
	}

	public static void revert() {
		while (!updated.empty())
			updated.pop().revert();
	}
	
	public static boolean delete(long rowID) {
		makeWritable();
		if (adapter.deleteRestaurant(rowID)) {
			int i = getIDs().indexOf(rowID);
			if (i>=0) {
				if (mainDataCached)
					cache.remove(i);
				IDs.remove(i);
			}
			return true;
		} else return false;
	}
	
	public static void cacheIDs() {
		if (idsCached)
			return;
		IDs = new ArrayList<Long>();
		makeReadable();
		Cursor c = adapter.getCursor(new String[] {DBAdapter.COLUMN_ID});
		if (c.moveToFirst()) {
			do {
				IDs.add(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_ID)));
			} while (c.moveToNext());
		}
		idsCached = true;
		c.close();
	}
	
	public static void cacheMainData() {
		if (mainDataCached)
			return;
		resetRestaurantCache(); // ensures IDs are cached
		makeReadable();
		Cursor c = adapter.getCursor(new String[] {
			DBAdapter.COLUMN_NAME,			
			DBAdapter.COLUMN_HOUR_SUN, 	
			DBAdapter.COLUMN_HOUR_MON, 	
			DBAdapter.COLUMN_HOUR_TUE, 	
			DBAdapter.COLUMN_HOUR_WED, 	
			DBAdapter.COLUMN_HOUR_THU, 	
			DBAdapter.COLUMN_HOUR_FRI, 	
			DBAdapter.COLUMN_HOUR_SAT, 	
			DBAdapter.COLUMN_FAVORITE, 	
			DBAdapter.COLUMN_ON_THE_CARD,
			DBAdapter.COLUMN_OFF_CAMPUS, 
			DBAdapter.COLUMN_TYPE});
		if (c.moveToFirst()) {
			do {
				Restaurant current = new Restaurant();
				current.setName(c.getString(c.getColumnIndex(DBAdapter.COLUMN_NAME)));
				
				RestaurantHours rh = new RestaurantHours();
				rh.setRanges(Calendar.SUNDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_HOUR_SUN))));
				rh.setRanges(Calendar.MONDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_HOUR_MON))));
				rh.setRanges(Calendar.TUESDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_HOUR_TUE))));
				rh.setRanges(Calendar.WEDNESDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_HOUR_WED))));
				rh.setRanges(Calendar.THURSDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_HOUR_THU))));
				rh.setRanges(Calendar.FRIDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_HOUR_FRI))));
				rh.setRanges(Calendar.SATURDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_HOUR_SAT))));
				current.setHours(rh);
				
				current.setFavorite(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_FAVORITE)) == 1);
				current.setOnTheCard(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_ON_THE_CARD)) == 1);
				current.setOffCampus(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_OFF_CAMPUS)) == 1);
				current.setType(c.getString(c.getColumnIndex(DBAdapter.COLUMN_TYPE)));
				
				cache.add(current);
				cached.add(false);
			} while (c.moveToNext());
		}
		mainDataCached = true;
		c.close();
		close();

	}
	
	public static void cacheMapData() {
		if (mapDataCached)
			return;
		if (!mainDataCached)
			cacheMainData();
		makeReadable();	
		Cursor c = adapter.getCursor(new String[] {
			DBAdapter.COLUMN_LATITUDE, 
			DBAdapter.COLUMN_LONGITUDE,
			DBAdapter.COLUMN_ICON});
		if (c.moveToFirst()) {
			int i = 0;
			do {
				cache.get(i).setLocation(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LATITUDE)), 
						c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LONGITUDE)));
				cache.get(i++).setIcon(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_ICON)));
			} while (c.moveToNext());
		}
		mapDataCached = true;
		c.close();
		close();
	}
	
	public static int cacheRestaurant(long rowID) {
		if (!mainDataCached)
			cacheMainData();
		int i = IDs.indexOf(rowID);
		if (cached.get(i))
			return i;
		makeReadable();	
		ArrayList<String> columnsToRead = new ArrayList<String>();
		if (!mapDataCached) {
			columnsToRead.add(DBAdapter.COLUMN_LATITUDE);
			columnsToRead.add(DBAdapter.COLUMN_LONGITUDE);
			columnsToRead.add(DBAdapter.COLUMN_ICON);
		}
		columnsToRead.add(DBAdapter.COLUMN_DESCRIPTION);
		columnsToRead.add(DBAdapter.COLUMN_PHONE_NUMBER);
		columnsToRead.add(DBAdapter.COLUMN_URL);
		columnsToRead.add(DBAdapter.COLUMN_MENU);
		
		String [] clmns = new String[0];
		Cursor c = adapter.getCursor(columnsToRead.toArray(clmns), rowID);
		if (!c.moveToFirst()) 
			throw new RuntimeException("Cannot cache restaurant which doesnt exist");
		if (i < 0)
			throw new RuntimeException("error, descrepency between database and memory cache");
		
		Restaurant r = cache.get(i);
		if (!mapDataCached) {
			r.setLocation(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LATITUDE)), 
					c.getInt(c.getColumnIndex(DBAdapter.COLUMN_LONGITUDE)));
			r.setIcon(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_ICON)));
		}
		
		r.setDescription(c.getString(c.getColumnIndex(DBAdapter.COLUMN_DESCRIPTION)));
		r.setPhoneNumber(c.getString(c.getColumnIndex(DBAdapter.COLUMN_PHONE_NUMBER)));
		r.setUrl(c.getString(c.getColumnIndex(DBAdapter.COLUMN_URL)));
		// TODO remove comments when menu functional
		//r.setMenu(DBAdapter.getMenuFromXml(c.getString(c.getColumnIndex(DBAdapter.COLUMN_DESCRIPTION))));
		
		c.close();
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
	
	private static void resetRestaurantCache() {
		int restaurants = getIDs().size();
		cache = new ArrayList<Restaurant>();
		cache.ensureCapacity(restaurants);
		cached = new ArrayList<Boolean>();
		cached.ensureCapacity(restaurants);
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
	
	private static void initializeUpdateStack() {
		if (updated == null)
			updated = new Stack<UpdateItem>();
	}
	
	public static class UpdateItem {
		
		public static final int NAME = 0;
		public static final int HOURS = 1;
		public static final int MENU = 2;
		public static final int DESCRIPTION = 3;
		public static final int TYPE = 4; 
		public static final int ICON = 5;
		public static final int LATITUDE = 6;
		public static final int LONGITUDE = 7;
		public static final int FAVORITE = 8;
		public static final int ON_THE_CARD = 9;
		public static final int OFF_CAMPUS = 10; 
		public static final int PHONE_NUMBER = 11;
		public static final int URL = 12;
		
		private int _i;
		private int _field;
		private Object _oldVal;
		
		public UpdateItem(int i, int field, Object old) {
			_i = i;
			_field = field;
			_oldVal = old;
		}
		
		// pre: adapter is writable
		public boolean commit() {
			switch (_field) {
			case FAVORITE:
				return adapter.updateColumn(IDs.get(_i), DBAdapter.COLUMN_FAVORITE, cache.get(_i).favorite());
				
				// no other updateItem types needed yet, so none implemented
				
			default:
				return false;
			}
		}
		
		public void revert() {
			switch (_field) {
			case FAVORITE:
				cache.get(_i).setFavorite((Boolean)_oldVal);
				break;
				
				// same here 
				
			}
		}

	}
}
