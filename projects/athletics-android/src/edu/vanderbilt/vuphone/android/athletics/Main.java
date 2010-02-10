package edu.vanderbilt.vuphone.android.athletics;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends ListActivity {
	
	static final String[] SPORTS = new String[] {"Football"};
	
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
}