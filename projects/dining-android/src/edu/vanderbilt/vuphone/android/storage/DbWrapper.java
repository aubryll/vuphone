package edu.vanderbilt.vuphone.android.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
/**
 * @author austin
 *
 */
public class DbWrapper {

	
	/** State codes for the underlying DbAdapter */
	private static final int CLOSED = 0;
	private static final int READABLE = 1;
	private static final int WRITABLE = 2;
	
	/** the adapter whose access this class aims to intelligently cache */
	private static DbAdapter adapter;
	
	/** the state of read/write access of the adapter */
	private static int state;
	
	/** The array of rowIDs, necessarily in ascending order */
	private static ArrayList<Long> IDs;
		
	/** The cache of Restaurants, whose data is completed as access and caching calls are made */
	private static ArrayList<Restaurant> cache;
	
	/** indicates whether IDs is nonnull */
	private static boolean idsCached = false;
	
	/** indicates the restaurant data needed for the first application page has been cached */
	private static boolean mainDataCached = false;
	
	/** indicates the restaurant data needed for the map page has been cached */
	private static boolean mapDataCached = false;
	
	/** indicated the restaurant data for each restaurant, index corresponding to IDs and cache,
	 * 	has all been cached	 */
	private static ArrayList<Boolean> cached;
	
	/** set of items to update or revert in the database and cache, once commit() is called */
	private static ArrayList<UpdateItem> updated;
	

	/**
	 * returns a sorted reference to an array of all the restaurant IDs
	 * @return the array of IDs, 
	 * 		THIS CANNOT BE MODIFIED, OR UNDEFINED BEHAVIOR MAY OCCUR
	 * to modify the list, see copyIDs()
	 */
	protected static ArrayList<Long> getIDs() {
		cacheIDs();
		return IDs;
	}
	
