package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import edu.vanderbilt.vuphone.android.objects.Restaurant;

/**
 * Displays list of restaurants and lets user select new favorites
 * 
 * @author Peter
 */
public class MarkFavs extends ListActivity {

	/** Indicates what dialog is displayed when showDialog is called */
	public static final String EXTRA_FAVORITES = "favorites";
	/**
	 * The position in list restaurants and restaurantIDs of the restaurants
	 * that are currently marked as favorites
	 */
	private ArrayList<Long> restaurantIDs;
	private String[] restaurants;

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		restaurantIDs = Restaurant.getIDs();
		restaurants = new String[restaurantIDs.size()];

		for (int x = 0; x < restaurantIDs.size(); ++x) {
			restaurants[x] = Restaurant.getName(restaurantIDs.get(x));
		}

		setContentView(R.layout.mark_favs);

		// This section creates the buttons at the bottom of the list.
		((Button) findViewById(R.mark_favs.done))
				.setOnClickListener(doneListener);
		((Button) findViewById(R.mark_favs.cancel))
				.setOnClickListener(cancelListener);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, restaurants));
		getListView().setTextFilterEnabled(true);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		for (int x = 0; x < restaurantIDs.size(); ++x) {
			if (Restaurant.favorite(restaurantIDs.get(x))) {
				getListView().setItemChecked(x, true);
			}
		}

	}

	/** updates the favorites variable for each location */
	private void setNewFavorites() {
		SparseBooleanArray allPositions = getListView()
				.getCheckedItemPositions();
		for (int x = 0; x < restaurants.length; ++x) {
			if (allPositions.get(x)) {
				Restaurant.get(restaurantIDs.get(x)).setFavorite(true);
			} else {
				Restaurant.get(restaurantIDs.get(x)).setFavorite(false);
			}
		}
	}

	/** ends activity and updates favorites when done button is clicked */
	private OnClickListener doneListener = new OnClickListener() {

		public void onClick(View v) {
			setNewFavorites();
			finish();
		}
	};

	/** ends activity when cancel button is clicked */
	private OnClickListener cancelListener = new OnClickListener() {

		public void onClick(View v) {
			finish();
		}
	};
}
