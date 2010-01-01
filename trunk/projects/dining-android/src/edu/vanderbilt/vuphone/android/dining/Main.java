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
import android.provider.Settings.System;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import edu.vanderbilt.vuphone.android.map.AllLocations;
import edu.vanderbilt.vuphone.android.objects.Range;
import edu.vanderbilt.vuphone.android.objects.RestaurantAdapter;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;
import edu.vanderbilt.vuphone.android.objects.RestaurantMenu;
import edu.vanderbilt.vuphone.android.objects.Time;
import edu.vanderbilt.vuphone.android.storage.Restaurant;
import edu.vanderbilt.vuphone.android.storage.StaticRestaurantData;

/**
 * @author austin
 *
 */
public class Main extends ListActivity {
	
	public static Context applicationContext;
	public static boolean display24;

	// current mode and values
	private static final int NORMAL = 0;
	private static final int MARK_FAVS = 1;
	private int mode;
	
	private RestaurantAdapter ra;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// clunky mechanic with gross file dependency,
		// but DBWrapper needs Main.mainContext for its
		// calls to DBAdapter
		if (applicationContext == null)
			applicationContext = getApplicationContext();
		display24 = "24".equals(System.getString(this.getContentResolver(), System.TIME_12_24));
		
		// deleteAllRestaurants();
		// addRandomRestaurantsToDB(20);
		if (Restaurant.getIDs().isEmpty()) {
			(new StaticRestaurantData()).createAllRestaurants();
		}

		initializeContentView();
		checkedSort = new boolean[] {true, true, false, false};

		
		ra = new RestaurantAdapter(this, checkedSort[0], checkedSort[1], checkedSort[2], checkedSort[3]);
		
