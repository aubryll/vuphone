package edu.vanderbilt.vuphone.android.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
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
	

	public static ArrayList<Long> getIDs() {
		cacheIDs();
		return IDs;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Long> copyIDs() {
		cacheIDs();
		return (ArrayList<Long>) IDs.clone();
	}
	
	
	public static Restaurant get(long rowID) { 
		int i = cacheRestaurant(rowID);
		return cache.get(i);
	}
	
	public static String getName(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).getName();
	}

	public static int getLat(long rowID) {
		cacheMapData();
		return cache.get(getI(rowID)).getLat();
	}

	public static int getLon(long rowID) {
		cacheMapData();
		return cache.get(getI(rowID)).getLon();
	}

	public static RestaurantHours getHours(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).getHours();
	}

	public static String getType(long rowID) { 
		cacheMainData();
		return cache.get(getI(rowID)).getType();
	}

	public static int getIcon(long rowID) {
		cacheMapData();
		return cache.get(getI(rowID)).getIcon();
	}

	public static boolean favorite(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).favorite();
	}

	public static boolean onTheCard(long rowID) { 
		cacheMainData();
		return cache.get(getI(rowID)).onTheCard();
	}

	public static boolean offCampus(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).offCampus();
	}
	
	
	public static boolean create(Restaurant r) {
		makeWritable();
		long rID = adapter.createRestaurant(r);
		if (rID >= 0) {
			if (idsCached)
				IDs.add(rID);
			if (mainDataCached)
				cache.add(r);
			return true;
		} else
			return false;
	}
	
	// updates the database and cache immediately with new values
	public static boolean update(long rowID, Restaurant updated) {
		makeWritable();
		int i = getI(rowID);
		if (i<0)
			return false;
		boolean success =  adapter.updateRestaurant(rowID, updated); 
		close();
		if (mainDataCached && success)
			cache.set(i, updated); 
		return success;
	}
	
	// updates only the local cache with new favorite, call commit() to write changes to database
	// or revert() to revert to old settings
	public static boolean setFavorite(long rowID, boolean favorite) {
		cacheMainData();
		int i = getI(rowID);
		if (i<0 || cache.get(i).favorite()==favorite)
			return false;
		initializeUpdateStack();
		updated.push(new UpdateItem(i, UpdateItem.FAVORITE, !favorite));
		cache.get(i).setFavorite(favorite);
		return true;
	}
	
	public static boolean commit() {
		if (updated == null || updated.empty())
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
		if (updated == null)
			return;
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
				IDs.remove(i); // IDs cached with getIDs() above
			}
			return true;
		} else return false;
	}
	
	public static boolean deleteAll() {
		makeWritable();
		if (adapter.deleteAllRestaurants()) {
			idsCached = false;
			mainDataCached = false;
			mapDataCached = false;
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
			DBAdapter.COLUMN_BOOLEANS,
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
				
				boolean [] booleans = DBAdapter.booleansDecode(c.getInt(c.getColumnIndex(DBAdapter.COLUMN_BOOLEANS)), 4);
				current.setFavorite(booleans[0]);
				current.setPlanAccepted(booleans[1]);
				current.setMoneyAccepted(booleans[2]);
				current.setOffCampus(booleans[3]);
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
		int i = getI(rowID);
		if (cached.get(i))
			return i;
		makeReadable();	
		ArrayList<String> columnsToRead = new ArrayList<String>();
		if (!mapDataCached) {
			columnsToRead.ensureCapacity(7);
			columnsToRead.add(DBAdapter.COLUMN_LATITUDE);
			columnsToRead.add(DBAdapter.COLUMN_LONGITUDE);
			columnsToRead.add(DBAdapter.COLUMN_ICON);
		} else columnsToRead.ensureCapacity(4);
		columnsToRead.add(DBAdapter.COLUMN_DESCRIPTION);
		columnsToRead.add(DBAdapter.COLUMN_PHONE_NUMBER);
		columnsToRead.add(DBAdapter.COLUMN_URL);
		columnsToRead.add(DBAdapter.COLUMN_MENU);
		
		String [] clmns = new String[0]; // for the type
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
		case WRITABLE:
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

	private static int getI(long rowID) {
		int i = getIDs().indexOf(rowID);
		if (i < 0)
			throw new RuntimeException("Restaurant does not exist");
		return i;
	}
	
	private static void initializeUpdateStack() {
		if (updated == null) {
			updated = new Stack<UpdateItem>();
		}
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
		public static final int PLAN_ACCEPTED = 9;
		public static final int MONEY_ACCEPTED = 10;
		public static final int OFF_CAMPUS = 11; 
		public static final int PHONE_NUMBER = 12;
		public static final int URL = 13;
		
		private int _i;
		private int _field;
		private Object _oldVal;
		
		private static boolean booleansCommited;
		
		public UpdateItem(int i, int field, Object old) {
			_i = i;
			_field = field;
			_oldVal = old;
			booleansCommited = false;
		}
		
		// pre: adapter is writable
		public boolean commit() {
			switch (_field) {
			case FAVORITE:  
			case PLAN_ACCEPTED:
			case MONEY_ACCEPTED:
			case OFF_CAMPUS:
				if (!booleansCommited) {
					booleansCommited = true;
					return adapter.updateColumn(getIDs().get(_i), DBAdapter.COLUMN_BOOLEANS, 
							DBAdapter.booleansEncode(new boolean[] {cache.get(_i).favorite(), cache.get(_i).mealPlanAccepted(), 
									cache.get(_i).mealMoneyAccepted(), cache.get(_i).offCampus()}));
				}
				else return true;
				
				// no other updateItem types needed yet, so none implemented
				
			case NAME: 
				return adapter.updateColumn(getIDs().get(_i), DBAdapter.COLUMN_NAME, cache.get(_i).getName());
			case HOURS:
				//return adapter.updateColumn(IDs.get(_i), DBAdapter.COLUMN_HOURS, (String)_oldVal);
			case MENU:
			case DESCRIPTION:
			case TYPE:
			case ICON:
			case LATITUDE:
			case LONGITUDE:
			case PHONE_NUMBER:
			case URL:
				
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
