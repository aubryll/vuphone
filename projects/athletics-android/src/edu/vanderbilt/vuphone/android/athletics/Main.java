package edu.vanderbilt.vuphone.android.athletics;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends ListActivity {

	static final String[] SPORTS = new String[] { "Football" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, SPORTS));
		getListView().setTextFilterEnabled(true);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// starts restaurant details page and sends index of restaurant
		startActivity(new Intent(this, TeamMain.class));

	}

	// -------------------- MENU FUNCTIONS

	private static final int MENU_ITEM_ABOUT = 0;

	/** Creates list of actions for user when the menu button is clicked */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_ITEM_ABOUT, Menu.NONE, "About").setIcon(
				getResources().getDrawable(
						android.R.drawable.ic_menu_info_details));

		return true;
	}
	
	/** Handles what happens when each menu item is clicked */
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_ITEM_ABOUT:
			startActivity(new Intent(this, About.class));
			break;
		}
		return true;
	}
}