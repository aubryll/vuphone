package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import edu.vanderbilt.vuphone.android.map.AllLocations;
import edu.vanderbilt.vuphone.android.objects.Range;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;
import edu.vanderbilt.vuphone.android.objects.Time;
import edu.vanderbilt.vuphone.android.storage.DBAdapter;

public class Main extends ListActivity {
	
	// austin added this to toggle the more inane log statements
	public static final boolean DEBUG = true; 
	
	/**The first case in the menu*/
	private static final int MENU_ITEM_VIEW_MAP = 0;
	/** The second case in the menu */
	private static final int MENU_ITEM_MARK_FAVS = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		RestaurantHours rh = new RestaurantHours();
		ArrayList<Range> monday = new ArrayList<Range>();
		Range st = new Range(new Time(3,30), new Time(4,45));
		monday.add(st);
		st = new Range(new Time(5,30), new Time(7,45));
		monday.add(st);
		rh.setMondayRanges(monday);
		
		ArrayList<Range> tues = new ArrayList<Range>();
		st = new Range(new Time(3,30), new Time(4,45));
		tues.add(st);
		
		rh.setTuesdayRanges(tues);
		
		DBAdapter adapt = new DBAdapter(this);
		adapt.openWritable();
		adapt.createRestaurant("poo", 12.1, 1.1, "poopo", false, rh);


		String[] from = new String[] { DBAdapter.COLUMN_NAME,
				DBAdapter.COLUMN_ID };
		int[] to = new int[] { android.R.id.text1, R.list_view.restaurantID };

		SimpleCursorAdapter sca = new SimpleCursorAdapter(
				getApplicationContext(), R.layout.list_view, adapt
						.fetchAllRestaurantsCursor(), from, to);

		setListAdapter(sca);
		getListView().setTextFilterEnabled(true);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Log.i("dining", "position " + position + " was clicked");
		TextView hiddenID = (TextView)v.findViewById(R.list_view.restaurantID);
		long h_id = 0;
		h_id = Long.parseLong(hiddenID.getText().toString());
		
		// starts restaurant details page and sends index of restaurant
		Intent toDetails = new Intent(this, RestaurantDetails.class);
		toDetails.putExtra("restaurant", h_id);

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

}
