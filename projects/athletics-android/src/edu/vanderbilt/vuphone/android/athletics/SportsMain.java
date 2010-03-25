package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/*
 * SportsMain when launched displays a list of
 * Vanderbilt Sports.
 * 
 * @author Grayson Sharpe
 */

public class SportsMain extends ListActivity {

	ArrayAdapter<String> adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter main;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		setTitle("Vanderbilt Sports");
		
		// setListAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, SPORTS));

		/*
		 * SimpleAdapter is in place for the static data for the view.
		 * The view will later be populated with information from
		 * database.
		 * 
		 * Make sure that the layout file in the adapter is the 
		 * custom cell file and not the custom listview file
		 * 
		 * May populate from database.
		 */
		main = new SimpleAdapter(this, list, R.layout.main_cell,
				new String[] { "sports_title" }, new int[] { R.main.text });
		setListAdapter(main);
		this.addItem(); //Populates the list

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		/*
		 * Launches activity TeamMain for the selected sport and puts the
		 * Extra of the sport's name on the intent.
		 */
		startActivity(new Intent(this, TeamMain.class).putExtra("sports_title",
				list.get(position).get("sports_title")));
		
	}
	
	
	/*
	 * Static data in a hashmap for the list
	 */
	private void addItem() {
		String[] title = new String[] { "Football", "Men's Basketball",
				"Women's Basketball", "Baseball", "Men's Cross Country",
				"Women's Cross Country", "Men's Golf", "Women's Golf",
				"Men's Tennis", "Women's Tennis", "Bowling", "Lacrosse",
				"Swimming", "Soccer", "Track & Field" };

		for (int i = 0; i < 15; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("sports_title", title[i]);

			list.add(item);
			main.notifyDataSetChanged();
		}
	}
}