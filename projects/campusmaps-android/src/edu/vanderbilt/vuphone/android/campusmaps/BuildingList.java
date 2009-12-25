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

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.campusmaps.storage.Building;
import edu.vanderbilt.vuphone.android.campusmaps.tools.Serializer;
import edu.vanderbilt.vuphone.android.campusmaps.tools.Tools;
import edu.vanderbilt.vuphone.android.campusmaps.tools.XMLTools;

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
	 * Parses in the building data to populate BuildingList
	 */
	public static void populateBuildings(InputStream xmlData,
			InputStream buildingCache) {
		// Prevent the building list from being populated each time onCreate is
		// called

		if (getBuildingList().size() != 0)
			return;

		if (isNewListAvailable() || buildingCache == null) {
			Main.trace("Parsing building list from XML");
			loadFromXML(xmlData);

			// Cache the building list
			updateCacheFile();
		}

		else {
			Main.trace("Loading building list from CACHE");
			loadFromCache(buildingCache);
		}

		Log.i("mad", "Populated " + buildings_.size() + " entries");
	}

	private static boolean isNewListAvailable() {
		// TODO check server for updated building list

		return true;
	}

	private static void updateCacheFile() {
		try {
			FileOutputStream fos = Main.getInstance().openFileOutput(
					"buildingList.cache", MODE_WORLD_READABLE);

			OutputStreamWriter osw = new OutputStreamWriter(fos);

			new Serializer().saveObject(buildings_, osw);
		} catch (Exception e) {
			Main.trace("Unable to update the building list cache");
			e.printStackTrace();
			return;
		}

		Main.trace("Building list cache has been updated");
	}

	/**
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}

	private static void loadFromXML(InputStream xmlData) {
		Map<Long, Building> bList = BuildingList.getBuildingList();

		Document doc = XMLTools.parseXML(xmlData);

		if (doc == null)
			return;

		int i;
		NodeList list_ = doc.getElementsByTagName("feature");
		for (i = 0; i < list_.getLength(); i++) {
			Properties attrib = XMLTools.NodeList2Array(list_.item(i)
					.getChildNodes());

			if (attrib == null)
				continue;

			String name = Tools.titleCase(attrib.getProperty("FACILITY_NAME"));

			if (!attrib.containsKey("coordinates"))
				continue;

			String loc[] = attrib.getProperty("coordinates").split(" ");
			String latlong[] = loc[0].split(",");
			GeoPoint gp = Tools.EPSG900913ToGeoPoint(Double
					.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
			String url = "http://www.vanderbilt.edu/map/"
					+ attrib.getProperty("FACILITY_URL").toLowerCase();

			Building b = new Building(i, gp, name, attrib
					.getProperty("FACILITY_REMARKS"), url);

			bList.put(new Long(i), b);
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadFromCache(InputStream is) {
		try {
			int avail = is.available();
			if (avail <= 0)
				return;

			byte file[] = new byte[avail];
			is.read(file);

			String str = new String(file);

			Object o = new Serializer().fromXML(str);

			buildings_ = (Map<Long, Building>) o;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
