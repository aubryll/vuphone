package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import edu.vanderbilt.vuphone.android.map.AllLocations;
import edu.vanderbilt.vuphone.android.objects.Range;
import edu.vanderbilt.vuphone.android.objects.Restaurant;
import edu.vanderbilt.vuphone.android.objects.RestaurantAdapter;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;
import edu.vanderbilt.vuphone.android.objects.Time;
import edu.vanderbilt.vuphone.android.storage.DBAdapter;

public class Main extends ListActivity {
	
	// austin added this to toggle the more inane log statements
	public static final boolean DEBUG = true; 
	
	public static Context applicationContext;
	/**The first case in the menu*/
	private static final int MENU_ITEM_VIEW_MAP = 0;
	/** The second case in the menu */
	private static final int MENU_ITEM_MARK_FAVS = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// clunky mechanic with gross file dependency,
		// but DBWrapper needs Main.mainContext for its
		// calls to DBAdapter
		if (applicationContext == null)
			applicationContext = getApplicationContext();
		
		//deleteAllRestaurants();
		//addRandomRestaurantsToDB(20);
				
		RestaurantAdapter ra = new RestaurantAdapter(this, RestaurantAdapter.OPEN_CLOSED);
		//ra.setSort(RestaurantAdapter.FAVORITE_OPEN_CLOSED);
		setListAdapter(ra); // redisplay using correct sorting
		
		getListView().setTextFilterEnabled(true);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (id<0)
			return;
		Log.i("dining", "position " + position + " was clicked");
		
		// starts restaurant details page and sends index of restaurant
		Intent toDetails = new Intent(this, RestaurantDetails.class);
		toDetails.putExtra(RestaurantDetails.RESTAURANT_ID, id);

		startActivity(toDetails);
	}

	/** Creates list of actions for user when the menu button is clicked */
	public boolean onCreateOptionsMenu(Menu menu) { // do we need to call the super here?
		menu.add(0, MENU_ITEM_VIEW_MAP, 0, "View Map");
		menu.add(0, MENU_ITEM_MARK_FAVS, 0, "Mark Favorites");
		return true;
	}

	/** Handles what happens when each menu item is clicked */
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_ITEM_VIEW_MAP:
			Intent i = new Intent(this, AllLocations.class);
//			i.putExtra(AllLocations.EXTRA_LONGITUDES, LOCATION_LONGITUDES);
//			i.putExtra(AllLocations.EXTRA_LATITUDES, LOCATION_LATITUDES);
//			i.putExtra(AllLocations.EXTRA_LOCATIONS, RESTAURANTS);
			startActivity(i);
			return true;
		case MENU_ITEM_MARK_FAVS:
			Intent i2 = new Intent(this, MarkFavs.class);
//			i2.putExtra(MarkFavs.EXTRA_FAVORITES, FAVORITES);
			startActivity(i2);
			return true;
		default:
			return false;
		}
	}
	
	
	// placeholder/temporary methods below
	
	// MAY CONFUSE DBWrapper IF USED AFTER ANY OTHER DB FUNCTIONS
	private void deleteAllRestaurants() {
		Log.i("test", "opening writable database.");
		DBAdapter adapt = new DBAdapter(this);
		adapt.openWritable();
		ArrayList<Long> ids = Restaurant.getIDs();
		Log.i("test", "deleting database contents");
		for (int i = 0; i<ids.size(); i++)
			adapt.deleteRestaurant(ids.get(i));
		adapt.close();
	}

	private void addRandomRestaurantsToDB(int numRest) {
		String []letters = {"a", "b", "c", "d", "e", "f", "g", "h", 
				"i","k", "l", "m", "n","o","p","q","r",
				"s","t","u","v","w","x","y","z"};
		Log.i("test", "loading database with valid random data");
		Random r = new Random();
		for (int i = 1; i <= numRest; i++) {
			RestaurantHours rh = new RestaurantHours();
			for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
				int ranges = r.nextInt(2) + 1;
				for (int k = 0; k < ranges; k++) {
					Time start = new Time(r.nextInt(12 / ranges) + k * 12, r
							.nextInt(59));
					Time stop = new Time(
							(r.nextInt(12) + 12) / ranges + k * 12, r
									.nextInt(59));
					rh.addRange(day, new Range(start, stop));
				}
			}
			Restaurant restaurant = new Restaurant();
			String name = new String();
			for (int j = 0; j<7; j++)
				name = name + letters[r.nextInt(letters.length)];
			restaurant.setAttributes(name + " " + i, rh, r.nextBoolean(), r.nextInt(), r
					.nextInt(), null, 
					"Known for its fine cuisine, this is the restaurant Restaurant " + name + " " + i, 0x0, true,
					 false, null, null);
			restaurant.create();
			Log.i("test", restaurant.toString());
		}
	}

}
