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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import android.widget.Toast;
import edu.vanderbilt.vuphone.android.campusmaps.storage.Building;

public class BuildingList extends ListActivity {

	private EditText filterText = null;
	SimpleCursorAdapter simpleCursorAdapter = null;
	ArrayAdapter<Building> dataAdapter = null;
	private static Map<Long, Building> buildings_ = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.buildinglist);

		filterText = (EditText) findViewById(R.building_list.search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		List<Building> bList = new ArrayList<Building>(buildings_.values());

		if (bList.size() == 0) {
			echo("The building list is still being populated. Please wait...");
			finish();
			return;
		}

		// Alphabetize
		Collections.sort(bList);

		dataAdapter = new ArrayAdapter<Building>(this,
				android.R.layout.simple_list_item_1, bList);

		setListAdapter(dataAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Building bc = (Building) getListView().getItemAtPosition(position);
		Main.trace(bc.getName() + " selected");

		// TODO open a menu that asks what they want to do

		// Drop a pin
		Main.getInstance().drop_pin(bc);

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
			dataAdapter.getFilter().filter(s);
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
	}

	/**
	 * Returns a list of building names
	 * 
	 * @return
	 */
	public List<String> getBuildingNames() {
		List<String> list = new ArrayList<String>();

		Iterator<Building> iter = buildings_.values().iterator();
		while (iter.hasNext()) {
			list.add(iter.next().getName());
		}

		return list;
	}

	/**
	 * Provides access to the database of buildings
	 * 
	 * @return
	 */
	public static Map<Long, Building> getBuildingList() {
		if (buildings_ == null)
			buildings_ = new HashMap<Long, Building>();

		return buildings_;
	}

	public static Building getBuilding(long buildingID) {
		return buildings_.get(new Long(buildingID));
	}

	/**
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}
}
