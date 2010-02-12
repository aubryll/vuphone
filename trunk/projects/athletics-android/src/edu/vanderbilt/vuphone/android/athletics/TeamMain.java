package edu.vanderbilt.vuphone.android.athletics;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class TeamMain extends ListActivity {
	
	static final String[] SPORTS = new String[] {"Team Main"};

@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  setListAdapter(new ArrayAdapter<String>(this,
	          android.R.layout.simple_list_item_1, SPORTS));
	  getListView().setTextFilterEnabled(true);
	}

//-------------------- MENU FUNCTIONS

private static final int MENU_ITEM_NEWS = 0;
private static final int MENU_ITEM_SCHEDULE = 1;

/** Creates list of actions for user when the menu button is clicked */
public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	menu.add(Menu.NONE, MENU_ITEM_SCHEDULE, Menu.NONE, "Schedule").setIcon(
			getResources().getDrawable(
					android.R.drawable.ic_menu_my_calendar));
	menu.add(Menu.NONE, MENU_ITEM_NEWS, Menu.NONE, "News").setIcon(
			getResources().getDrawable(
					android.R.drawable.ic_menu_recent_history));
	

	return true;
}

/** Handles what happens when each menu item is clicked */
public boolean onOptionsItemSelected(MenuItem item) {
	super.onOptionsItemSelected(item);
	switch (item.getItemId()) {
	case MENU_ITEM_NEWS:
		Intent news = new Intent(this, AthleticsNews.class);
		startActivity(news);
		return true;
	case MENU_ITEM_SCHEDULE:
		Intent schedule = new Intent(this, AthleticsSchedule.class);
		startActivity(schedule);
		return true;
	
	}
	return true;
}

}