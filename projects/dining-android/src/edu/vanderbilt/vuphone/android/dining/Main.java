package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import edu.vanderbilt.vuphone.android.map.AllLocations;
import edu.vanderbilt.vuphone.android.objects.Range;
import edu.vanderbilt.vuphone.android.objects.RestaurantAdapter;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;
import edu.vanderbilt.vuphone.android.objects.RestaurantMenu;
import edu.vanderbilt.vuphone.android.objects.Time;
import edu.vanderbilt.vuphone.android.storage.Restaurant;
import edu.vanderbilt.vuphone.android.storage.StaticRestaurantData;

public class Main extends ListActivity {
	
	
	public static Context applicationContext;
	/**The first case in the menu*/
	private static final int MENU_ITEM_VIEW_MAP = 0;
	/** The second case in the menu */
	private static final int MENU_ITEM_MARK_FAVS = 1;
	/** The case in menu to change sorting method */
	private static final int MENU_ITEM_CHOOSE_SORTING = 2;
	/** The custom adapter used to create the list */
	private RestaurantAdapter ra;
	
	private static final int NORMAL = 0;
	private static final int MARK_FAVS = 1;
	private int mode;
	private View doneButton;
	private View cancelButton;
	
	boolean [] checked;

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
		if (Restaurant.getIDs().isEmpty()) {
			(new StaticRestaurantData()).createAllRestaurants();
		}
		
		Log.i("Main", "before initializing content view");
		initializeContentView();
		
		Log.i("Main", "after initializing content view, now creating restaurant adapter");
		ra = new RestaurantAdapter(this,RestaurantAdapter.SAMPLE_FAVORITE_OPEN_CLOSED);//SAMPLE_FAVORITE_OPEN_CLOSED);
		checked = new boolean[] {true, false, true};
		Log.i("Main", "after initializing restaurant adapter");
		setListAdapter(ra);
		getListView().setTextFilterEnabled(true);
		Log.i("Main", "everything done");
				
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (id<0)   // if user clicked on a partition
			return;
		Log.i("dining", "position " + position + " was clicked");
		switch (mode) {
		case NORMAL:
			// starts restaurant details page and sends index of restaurant
			Intent toDetails = new Intent(this, RestaurantDetails.class);
			toDetails.putExtra(RestaurantDetails.RESTAURANT_ID, id);
			
			startActivity(toDetails);
			break;
		case MARK_FAVS:
			Restaurant.setFavorite(id, !Restaurant.favorite(id));
			ra.notifyDataSetChanged();
			//getListView().recomputeViewAttributes(v); // TODO examin this
		}
	}

	/**This opens the dialog that allows the user to choose a new sorting option*/
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		final CharSequence[] items = {"Favorites, Open", "Open/Close times", "Open",
//				 "Favorites", "Alphabetical" };
//		final ArrayList<Integer> itemsIDs = new ArrayList<Integer>();
//		itemsIDs.add(RestaurantAdapter.SAMPLE_FAVORITE_OPEN_CLOSED);
//		itemsIDs.add(RestaurantAdapter.SAMPLE_FAVORITE_TIMES);
//		itemsIDs.add(RestaurantAdapter.SAMPLE_OPEN_CLOSED);
//		itemsIDs.add(RestaurantAdapter.SAMPLE_FAVORITE); 
//		itemsIDs.add(RestaurantAdapter.SAMPLE_ALPHABETICAL);
		
		CharSequence[] items = {"Open", "Time until open or close", "Favorite"};
		
		// this implementation is not very good and should be improved;
//		final ArrayList<Integer> itemsIDs = new ArrayList<Integer>();
//		itemsIDs.add(RestaurantAdapter.OPEN_CLOSED * RestaurantAdapter.LEVEL[0] +
//				RestaurantAdapter.SHOW_OPEN_PART);
//		itemsIDs.add(RestaurantAdapter.TIME_TO_CLOSE * RestaurantAdapter.LEVEL[1] +
//				(RestaurantAdapter.TIME_TO_OPEN + RestaurantAdapter.DESCENDING) * 
//				RestaurantAdapter.LEVEL[2]);
//		itemsIDs.add(RestaurantAdapter.SHOW_FAV_PART - RestaurantAdapter.SHOW_FAV_ICON + 
//				RestaurantAdapter.FAVORITE * RestaurantAdapter.LEVEL[3]);
		
		builder.setMultiChoiceItems(items, checked,
				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				if (which == 0 && !isChecked && checked[1])
					onClick(dialog, 1, false); // currently doesnt work for some reason...
				if (which == 1 && isChecked && !checked[0])
					onClick(dialog, 0, true);
					
					
				checked[which] = isChecked;
//				if (isChecked)
//					ra.setSort(ra.getSortType() + itemsIDs.get(which));
//				else 
//					ra.setSort(ra.getSortType() - itemsIDs.get(which));
			}
		});
		
				