		setListAdapter(ra);
		getListView().setTextFilterEnabled(true);
		
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (id < 0) // if user clicked on a partition
			return;
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
		}
	}
	
	private void initializeContentView() {
		setContentView(R.layout.main);
		doneButton = findViewById(R.mark_favs.done);
		cancelButton = findViewById(R.mark_favs.cancel);
		((Button) doneButton).setOnClickListener(doneListener);
		((Button) cancelButton).setOnClickListener(cancelListener);
		mode = NORMAL;
	}
	
	// -------------------- MENU FUNCTIONS
	
	private static final int MENU_ITEM_VIEW_MAP = 0;
	private static final int MENU_ITEM_MARK_FAVS = 1;
	private static final int MENU_ITEM_CHOOSE_SORTING = 2;
	private static final int MENU_ITEM_VIEW_SETTINGS = 3;
	
	private View doneButton;
	private View cancelButton;
	
	// saved showFavIcon preference to reset to when switching back to normal
	boolean showFavIcon;

	/** Creates list of actions for user when the menu button is clicked */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_ITEM_VIEW_SETTINGS, Menu.NONE, "Settings").setIcon(
				getResources().getDrawable(
						android.R.drawable.ic_menu_preferences));
		menu.add(Menu.NONE, MENU_ITEM_CHOOSE_SORTING, Menu.NONE, "Sort").setIcon(
						getResources().getDrawable(
								android.R.drawable.ic_menu_agenda));
		menu.add(Menu.NONE, MENU_ITEM_MARK_FAVS, Menu.NONE, "Mark Favorites").setIcon(
						getResources().getDrawable(
								R.drawable.ic_menu_star));
		menu.add(Menu.NONE, MENU_ITEM_VIEW_MAP, Menu.NONE, "View Map").setIcon(
				getResources().getDrawable(android.R.drawable.ic_menu_mapmode));
		//TODO Settings menu button needs an action added to it when clicked.
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
			showDialog(DIALOG_SORT);
			return true;
		case MENU_ITEM_VIEW_SETTINGS:
			showDialog(DIALOG_SETTINGS);
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

	private void setModeNormal() {
		mode = NORMAL;
		doneButton.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		ra.setShowFavIcon(showFavIcon);
		ra.setSort();
		ra.notifyDataSetChanged();
	}

	private void setModeMarkFavs() {
		mode = MARK_FAVS;
		doneButton.setVisibility(View.VISIBLE);
		cancelButton.setVisibility(View.VISIBLE);
		showFavIcon = ra.getShowFavIcon();
		ra.setShowFavIcon(true);
		ra.notifyDataSetChanged();
	}

	
	// --------------- POP UP DIALOG FUNCTIONS

	private static final int DIALOG_SORT = 0;
	private static final int DIALOG_SETTINGS = 1;
	
	private boolean[] checkedSort;
	private boolean[] checkedSetting;
	private boolean sortSettingsModified = false;

	/**
	 * This opens the dialog that allows the user to choose a new sorting option
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SORT:
		default: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			CharSequence[] items = {"Favorite", "Open", "Time until close", "Near" };

			builder.setMultiChoiceItems(items, checkedSort,
					new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							checkedSort[which] = isChecked;
							((AlertDialog)dialog).getListView().setItemChecked(which, isChecked);
							
							if (which == 1 && !isChecked && checkedSort[2])
								onClick(dialog, 2, false);
							if (which == 2 && isChecked) {
								if (!checkedSort[1])
									onClick(dialog, 1, true);
								if (checkedSort[3])
									onClick(dialog, 3, false);
							}
							if (which == 3 && isChecked) {
								if (!getLocationWithUI()) 
									onClick(dialog, 3, false);
								else if (checkedSort[1])
									onClick(dialog, 2, false);
							}
						}
					});

			builder.setNeutralButton("Done",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							if (sortSettingsModified)
								ra.setSortPreserveSettings(checkedSort[0], checkedSort[1], checkedSort[2], checkedSort[3]);
							else 
								ra.setSort(checkedSort[0], checkedSort[1], checkedSort[2], checkedSort[3]);
							ra.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
			builder.setTitle("Sort by");

			return builder.create();
		}
		case DIALOG_SETTINGS:
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			CharSequence[] items = { "Show favorites icon", "Gray closed places", "Show distance", "Show place type"};
			checkedSetting = new boolean [] {ra.getShowFavIcon(), ra.getGrayClosed(), ra.getShowDistances(), ra.getShowRestaurantType()};
			
			builder.setMultiChoiceItems(items, checkedSetting, 
					new DialogInterface.OnMultiChoiceClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							checkedSetting[which]=isChecked;
							((AlertDialog)dialog).getListView().setItemChecked(which, isChecked);
							
							switch (which) {
							case 0:
								ra.setShowFavIcon(isChecked);
								sortSettingsModified = true;
								break;
							case 1:
								ra.setGrayClosed(isChecked);
								sortSettingsModified = true;
								break;
							case 2:
								if (isChecked)
									if (getLocationWithUI()) {
										ra.setShowDistances(true); // TODO find a sane way to deal with this if false
									} else {
										onClick(dialog, 2, false);
									}
								else ra.setShowDistances(false);
								break;
							case 3:
								ra.setShowRestaurantType(isChecked);
							}
							
						}
					});
			
			builder.setNeutralButton("Done",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							ra.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
			
			builder.setNegativeButton("Set Defaults", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ra.setBoolsToDefault();

							boolean checked[] = {ra.getShowFavIcon(), ra.getGrayClosed(), ra.getShowDistances(), ra.getShowRestaurantType()};
							for (int i = 0; i<checked.length; i++) {
								checkedSetting[i]=checked[i];
								((AlertDialog)dialog).getListView().setItemChecked(i, checked[i]);
							}
							
							sortSettingsModified = false;
							ra.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
			
			builder.setTitle("Settings");

			return builder.create();
			
		}
		}
	}
	
	
	
	private boolean getLocationWithUI() {
		if (!ra.refreshDistances()) {
			Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
			return false;
		} return true;
	}

	// PLACEHOLDER / TEMOPRARY METHODS BELOW

	private void deleteAllRestaurants() {
		ArrayList<Long> ids = Restaurant.copyIDs();
		for (int i = 0; i < ids.size(); i++)
			Restaurant.delete(ids.get(i));
	}

	private void addRandomRestaurantsToDB(int numRest) {
		String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "k",
				"l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
				"x", "y", "z" };
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
					Time start = new Time((r.nextInt(12 / ranges) + k * 24
							/ ranges + hourOffset) % 24, r.nextInt(59));
					Time stop = new Time(((r.nextInt(12) + 12) / ranges + k
							* 24 / ranges + hourOffset) % 24, r.nextInt(59));
					rh.addRange(day, new Range(start, stop));
				}
			}
			RestaurantMenu menu = new RestaurantMenu();
			int items = r.nextInt(maxMenuItems);
			for (int k = 0; k < items; k++) {
				String name = new String();
				for (int j = 0; j < 14; j++)
					name = name + letters[r.nextInt(letters.length)];
				menu
						.addItem(new RestaurantMenu.MenuItem(name,
								"A wonderful blend of nothing and everything to make something"));
			}
			String name = new String();
			for (int j = 0; j < 7; j++)
				name = name + letters[r.nextInt(letters.length)];
			Restaurant restaurant = new Restaurant(name + " " + i, rh, r
					.nextBoolean()
					&& r.nextBoolean(), r.nextInt(), r.nextInt(), "cafe", menu,
					"Known for its fine cuisine, this is the restaurant Restaurant "
							+ name + " " + i, R.drawable.dining, true, false,
					false, "(615) 555-1234", "http://example.com");
			restaurant.create();
		}
	}

	
}
