package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SportsMain extends ListActivity {
	
	ArrayAdapter<String> adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter main;
	private static final int ADD_ITEM_ID = 1;

	static final String[] SPORTS = new String[] { "Football" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		//setListAdapter(new ArrayAdapter<String>(this,
		//		android.R.layout.simple_list_item_1, SPORTS));
		
		main = new SimpleAdapter(this, list, R.layout.main_cell, new String[] {
				"sports_title" }, new int[] { R.main.text});
		setListAdapter(main);
		this.addItem();
				
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		startActivity(new Intent(this, TeamMain.class));

	}

	private void addItem() {
		String[] title = new String[] { "Football", "Basketball", "Baseball", "Track", "Soccer", "fda", "as", "fds", "fsd"};

		for (int i = 0; i < 9; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("sports_title", title[i]);

			list.add(item);
			main.notifyDataSetChanged();
		}
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