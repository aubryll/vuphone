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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

public class BuildingList extends ListActivity {

	// List of buildings -- should get populated at boot
	private HashMap<Integer, Building> buildings_ = null;
	private HashMap<Integer, Integer> indexToHash_ = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		buildings_ = SharedData.getInstance().getBuildingList();
		if (buildings_ == null || buildings_.size() < 1) {
			echo("Building list not found!");
		} else {
			// TODO Create custom adapter so we can show more than just the
			// building name
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, getBuildingList(null)));
		}

		Log.d("mad", "Buildings loaded: " + buildings_.size());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// Get the clicked building
		Building b = buildings_.get(indexToHash_.get(position));

		// TODO open a menu that asks what they want to do

		// Drop a pin
		Main.getInstance().drop_pin(b.getLocation(), b);

		super.finish();
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
		Iterator<Building> i = buildings_.values().iterator();
		indexToHash_ = new HashMap<Integer, Integer>();

		int index = 0;
		while (i.hasNext()) {
			Building b = (Building) i.next();
			indexToHash_.put(index, b.hashCode());
			list.add(b.getName());
			++index;
		}

		return list;
	}

	/**
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}
}
