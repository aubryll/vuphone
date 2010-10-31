package edu.vanderbilt.vuphone.android.dining;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import edu.vanderbilt.vuphone.android.objects.RestaurantAdapter;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// clunky mechanic with gross file dependency,
		// but DBWrapper needs Main.mainContext for its
		// calls to DBAdapter
		if (applicationContext == null)
			applicationContext = getApplicationContext();
		display24 = "24".equals(System.getString(this.getContentResolver(),
				System.TIME_12_24));

		//Restaurant.deleteAll();
		if (Restaurant.getIDs().size() != StaticRestaurantData.NUM_RESTAURANTS) {
			Log.i("Dining", "database purged: getIDs().size()="
					+ Restaurant.getIDs().size() + ", Static data size="
					+ StaticRestaurantData.NUM_RESTAURANTS);
			Restaurant.deleteAll();
			(new StaticRestaurantData()).createAllRestaurants();
		}

		initializeContentView();

		ra = new RestaurantAdapter(this, RestaurantAdapter.SORT_UNSORTED); 
			// this does the least work because retrieveUserSettings() now sorts the list);
		
		retrieveUserSettings();

		setListAdapter(ra);
		// getListView().setTextFilterEnabled(true);
		// RestaurantAdapter must implement Filterable for this to work
		// getListView().setFastScrollEnabled(true);
		// dont know if this is appropriate

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		commitUserSettings();
	}
	

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (id < 0) // if user clicked on a partition
			return;
		switch (mode) {
		case NORMAL:
			// starts restaurant details page and sends index of restaurant
			startActivity(new Intent(this, RestaurantDetails.class).putExtra(RestaurantDetails.RESTAURANT_ID, id));
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
	private static final int MENU_ITEM_ABOUT = 4;

	private View doneButton;
	private View cancelButton;

	// saved showFavIcon preference to reset to when switching back to normal
	boolean showFavIcon;

	/** Creates list of actions for user when the menu button is clicked */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_ITEM_VIEW_SETTINGS, Menu.NONE, "Settings")
				.setIcon(
						getResources().getDrawable(
								android.R.drawable.ic_menu_preferences));
		menu.add(Menu.NONE, MENU_ITEM_CHOOSE_SORTING, Menu.NONE, "Sort")
				.setIcon(
						getResources().getDrawable(
								android.R.drawable.ic_menu_agenda));
		menu.add(Menu.NONE, MENU_ITEM_MARK_FAVS, Menu.NONE, "Mark Favorites")
				.setIcon(getResources().getDrawable(R.drawable.ic_menu_star));
		menu.add(Menu.NONE, MENU_ITEM_ABOUT, Menu.NONE, "About").setIcon(
				getResources().getDrawable(
						android.R.drawable.ic_menu_info_details));
		menu.add(Menu.NONE, MENU_ITEM_VIEW_MAP, Menu.NONE, "View Map").setIcon(
				getResources().getDrawable(android.R.drawable.ic_menu_mapmode));

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
		case MENU_ITEM_ABOUT:
			Intent about = new Intent(this, About.class);
			startActivity(about);
			return true;
		}
		return true;
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

	private static final boolean[] SORT_OPTION_DEFAULTS = {true, true, false, false}; // {favorite,  
	private final boolean[] checkedSort = new boolean[SORT_OPTION_DEFAULTS.length];   // open,       
																		              // time till   
																		              // close,      
																		              // near};      
																		                             
	private static final int SETTINGS_NUM = 6;
	private final boolean[] checkedSetting = new boolean[SETTINGS_NUM]; // needed for inner classes
	private boolean settingsModified = false;
	private boolean sortSettingsModified = false;
	private boolean reSortNeeded = false;

	/**
	 * This opens the dialog that allows the user to choose a new sorting option
	 */
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		switch (id) {
		case DIALOG_SORT: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			CharSequence[] items = { "Favorite", "Open", "Time until close",
					"Near" };

			builder.setMultiChoiceItems(items, checkedSort,
					new DialogInterface.OnMultiChoiceClickListener() {

						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							checkedSort[which] = isChecked;
							((AlertDialog) dialog).getListView()
									.setItemChecked(which, isChecked);

							if (which == 1 && !isChecked && checkedSort[2]) {
								onClick(dialog, 2, false);
							}
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

						public void onClick(DialogInterface dialog, int which) {
							ra.setSort(checkedSort[0], checkedSort[1],
									checkedSort[2], checkedSort[3],
									settingsModified, sortSettingsModified);
							ra.notifyDataSetChanged();
							dialog.dismiss();
						}

					});

			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			builder.setTitle("Sort by");

			return builder.create();
		}
		case DIALOG_SETTINGS:
		default: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			CharSequence[] items = { "Show favorites icon",
					"Gray closed places", "Show distance", "Show place type",
					"Hide off campus", "Hide off the card" };
			
			boolean[] checked = { ra.getShowFavIcon(),
					ra.getGrayClosed(), ra.getShowDistances(),
					ra.getShowRestaurantType(), ra.getHideOffCampus(),
					ra.getHideOffTheCard() };
			for (int i = 0; i < SETTINGS_NUM; i++)
				checkedSetting[i] = checked[i];

			builder.setMultiChoiceItems(items, checkedSetting,
					new DialogInterface.OnMultiChoiceClickListener() {

						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							checkedSetting[which] = isChecked;
							((AlertDialog) dialog).getListView()
									.setItemChecked(which, isChecked);

							settingsModified = true;

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
										ra.setShowDistances(true);
									} else {
										onClick(dialog, 2, false);
									}
								else
									ra.setShowDistances(false);
								break;
							case 3:
								ra.setShowRestaurantType(isChecked);
								break;
							case 4:
								ra.setHideOffCampus(isChecked);
								reSortNeeded = true;
								break;
							case 5:
								ra.setHideOffTheCard(isChecked);
								reSortNeeded = true;
								break;
							}

						}
					});

			builder.setNeutralButton("Done",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							if (reSortNeeded) {
								ra.setSort();
								reSortNeeded = false;
							}
							ra.notifyDataSetChanged();
							dialog.dismiss();
						}
					});

			builder.setNegativeButton("Set Defaults",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							ra.setAllBoolsToDefault();

							settingsModified = false;
							sortSettingsModified = false;

							if (reSortNeeded) {
								ra.setSort();
								reSortNeeded = false;
							}
							ra.notifyDataSetChanged();
							dialog.dismiss();
						}
					});

			builder.setTitle("Settings");

			return builder.create();

		}
		}
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DIALOG_SORT: {
			boolean[] checked = { ra.indexOf(RestaurantAdapter.SORT_FAVORITE) != -1,
					ra.indexOf(RestaurantAdapter.SORT_OPEN_CLOSED) != -1,
					ra.indexOf(RestaurantAdapter.SORT_TIME_TO_CLOSE) != -1,
					ra.indexOf(RestaurantAdapter.SORT_NEAR_FAR) != -1 };
			for (int i = 0; i < checked.length; i++) {
				checkedSort[i] = checked[i];
				((AlertDialog) dialog).getListView().setItemChecked(i,
						checked[i]);
			}
			break;
		}
		case DIALOG_SETTINGS: {
			boolean checked[] = { ra.getShowFavIcon(), ra.getGrayClosed(),
					ra.getShowDistances(), ra.getShowRestaurantType(),
					ra.getHideOffCampus(), ra.getHideOffTheCard() };
			for (int i = 0; i < checked.length; i++) {
				checkedSetting[i] = checked[i];
				((AlertDialog) dialog).getListView().setItemChecked(i,
						checked[i]);
			}
			break;
		}
		}
	}

	private boolean getLocationWithUI() {
		// Toast trying = Toast.makeText(this,
		// "Trying to determine your location..." , Toast.LENGTH_SHORT);
		// trying.show();
		// TODO show a waiting for device type message
		if (!ra.refreshDistances()) {
			// trying.cancel();
			Toast.makeText(this, "Your location is temporarily unavailable",
					Toast.LENGTH_SHORT).show();
			// TODO make these strings part of resource data
			return false;
		} else {
			// trying.cancel();
			return true;
		}
	}
	
	// SAVING PREFERENCES ACCROSS INSTANCES
	
	private SharedPreferences mprefs;
	private static final String PREFERENCES = "preferences";
	
	// persistent user settings
	private static final String PREF_SETTINGS_MODIFIED = "setMod";
	private static final String PREF_SORT_SETTINGS_MODIFIED = "sortSetMod";
	private static final String PREF_FAV_ICON = "favIcon";
	private static final String PREF_GRAY_CLOSED = "grayClosed";
	private static final String PREF_SHOW_DISTANCE = "showDist";
	private static final String PREF_SHOW_PLACE_TYPE = "placeType";
	private static final String PREF_HIDE_OFF_CAMPUS = "hideOffCampus";
	private static final String PREF_HIDE_OFF_CARD = "hideOffCard";
	
	// persistent sort type
	private static final String PREF_FAVORITE = "favorite";
	private static final String PREF_OPEN = "open";
	private static final String PREF_TIME_TILL_CLOSE = "timeClose";
	private static final String PREF_NEAR = "near";
	
	
	private void retrieveUserSettings() {
		mprefs = getSharedPreferences(PREFERENCES, ContextWrapper.MODE_PRIVATE);
		
		settingsModified = mprefs.getBoolean(PREF_SETTINGS_MODIFIED, false);
		sortSettingsModified = mprefs.getBoolean(PREF_SORT_SETTINGS_MODIFIED, false);
		checkedSort[0] = mprefs.getBoolean(PREF_FAVORITE, SORT_OPTION_DEFAULTS[0]);
		checkedSort[1] = mprefs.getBoolean(PREF_OPEN, SORT_OPTION_DEFAULTS[1]);
		checkedSort[2] = mprefs.getBoolean(PREF_TIME_TILL_CLOSE, SORT_OPTION_DEFAULTS[2]);
		checkedSort[3] = mprefs.getBoolean(PREF_NEAR, SORT_OPTION_DEFAULTS[3]);
		ra.setSort(checkedSort[0], checkedSort[1], checkedSort[2], checkedSort[3], settingsModified, sortSettingsModified);
		ra.notifyDataSetChanged();
		
		ra.setShowFavIcon(mprefs.getBoolean(PREF_FAV_ICON, ra.getShowFavIcon()));
		ra.setGrayClosed(mprefs.getBoolean(PREF_GRAY_CLOSED, ra.getGrayClosed()));
		ra.setShowDistances(mprefs.getBoolean(PREF_SHOW_DISTANCE, ra.getShowDistances()));
		ra.setShowRestaurantType(mprefs.getBoolean(PREF_SHOW_PLACE_TYPE, ra.getShowRestaurantType()));
		ra.setHideOffCampus(mprefs.getBoolean(PREF_HIDE_OFF_CAMPUS, ra.getHideOffCampus()));
		ra.setHideOffTheCard(mprefs.getBoolean(PREF_HIDE_OFF_CARD, ra.getHideOffTheCard()));
		
	}
	
	private void commitUserSettings() {
		SharedPreferences.Editor ed = mprefs.edit();
		
		ed.putBoolean(PREF_SETTINGS_MODIFIED, settingsModified);
		ed.putBoolean(PREF_SORT_SETTINGS_MODIFIED, sortSettingsModified);
		ed.putBoolean(PREF_FAVORITE, checkedSort[0]);
		ed.putBoolean(PREF_OPEN, checkedSort[1]);
		ed.putBoolean(PREF_TIME_TILL_CLOSE, checkedSort[2]);
		ed.putBoolean(PREF_NEAR, checkedSort[3]);

		
		ed.putBoolean(PREF_FAV_ICON, ra.getShowFavIcon());
		ed.putBoolean(PREF_GRAY_CLOSED, ra.getGrayClosed());
		ed.putBoolean(PREF_SHOW_DISTANCE, ra.getShowDistances());
		ed.putBoolean(PREF_SHOW_PLACE_TYPE, ra.getShowRestaurantType());
		ed.putBoolean(PREF_HIDE_OFF_CAMPUS, ra.getHideOffCampus());
		ed.putBoolean(PREF_HIDE_OFF_CARD, ra.getHideOffTheCard());
		
		ed.commit();
	}

	// PLACEHOLDER / TEMOPRARY METHODS BELOW

	// private void addRandomRestaurantsToDB(int numRest) {
	// String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "k",
	// "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
	// "x", "y", "z" };
	// int maxRanges = 2;
	// int maxMenuItems = 10;
	// int hourOffset = 5;
	// Log.i("test", "loading database with valid random data");
	// Random r = new Random();
	// for (int i = 1; i <= numRest; i++) {
	// RestaurantHours rh = new RestaurantHours();
	// for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
	// int ranges = r.nextInt(maxRanges) + 1;
	// for (int k = 0; k < ranges; k++) {
	// Time start = new Time((r.nextInt(12 / ranges) + k * 24
	// / ranges + hourOffset) % 24, r.nextInt(59));
	// Time stop = new Time(((r.nextInt(12) + 12) / ranges + k
	// * 24 / ranges + hourOffset) % 24, r.nextInt(59));
	// rh.addRange(day, new Range(start, stop));
	// }
	// }
	// RestaurantMenu menu = new RestaurantMenu();
	// int items = r.nextInt(maxMenuItems);
	// for (int k = 0; k < items; k++) {
	// String name = new String();
	// for (int j = 0; j < 14; j++)
	// name = name + letters[r.nextInt(letters.length)];
	// menu
	// .addItem(new RestaurantMenu.MenuItem(name,
	// "A wonderful blend of nothing and everything to make something"));
	// }
	// String name = new String();
	// for (int j = 0; j < 7; j++)
	// name = name + letters[r.nextInt(letters.length)];
	// Restaurant restaurant = new Restaurant(name + " " + i, rh, r
	// .nextBoolean()
	// && r.nextBoolean(), r.nextInt(), r.nextInt(), "cafe", menu,
	// "Known for its fine cuisine, this is the restaurant Restaurant "
	// + name + " " + i, R.drawable.dining, true, false,
	// false, "(615) 555-1234", "http://example.com");
	// restaurant.create();
	// }
	// }

}
