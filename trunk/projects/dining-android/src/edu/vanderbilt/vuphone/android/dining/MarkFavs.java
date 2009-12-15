package edu.vanderbilt.vuphone.android.dining;


import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import edu.vanderbilt.vuphone.android.objects.DBWrapper;
import edu.vanderbilt.vuphone.android.objects.Restaurant;
import edu.vanderbilt.vuphone.android.objects.RestaurantAdapter;

/**
 * This class uses the same custom list view as the main screen but allows the
 * user to choose their favorite locations
 * 
 * @author Peter
 * 
 */
public class MarkFavs extends ListActivity {

	public static final String ADAPTER = "adapter";
	private long[] listOrder;

	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);

		setContentView(R.layout.mark_favs);

		// This section creates the buttons at the bottom of the list.
		((Button) findViewById(R.mark_favs.done))
				.setOnClickListener(doneListener);
		((Button) findViewById(R.mark_favs.cancel))
				.setOnClickListener(cancelListener);

		Bundle extras = getIntent().getExtras();
		listOrder = extras.getLongArray(ADAPTER);

		createList();
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (id < 0)
			return;
		
		// toggles the favorite in memory and in the database
		Restaurant.setFavorite(id, !Restaurant.favorite(id));
		
		createList();
	}

	/** Creates the list that is displayed */
	private void createList() {

		ArrayList<Long> order = new ArrayList<Long>();

		for (int x = 0; x < listOrder.length; ++x) {
			order.add(listOrder[x]);
		}

		RestaurantAdapter ra = new RestaurantAdapter(this, order, true);
		setListAdapter(ra);

		getListView().setTextFilterEnabled(true);
	}

	/** ends activity and updates favorites when done button is clicked */
	private OnClickListener doneListener = new OnClickListener() {

		public void onClick(View v) {
			DBWrapper.commit();
			finish();
		}
	};

	/** ends activity when cancel button is clicked */
	private OnClickListener cancelListener = new OnClickListener() {

		public void onClick(View v) {
			DBWrapper.revert();
			finish();
		}
	};

}
