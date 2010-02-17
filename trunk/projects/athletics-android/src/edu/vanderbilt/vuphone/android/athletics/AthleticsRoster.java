package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class AthleticsRoster extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ArrayList<String> roster = new ArrayList<String>();
		roster.add("Larry");
		roster.add("Adam");

		setListAdapter(new ArrayAdapter<String>(this,
			android.R.layout.simple_list_item_1, roster));
	}

}