	/** returns a sorted array of all the restaurant IDs
	 * 
	 * @return the array of IDs. This can be safely modified, and will not
	 * be updated if the underlying database cache changes
	 */
	@SuppressWarnings("unchecked")
	protected static ArrayList<Long> copyIDs() {
		cacheIDs();
		return (ArrayList<Long>) IDs.clone();
	}
	
	
	/**
	 * Caches and then returns the restaurant with specified rowID
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	the Restaurant in cache; it cannot be modified
	 */
	protected static Restaurant get(long rowID) { 
		int i = cacheRestaurant(rowID);
		return cache.get(i);
	}
	
	
	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns the name of the given restaurant
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	the name of the restaurant
	 */
	protected static String getName(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).getName();
	}

	/** 
	 * Caches the map data for every restaurant if not already done
	 * and returns the latitude of the given restaurant
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	the latitude of the restaurant
	 */
	protected static int getLat(long rowID) {
		cacheMapData();
		return cache.get(getI(rowID)).getLat();
	}

	/** 
	 * Caches the map data for every restaurant if not already done
	 * and returns the longitude of the given restaurant
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	the longitude of the restaurant
	 */
	protected static int getLon(long rowID) {
		cacheMapData();
		return cache.get(getI(rowID)).getLon();
	}


	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns the hours of the given restaurant
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	the hours of the restaurant
	 */
	protected static RestaurantHours getHours(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).getHours();
	}

	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns the type of the given restaurant
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	the type of the restaurant
	 */
	protected static String getType(long rowID) { 
		cacheMainData();
		return cache.get(getI(rowID)).getType();
	}

	/** 
	 * Caches the map data for every restaurant if not already done
	 * and returns the icon of the given restaurant
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	the icon of the restaurant
	 */
	protected static int getIcon(long rowID) {
		cacheMapData();
		return cache.get(getI(rowID)).getIcon();
	}

	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns whether or not the given restaurant is a user favorite
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	whether or not the restaurant is a favorite
	 */
	protected static boolean favorite(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).favorite();
	}
	
	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns whether or not the given restaurant accepts the meal plan
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	whether or not the restaurant accepts the meal plan
	 */
	protected static boolean mealPlanAccepted(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).mealPlanAccepted();
	}

	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns whether or not the given restaurant accepts meal money
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	whether or not the restaurant accepts meal money
	 */
	protected static boolean mealMoneyAccepted(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).mealMoneyAccepted();
	}

	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns whether or not the given restaurant is on the card
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	whether or not the restaurant is on the card
	 */
	protected static boolean onTheCard(long rowID) { 
		cacheMainData();
		return cache.get(getI(rowID)).onTheCard();
	}

	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns whether or not the given restaurant is a Taste of Nashville participant
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	whether or not the restaurant is a Taste of Nashville participant
	 */
	protected static boolean tasteOfNashville(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).tasteOfNashville();
	}

	/** 
	 * Caches the main data for every restaurant if not already done
	 * and returns whether or not the given restaurant is off campus
	 * @param rowID
	 * 	id of the needed restaurant
	 * @return
	 * 	whether or not the restaurant is off campus
	 */
	protected static boolean offCampus(long rowID) {
		cacheMainData();
		return cache.get(getI(rowID)).offCampus();
	}
	
	
	/**
	 * creates the restaurant and adds it to cache, if it exists
	 * @param r
	 * 	the restaurant to create
	 * @return
	 * 	whether or not the creation was successful
	 */
	protected static boolean create(Restaurant r) {
		makeWritable();
		long rID = adapter.createRestaurant(r);
		if (rID >= 0) {
			if (idsCached)
				IDs.add(rID);
				// could potentially be a problem if this addition causes IDs
				// not to be sorted. (Shouldnt in theory since ID is set to 
				// autoincrement
			if (mainDataCached)
				cache.add(r);
			return true;
		} else
			return false;
	}
	
	/**
	 * Updates both the database and cache immediately with new values
	 * @param rowID
	 * 	id of restaurant to modify
	 * @param updated
	 * 	restaurant containing the new values for the database
	 * @return true if the operation was a success
	 */
	protected static boolean update(long rowID, Restaurant updated) {
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
	
	/** 
	 * Updates the local cache with a new favorite value for a restaurant, and enters the change 
	 * into a list of updates.
	 * Call commit() to write changes to database or revert() to revert to old settings
	 * @param rowID
	 * 	id of restaurant to modify
	 * @param favorite
	 * 	new favorite value
	 * @return true if anything was changed
	 */
	protected static boolean setFavorite(long rowID, boolean favorite) {
		cacheMainData();
		int i = getI(rowID);
		if (i<0 || cache.get(i).favorite()==favorite)
			return false;
		initializeUpdateStack();
		updated.add(new UpdateItem(i, UpdateItem.FAVORITE, !favorite));
		cache.get(i).setFavorite(favorite);
		return true;
	}
	
	/**
	 * Commits to the database all the changes previously made to the cache
	 * with setX()
	 * @return true if anything was changed
	 */
	protected static boolean commit() {
		if (updated == null || updated.isEmpty())
			return false;
		else
			makeWritable();
		for (int i = 0; i < updated.size(); i++) {
			if (!updated.get(i).commit())
				throw new RuntimeException("Unable to commit all changes, undefined database behavior");
		}
		updated = null;
		close();
		return true;
	}

	/**
	 * Undos the changes previously made to the cache with setX() since the last
	 * call to commit() or revert()
	 */
	protected static void revert() {
		if (updated == null)
			return;
		for (int i = 0; i < updated.size(); i++)
			updated.get(i).revert();
	}
	
	/**
	 * Deletes a restaurant from the database and cache
	 * @param rowID
	 * 	id of restaurant to delete
	 * @return true if successful
	 */
	protected static boolean delete(long rowID) {
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
	
	/**
	 * Deletes every restaurant from the database and clears the cache
	 * @return true if successful
	 */
	protected static boolean deleteAll() {
		makeWritable();
		if (adapter.deleteAllRestaurants()) {
			idsCached = false;
			mainDataCached = false;
			mapDataCached = false;
			return true;
		} else return false;
	}
	
	/**
	 * Populates IDs with all the restaurant ids and sorts
	 */
	protected static void cacheIDs() {
		if (idsCached)
			return;
		IDs = new ArrayList<Long>();
		makeReadable();
		Cursor c = adapter.getCursor(new String[] {DbAdapter.COLUMN_ID});
		if (c.moveToFirst()) {
			do {
				IDs.add(c.getLong(c.getColumnIndex(DbAdapter.COLUMN_ID)));
			} while (c.moveToNext());
		}
		Collections.sort(IDs);
		idsCached = true;
		c.close();
	}
	
	/**
	 * Populates the cache with all the data needed to render the Main activity
	 */
	protected static void cacheMainData() {
		if (mainDataCached)
			return;
		resetRestaurantCache(); // ensures IDs are cached
		makeReadable();
		Cursor c = adapter.getCursor(new String[] {
			DbAdapter.COLUMN_NAME,			
			DbAdapter.COLUMN_HOUR_SUN, 	
			DbAdapter.COLUMN_HOUR_MON, 	
			DbAdapter.COLUMN_HOUR_TUE, 	
			DbAdapter.COLUMN_HOUR_WED, 	
			DbAdapter.COLUMN_HOUR_THU, 	
			DbAdapter.COLUMN_HOUR_FRI, 	
			DbAdapter.COLUMN_HOUR_SAT, 	
			DbAdapter.COLUMN_BOOLEANS,
			DbAdapter.COLUMN_TYPE});
		if (c.moveToFirst()) {
			do {
				Restaurant current = new Restaurant();
				current.setName(c.getString(c.getColumnIndex(DbAdapter.COLUMN_NAME)));
				
				RestaurantHours rh = new RestaurantHours();
				rh.setRanges(Calendar.SUNDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DbAdapter.COLUMN_HOUR_SUN))));
				rh.setRanges(Calendar.MONDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DbAdapter.COLUMN_HOUR_MON))));
				rh.setRanges(Calendar.TUESDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DbAdapter.COLUMN_HOUR_TUE))));
				rh.setRanges(Calendar.WEDNESDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DbAdapter.COLUMN_HOUR_WED))));
				rh.setRanges(Calendar.THURSDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DbAdapter.COLUMN_HOUR_THU))));
				rh.setRanges(Calendar.FRIDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DbAdapter.COLUMN_HOUR_FRI))));
				rh.setRanges(Calendar.SATURDAY, RestaurantHours.inflate(c.getLong(c.getColumnIndex(DbAdapter.COLUMN_HOUR_SAT))));
				current.setHours(rh);
				
				boolean [] booleans = DbAdapter.booleansDecode(c.getInt(c.getColumnIndex(DbAdapter.COLUMN_BOOLEANS)));
				current.setFavorite(booleans[0]);
				current.setPlanAccepted(booleans[1]);
				current.setMoneyAccepted(booleans[2]);
				current.setOffCampus(booleans[3]);
				current.setType(c.getString(c.getColumnIndex(DbAdapter.COLUMN_TYPE)));
				
				cache.add(current);
				cached.add(false);
			} while (c.moveToNext());
		}
		mainDataCached = true;
		c.close();
		close();

	}
	
	/**
	 * Populates the cache with all the data needed to render the map activity
	 */
	protected static void cacheMapData() {
		if (mapDataCached)
			return;
		if (!mainDataCached)
			cacheMainData();
		makeReadable();	
		Cursor c = adapter.getCursor(new String[] {
			DbAdapter.COLUMN_LATITUDE, 
			DbAdapter.COLUMN_LONGITUDE,
			DbAdapter.COLUMN_ICON});
		if (c.moveToFirst()) {
			int i = 0;
			do {
				cache.get(i).setLocation(c.getInt(c.getColumnIndex(DbAdapter.COLUMN_LATITUDE)), 
						c.getInt(c.getColumnIndex(DbAdapter.COLUMN_LONGITUDE)));
				cache.get(i++).setIcon(c.getInt(c.getColumnIndex(DbAdapter.COLUMN_ICON)));
			} while (c.moveToNext());
		}
		mapDataCached = true;
		c.close();
		close();
	}
	
	/**
	 * Populates the cache with all the data for a particular restaurant
	 * @param rowID
	 * 	id of the restaurnt to cache
	 * @return
	 * 	the index of the cached restaurant
	 */
	protected static int cacheRestaurant(long rowID) {
		if (!mainDataCached)
			cacheMainData();
		int i = getI(rowID);
		if (cached.get(i))
			return i;
		makeReadable();	
		ArrayList<String> columnsToRead = new ArrayList<String>();
		if (!mapDataCached) {
			columnsToRead.ensureCapacity(7);
			columnsToRead.add(DbAdapter.COLUMN_LATITUDE);
			columnsToRead.add(DbAdapter.COLUMN_LONGITUDE);
			columnsToRead.add(DbAdapter.COLUMN_ICON);
		} else columnsToRead.ensureCapacity(4);
		columnsToRead.add(DbAdapter.COLUMN_DESCRIPTION);
		columnsToRead.add(DbAdapter.COLUMN_PHONE_NUMBER);
		columnsToRead.add(DbAdapter.COLUMN_URL);
		columnsToRead.add(DbAdapter.COLUMN_MENU);
		
		String [] clmns = new String[0]; // for the type
		Cursor c = adapter.getCursor(columnsToRead.toArray(clmns), rowID);
		if (!c.moveToFirst()) 
			throw new RuntimeException("Cannot cache restaurant which doesnt exist");
		if (i < 0)
			throw new RuntimeException("error, descrepency between database and memory cache");
		
		Restaurant r = cache.get(i);
		if (!mapDataCached) {
			r.setLocation(c.getInt(c.getColumnIndex(DbAdapter.COLUMN_LATITUDE)), 
					c.getInt(c.getColumnIndex(DbAdapter.COLUMN_LONGITUDE)));
			r.setIcon(c.getInt(c.getColumnIndex(DbAdapter.COLUMN_ICON)));
		}
		
		r.setDescription(c.getString(c.getColumnIndex(DbAdapter.COLUMN_DESCRIPTION)));
		r.setPhoneNumber(c.getString(c.getColumnIndex(DbAdapter.COLUMN_PHONE_NUMBER)));
		r.setUrl(c.getString(c.getColumnIndex(DbAdapter.COLUMN_URL)));
		// TODO remove comments when menu functional
		//r.setMenu(DBAdapter.getMenuFromXml(c.getString(c.getColumnIndex(DBAdapter.COLUMN_DESCRIPTION))));
		
		c.close();
		close();
		cached.set(i, true);
		return i;
	}
	
	
	/**
	 * Destroys the restaurant cache, but not the ID cache
	 */
	private static void resetRestaurantCache() {
		int restaurants = getIDs().size();
		cache = new ArrayList<Restaurant>();
		cache.ensureCapacity(restaurants);
		cached = new ArrayList<Boolean>();
		cached.ensureCapacity(restaurants);
		mainDataCached = false;
		mapDataCached = false;
	}
	
	/**
	 * Initializes the database if not already
	 */
	private static void initialize() {
		if (adapter == null) {
			adapter = new DbAdapter(Main.applicationContext);
			state = CLOSED;
		}
	}
	
	/**
	 * Brings the database to a writable state
	 * Should be called before any write calls
	 */
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
	
	/**
	 * Brings the database to a readable state
	 * Should be called before any read calls
	 */
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
	
	/**
	 * Closes the underlying database adapter 
	 * no read/writes are to be performed in the 
	 * near future
	 */
	protected static void close() {
		initialize();
		switch (state) {
		case READABLE:
		case WRITABLE:
			adapter.close();
			state = CLOSED;
			return;
		case CLOSED:
			return;
		}
	}

	/**
	 * Returns the cache index of the restaurant with the specified rowID
	 * @param rowID
	 * 	id of the restaurant whose index to return
	 * @return
	 * 	the index of the restaurant in cache
	 */
	protected static int getI(long rowID) {
		int i = Collections.binarySearch(IDs, rowID);
		if (i < 0)
			throw new RuntimeException("Restaurant does not exist");
		return i;
	}
	
	private static void initializeUpdateStack() {
		if (updated == null) {
			updated = new ArrayList<UpdateItem>();
		}
	}
	
	protected static class UpdateItem {
		
		protected static final int NAME = 0;
		protected static final int HOURS = 1;
		protected static final int MENU = 2;
		protected static final int DESCRIPTION = 3;
		protected static final int TYPE = 4; 
		protected static final int ICON = 5;
		protected static final int LATITUDE = 6;
		protected static final int LONGITUDE = 7;
		protected static final int FAVORITE = 8;
		protected static final int PLAN_ACCEPTED = 9;
		protected static final int MONEY_ACCEPTED = 10;
		protected static final int OFF_CAMPUS = 11; 
		protected static final int PHONE_NUMBER = 12;
		protected static final int URL = 13;
		
		private int _i;
		private int _field;
		private Object _oldVal;
		
		private static boolean booleansCommited;
		
		protected UpdateItem(int i, int field, Object old) {
			_i = i;
			_field = field;
			_oldVal = old;
			booleansCommited = false;
		}
		
		// pre: adapter is writable
		protected boolean commit() {
			switch (_field) {
			case FAVORITE:  
			case PLAN_ACCEPTED:
			case MONEY_ACCEPTED:
			case OFF_CAMPUS:
				if (!booleansCommited) {
					booleansCommited = true;
					return adapter.updateColumn(getIDs().get(_i), DbAdapter.COLUMN_BOOLEANS, 
							DbAdapter.booleansEncode(new boolean[] {cache.get(_i).favorite(), cache.get(_i).mealPlanAccepted(), 
									cache.get(_i).mealMoneyAccepted(), cache.get(_i).offCampus()}));
				}
				else return true;
				
				// no other updateItem types needed yet, so none implemented
				
			case NAME: 
				return adapter.updateColumn(getIDs().get(_i), DbAdapter.COLUMN_NAME, cache.get(_i).getName());
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
		
		protected void revert() {
			switch (_field) {
			case FAVORITE:
				cache.get(_i).setFavorite((Boolean)_oldVal);
				break;
				
				// same here 
				
			}
		}

	}
}
