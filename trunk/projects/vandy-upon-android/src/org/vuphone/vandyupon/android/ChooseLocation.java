package org.vuphone.vandyupon.android;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

/**
 * Used by the SubmitEvent class to allow the user to choose a location on
 * campus.
 * 
 * @author Hamilton Turner
 * 
 */
public class ChooseLocation extends ExpandableListActivity {
	private static final String LOCATION_CATEGORY = "lc";
	private static final String LOCATION = "l";

	/** The top-level categories */
	static final String locationCategories[] = { "Engineering Buildings",
			"A&S Buildings", "Lawns", "Restaurants near campus", "Other" };

	/** The items in each category */
	static final String locations[][] = { { "Featheringill", "Stevenson" },
			{ "Garland", "Calhoun" }, { "Alumni", "Wilson" },
			{ "Qudoba", "Taco Bell", "Cafe Coco", "Mellow Mushroom" },
			{ "Other" } };

	/** Controls the action associated with selecting an item */
	private ExpandableListView.OnChildClickListener childClickListener_ = new ExpandableListView.OnChildClickListener() {
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			if (locations[groupPosition][childPosition].equals("Other"))
				setResult(SubmitEvent.RESULT_UNKNOWN);
			else
				setResult(SubmitEvent.RESULT_OK, (new Intent()).putExtra(
						SubmitEvent.RESULT,
						locations[groupPosition][childPosition]));
			finish();
			return true;
		}
	};

	/**
	 * Converts the array locations[][] into the expandable list data structure
	 * List<List<Map<String, ?>>>. The first list is the group index, the second
	 * list is the child index, the map contains all elements needed to show the
	 * child list item, in a Key / Value relationship
	 */
	private static ArrayList<ArrayList<HashMap<String, String>>> createChildList() {
		// Iterate over the groups
		ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
		for (int i = 0; i < locations.length; ++i) {

			// Create lists that represent the items
			ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
			for (int n = 0; n < locations[i].length; ++n) {
				HashMap<String, String> child = new HashMap<String, String>();
				child.put(LOCATION, locations[i][n]);
				secList.add(child);
			}
			result.add(secList);
		}
		return result;
	}

	/**
	 * Converts the array locationCategories[] into the expandable list data
	 * structure List<Map<String, ?>.The list is the group index, and the map
	 * contains all elements needed to show the group list item, in a Key /
	 * Value relationship
	 */
	private static ArrayList<HashMap<String, String>> createGroupList() {
		// Create a list representing the groups
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < locationCategories.length; ++i) {
			HashMap<String, String> m = new HashMap<String, String>();
			m.put(LOCATION_CATEGORY, locationCategories[i]);
			result.add(m);
		}
		return result;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we die, or otherwise quit, the result should be canceled
		setResult(SubmitEvent.RESULT_CANCELED);

		// Set up our adapter
		ExpandableListAdapter adapter = new SimpleExpandableListAdapter(this,
				createGroupList(),
				android.R.layout.simple_expandable_list_item_1,
				new String[] { LOCATION_CATEGORY },
				new int[] { android.R.id.text1 }, createChildList(),
				android.R.layout.simple_expandable_list_item_1,
				new String[] { LOCATION }, new int[] { android.R.id.text1 });

		getExpandableListView().setOnChildClickListener(childClickListener_);

		setListAdapter(adapter);
	}
}
