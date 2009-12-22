/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Adam Albright
 * @date Oct 16, 2009
 * 
 * Copyright 2009 VUPhone Team
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 *  implied. See the License for the specific language governing 
 *  permissions and limitations under the License. 
 */

package edu.vanderbilt.vuphone.android.campusmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.campusmaps.storage.Building;
import edu.vanderbilt.vuphone.android.campusmaps.storage.DBAdapter;

public class BuildingList extends ListActivity {

	private EditText filterText = null;
	ArrayAdapter<String> adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.buildinglist);

		filterText = (EditText) findViewById(R.building_list.search_box);
		filterText.addTextChangedListener(filterTextWatcher);
		
		DBAdapter adapt = new DBAdapter(this);
        
		String[] from = new String[] { DBAdapter.COLUMN_NAME,
             DBAdapter.COLUMN_ID };
		int[] to = new int[] { android.R.id.text1, R.list_view.buildingID };

		SimpleCursorAdapter sca = new SimpleCursorAdapter(
             getApplicationContext(), R.layout.building_list_item, adapt
                                 .fetchAllBuildingsCursor(), from, to);

		setListAdapter(sca);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		TextView hiddenID = (TextView)v.findViewById(R.list_view.buildingID);
        long b_id = 0;
        b_id = Long.parseLong(hiddenID.getText().toString());
        
        Building b = Building.get(b_id);
        	
		// TODO open a menu that asks what they want to do

		// Drop a pin
		Main.getInstance().drop_pin(b.getLocation(), b_id);

		super.finish();
	}
	
	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str = filterText.getText().toString();
			Log.d("list", "Text is " + str);
			adapter.getFilter().filter(s);
		}
		
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
	}

	/**
	 * Return the list of buildings near coordinates. I
	 * 
	 * @param point
	 *            - measure distance from building to this point (can be null)
	 * @return
	 */
	public ArrayList<String> getBuildingList(GeoPoint point) {
		if (point == null) {
			// Don't measure the distance
		}

		// Extract the building names for now
		ArrayList<String> list = new ArrayList<String>();
		//TODO(adammalbright): fix/finish this method
//		Iterator<Building> i = buildings_.values().iterator();
//		indexToHash_ = new HashMap<Integer, Integer>();
//
//		int index = 0;
//		while (i.hasNext()) {
//			Building b = (Building) i.next();
//			indexToHash_.put(index, b.hashCode());
//			list.add(b.getName());
//			++index;
//		}

		return list;
	}

	/**
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}
}