//		builder.setSingleChoiceItems(items,
//				itemsIDs.indexOf(ra.getSortType()),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int item) {
//
//						ra.setSort(itemsIDs.get(item));
//
//					}
//				});

		builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				ra.setSort(checked[0], checked[1], checked[2]);
				dialog.dismiss();
			}
		});
		builder.setTitle("Sort by");

		return builder.create();
	}

	/** Creates list of actions for user when the menu button is clicked */
	public boolean onCreateOptionsMenu(Menu menu) { 
		menu.add(Menu.NONE, MENU_ITEM_VIEW_MAP, Menu.NONE, "View Map");
		menu.add(Menu.NONE, MENU_ITEM_MARK_FAVS, Menu.NONE, "Mark Favorites");
		menu.add(Menu.NONE, MENU_ITEM_CHOOSE_SORTING, Menu.NONE, "Sort");
		return true;
	}

	/** Handles what happens when each menu item is clicked */
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_ITEM_VIEW_MAP:
			Intent toMapView = new Intent(this, AllLocations.class);
			startActivity(toMapView);
			return true;
		case MENU_ITEM_MARK_FAVS:
			setModeMarkFavs();
			return true;
		case MENU_ITEM_CHOOSE_SORTING:
			showDialog(1);
			return true;
		default:
			return false;
		}
	}	
	
	private OnClickListener doneListener = new OnClickListener() {

		public void onClick(View v) {
			Restaurant.commit();
			setModeNormal();
		}
	};

	/** ends activity when cancel button is clicked */
	private OnClickListener cancelListener = new OnClickListener() {

		public void onClick(View v) {
			Restaurant.revert();
			setModeNormal();
		}
	};
	
	private void initializeContentView() {
		setContentView(R.layout.main);
		doneButton = findViewById(R.mark_favs.done);
		cancelButton = findViewById(R.mark_favs.cancel);
		((Button) doneButton).setOnClickListener(doneListener);
		((Button) cancelButton).setOnClickListener(cancelListener);
		mode = NORMAL;
	}
	
	private void setModeNormal() {
		mode = NORMAL;
		doneButton.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		ra.setSort();
	}
	
	private void setModeMarkFavs() {
		mode = MARK_FAVS;
		doneButton.setVisibility(View.VISIBLE);
		cancelButton.setVisibility(View.VISIBLE);
		ra.setShowFavIcon(true);
	}

	
	// PLACEHOLDER / TEMOPRARY METHODS BELOW
	
	
	private void deleteAllRestaurants() {/*
		Log.i("test", "opening writable database.");
		DBAdapter adapt = new DBAdapter(this);
		adapt.openWritable();
		ArrayList<Long> ids = Restaurant.getIDs();
		Log.i("test", "deleting database contents");
		for (int i = 0; i<ids.size(); i++)
			adapt.deleteRestaurant(ids.get(i));
		adapt.close();*/
		ArrayList<Long> ids = Restaurant.copyIDs();
		for (int i = 0; i < ids.size(); i++)
			Restaurant.delete(ids.get(i));
	}

	private void addRandomRestaurantsToDB(int numRest) {
		String []letters = {"a", "b", "c", "d", "e", "f", "g", "h", 
				"i","k", "l", "m", "n","o","p","q","r",
				"s","t","u","v","w","x","y","z"};
		int maxRanges = 2;
		int maxMenuItems = 10;
		int hourOffset = 5;
		Log.i("test", "loading database with valid random data");
		Random r = new Random();
		for (int i = 1; i <= numRest; i++) {
			RestaurantHours rh = new RestaurantHours();
			for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
				int ranges = r.nextInt(maxRanges) + 1;
				for (int k = 0; k < ranges; k++) {
					Time start = new Time((r.nextInt(12 / ranges) + k * 24/ranges + hourOffset) % 24, r
							.nextInt(59));
					Time stop = new Time(
							((r.nextInt(12) + 12) / ranges + k * 24/ranges + hourOffset) % 24, r
									.nextInt(59));
					rh.addRange(day, new Range(start, stop));
				}
			}
			RestaurantMenu menu = new RestaurantMenu();
			int items = r.nextInt(maxMenuItems);
			for (int k = 0; k < items; k++) {
				String name = new String();
				for (int j = 0; j<14; j++)
					name = name + letters[r.nextInt(letters.length)];
				menu.addItem(new RestaurantMenu.MenuItem(name, "A wonderful blend of nothing and everything to make something"));
			}
			String name = new String();
			for (int j = 0; j<7; j++)
				name = name + letters[r.nextInt(letters.length)];
			Restaurant restaurant = new Restaurant(name + " " + i, rh, r.nextBoolean() && r.nextBoolean(), r.nextInt(), r
					.nextInt(), "cafe", menu, 
					"Known for its fine cuisine, this is the restaurant Restaurant " + name + " " + i, R.drawable.dining, true,
					 false, false, "(615) 555-1234", "http://example.com");
			restaurant.create();
		}
	}

}
