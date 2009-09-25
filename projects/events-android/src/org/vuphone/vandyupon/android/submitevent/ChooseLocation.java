package org.vuphone.vandyupon.android.submitevent;

import java.util.ArrayList;
import java.util.HashMap;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.LocationManager;

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
	/** Used for logging */
	private static final String LOCATION_CATEGORY = "lc";
	private static final String LOCATION = "l";

	/** Used to let other classes access the data returned */
	public static final String RESULT_NAME = "r";

	private static final String[][] locations_ = LocationManager.locations;
	private static final String[] locationCategories_ = LocationManager.groups;

	/** Controls the action associated with selecting an item */
	private ExpandableListView.OnChildClickListener childClickListener_ = new ExpandableListView.OnChildClickListener() {
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			if (locations_[groupPosition][childPosition].equals("Other"))
				setResult(Constants.RESULT_UNKNOWN);
			else {
				Intent result = new Intent();
				result.putExtra(RESULT_NAME,
						locations_[groupPosition][childPosition]);
				setResult(Constants.RESULT_OK, result);
			}
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
		for (int i = 0; i < locations_.length; ++i) {

			// Create lists that represent the items
			ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
			for (int n = 0; n < locations_[i].length; ++n) {
				HashMap<String, String> child = new HashMap<String, String>();
				child.put(LOCATION, locations_[i][n]);
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
		for (int i = 0; i < locationCategories_.length; ++i) {
			HashMap<String, String> m = new HashMap<String, String>();
			m.put(LOCATION_CATEGORY, locationCategories_[i]);
			result.add(m);
		}
		return result;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// If we die, or otherwise quit, the result should be canceled
		setResult(Constants.RESULT_CANCELED);

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
