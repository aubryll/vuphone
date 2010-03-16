package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/*
 * Athletics News class will start the Activity listing current 
 * news articles for the selected sport and are pulled from the 
 * Vanderbilt Athletics website.
 * This data will be retrieved by an XML parser.
 * 
 * @author Grayson Sharpe
 */
public class AthleticsNews extends ListActivity {
	ArrayAdapter<String> adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter news;
	private static final int ADD_ITEM_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_layout); // allows for custom list

		/*
		 * String title is assigned the value of the item (name of the sport
		 * information) that the user clicked on in the previous activity plus
		 * the additional Extra of the name of the sport. String title cannot go
		 * before onCreate is called because of a problem with pending Intent.
		 */
		// TODO Readdress implementation when database is implemented on all
		// pages
		String title = getIntent().getExtras().getString("sports_title");
		setTitle("Vanderbilt " + title + " News"); // sets Title bar

		/*
		 * SimpleAdapter is in place for the static data for the view. The view
		 * will later be populated with information from database.
		 * 
		 * Make sure that the layout file in the adapter is the custom cell file
		 * and not the custom listview file
		 */
		// TODO Implement Database
		news = new SimpleAdapter(this, list, R.layout.news_cell, new String[] {
				"news_title", "news_details", "news_date" }, new int[] {
				R.news.title, R.news.details, R.news.date });
		setListAdapter(news);
		this.addItem(); // Populates ListView
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startActivity(new Intent(
				Intent.ACTION_VIEW,
				Uri
						.parse("http://vucommodores.cstv.com/sports/m-baskbl/spec-rel/031510aaj.html")));
	}

	// TODO Remove menu button when database is implemented
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) Adds
	 * menu button "Add Item"
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_ITEM_ID, Menu.NONE, R.string.add_item);
		return result;
	}

	// TODO Remove menu function when database is implemented
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 * When clicked data already in the list is readded
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ADD_ITEM_ID:
			addItem();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// TODO Remove static data when database is implemented
	/*
	 * Add static data here in the hash map
	 */
	private void addItem() {
		String[] title = new String[] {
				"Taylor breaks out at the right time. Enjoy the free tacoes. They are awesome.",
				"Vanderbilt-Tennessee postgame quotes",
				"Vanderbilt hosts Tennessee Tuesday" };
		String[] details = new String[] {
				"Taylor breaks out at the right time. Enjoy the free tacoes. They are awesome.",
				"Vanderbilt-Tennessee postgame quotes",
				"Vanderbilt hosts Tennessee Tuesday" };
		String[] date = new String[] { "September 25, 2010",
				"September 23, 2010", "September 21, 2010" };

		for (int i = 0; i < 3; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("news_title", title[i]);
			item.put("news_details", details[i]);
			item.put("news_date", date[i]);

			list.add(item);
			news.notifyDataSetChanged();
		}
	}

}
