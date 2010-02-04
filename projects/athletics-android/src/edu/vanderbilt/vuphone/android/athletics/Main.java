package edu.vanderbilt.vuphone.android.athletics;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class Main extends ListActivity {
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  setListAdapter(new ArrayAdapter<String>(this,
	          android.R.layout.simple_list_item_1, SPORTS));
	  getListView().setTextFilterEnabled(true);
	}
	
	static final String[] SPORTS = new String[] {"Football"};
}