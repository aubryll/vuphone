package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import edu.vanderbilt.vuphone.android.athletics.storage.DatabaseAdapter;

/*
 * Athletics Schedule class will start the Activity listing the
 * schedule of upcoming games and the scores of previous games
 * for the selected sport and are pulled from the 
 * Vanderbilt Athletics website.
 * This data will be retrieved by an XML parser.
 * 
 * @author Grayson Sharpe
 */

public class AthleticsSchedule extends ListActivity {

	ArrayAdapter<String> adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter schedule;
	private static final int ADD_ITEM_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_layout); // allows for custom list

		/*
		 * String title is assigned the value of the item (name of the sport
		 * information) that the user clicked on in the previous activity plus
		 * the additional Extra of the name of the sport. String title cannot go
		 * before onCreate is called because of a problem with pending Intent.
		 */
		String title = getIntent().getExtras().getString("sports_title");
		setTitle("Vanderbilt " + title + " Schedule");

		DatabaseAdapter db = new DatabaseAdapter(this);
		db.open();
		
		Cursor cursor = db.fetchAllGames();

		String[] columns = { "hometeam", "homescore", "awayscore", "awayteam",
				"time" };
		int[] column_ids = { R.schedule.home_team, R.schedule.home_score,
				R.schedule.away_score, R.schedule.away_team, R.schedule.game_date };
		setListAdapter(new SimpleCursorAdapter(this, R.layout.schedule_cell,
				cursor, columns, column_ids));
		db.close();

		/*
		 * SimpleAdapter is in place for the static data for the view. The view
		 * will later be populated with information from database.
		 * 
		 * Make sure that the layout file in the adapter is the custom cell file
		 * and not the custom listview file
		 */
		/*// TODO Implement Database
		schedule = new SimpleAdapter(this, list, R.layout.schedule_cell,
				new String[] { "home_team", "away_team", "home_logo",
						"away_logo", "game_outcome", "home_score",
						"away_score", "game_date", "game_location" },
				new int[] { R.schedule.home_team, R.schedule.away_team,
						R.schedule.home_logo, R.schedule.away_logo,
						R.schedule.game_outcome, R.schedule.home_score,
						R.schedule.away_score, R.schedule.game_date,
						R.schedule.game_location });
		setListAdapter(schedule);
		this.addItem(); // Populates ListView
*/
		/*
		 * if (((TextView) findViewById(R.schedule.outcome)).getText() == "L") {
		 * ((TextView) findViewById(R.schedule.outcome))
		 * .setTextColor(Color.RED); } else { ((TextView)
		 * findViewById(R.schedule.outcome)) .setTextColor(Color.GREEN); }
		 */
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

		String[] home_team = new String[] { "Vanderbilt", "Tennessee",
				"Vanderbilt" };
		String[] away_team = new String[] { "Florida", "Vanderbilt", "Kentucky" };
		Integer[] home_logo = new Integer[] { R.drawable.vandy_logo,
				R.drawable.tenn_logo, R.drawable.vandy_logo };
		Integer[] away_logo = new Integer[] { R.drawable.fla_logo,
				R.drawable.vandy_logo, R.drawable.kty_logo };
		Integer[] game_outcome = new Integer[] { R.drawable.loss_icon,
				R.drawable.win_icon, 0 };
		String[] home_score = new String[] { "13", "13", "" };
		String[] away_score = new String[] { "23", "34", "" };
		String[] game_date = new String[] { "Oct. 17, 7:00PM CST",
				"Oct. 24, 12:00PM CST", "Oct. 31, 12:00PM CST" };
		String[] game_location = new String[] { "Nashville, Tennessee",
				"Knoxville, Tennessee", "Nashville, Tennessee" };

		for (int i = 0; i < 3; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			/*
			 * "home_team", "away_team", "home_logo", "away_logo",
			 * "game_outcome", "home_score", "away_score", "game_date"
			 */
			item.put("home_team", home_team[i]);
			item.put("away_team", away_team[i]);
			item.put("home_logo", Integer.toString(home_logo[i]));
			item.put("away_logo", Integer.toString(away_logo[i]));
			item.put("game_outcome", Integer.toString(game_outcome[i]));
			item.put("home_score", home_score[i]);
			item.put("away_score", away_score[i]);
			item.put("game_date", game_date[i]);
			item.put("game_location", game_location[i]);
			list.add(item);
			schedule.notifyDataSetChanged();
		}
	}
}
