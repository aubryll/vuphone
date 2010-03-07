package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import edu.vanderbilt.vuphone.android.athletics.storage.DatabaseAdapter;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class AthleticsSchedule extends ListActivity {

	ArrayAdapter<String> adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter schedule;
	private static final int ADD_ITEM_ID = 1;

	static final String[] TEAMS = new String[] { "Florida", "@ Florida" };
	static final String[] SCORES = new String[] { "56-14", "14-23" };
	static final String[] OUTCOMES = new String[] { "W", "L" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_layout);
		/*
		 * DatabaseAdapter db = new DatabaseAdapter(this); db.open();
		 * db.createGame("Vanderbilt", "Florida", "Football", "season",
		 * "2010-03-02", "23", "44"); Cursor cursor = db.fetchAllGames();
		 * 
		 * String[] columns = {"hometeam", "homescore", "awayscore", "awayteam",
		 * "time"}; int[] column_ids = {R.schedule.teamName,
		 * R.schedule.our_score, R.schedule.their_score, R.schedule.date};
		 * setListAdapter(new SimpleCursorAdapter(this, R.layout.schedule_cell,
		 * cursor, columns, column_ids)); db.close();
		 */

		schedule = new SimpleAdapter(this, list, R.layout.schedule_cell,
				new String[] { "home_team", "away_team", "home_logo",
						"away_logo", "game_outcome", "home_score",
					 	"away_score", "game_date"}, new int[] {
						R.schedule.home_team, R.schedule.away_team,
						R.schedule.home_logo, R.schedule.away_logo,
						R.schedule.game_outcome, R.schedule.home_score, 
						R.schedule.away_score, R.schedule.game_date });
		setListAdapter(schedule);
		this.addItem();

		/*
		 * if (((TextView) findViewById(R.schedule.outcome)).getText() == "L") {
		 * ((TextView) findViewById(R.schedule.outcome))
		 * .setTextColor(Color.RED); } else { ((TextView)
		 * findViewById(R.schedule.outcome)) .setTextColor(Color.GREEN); }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_ITEM_ID, Menu.NONE, R.string.add_item);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ADD_ITEM_ID:
			addItem();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void addItem() {
		
		String[] home_team = new String[] { "Vanderbilt", "Kentucky" };
		String[] away_team = new String[] { "Florida", "Tennessee" };
		Integer[] home_logo = new Integer[] { R.drawable.tenn_logo, R.drawable.kty_logo };
		Integer[] away_logo = new Integer[] { R.drawable.fla_logo, R.drawable.tenn_logo };
		Integer[] game_outcome = new Integer[] { R.drawable.loss_icon, R.drawable.win_icon };
		String[] home_score = new String[] { "23", "13" };
		String[] away_score = new String[] { "13", "34" };
		String[] game_date = new String[] { "Mar. 08 7:00PM", "Mar. 24 12:00PM" };

		for (int i = 0; i < 2; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			/*
			 * "home_team", "away_team", "home_logo",
						"away_logo", "game_outcome", "home_score",
					 	"away_score", "game_date"
			 */
			item.put("home_team", home_team[i]);
			item.put("away_team", away_team[i]);
			item.put("home_logo", Integer.toString(home_logo[i]));
			item.put("away_logo", Integer.toString(away_logo[i]));
			item.put("game_outcome", Integer.toString(game_outcome[i]));
			item.put("home_score", home_score[i]);
			item.put("away_score", away_score[i]);
			item.put("game_date", game_date[i]);
			list.add(item);
			schedule.notifyDataSetChanged();
		}
	}
	/*
	 * private String[] getStringArrayList() { // TODO Auto-generated method
	 * stub String[] SPORTS = new String[] {"Football"};
	 * 
	 * return SPORTS; }
	 * 
	 * private TextWatcher filterTextWatcher = new TextWatcher() {
	 * 
	 * public void afterTextChanged(Editable s) { }
	 * 
	 * public void beforeTextChanged(CharSequence s, int start, int count, int
	 * after) { }
	 * 
	 * public void onTextChanged(CharSequence s, int start, int before, int
	 * count) { adapter.getFilter().filter(s); }
	 * 
	 * };
	 * 
	 * @Override protected void onDestroy() { super.onDestroy();
	 * filterText.removeTextChangedListener(filterTextWatcher); }
	 * 
	 * /*
	 * 
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.schedule_layout);
	 * 
	 * setContentView(R.layout.filterable_listview);
	 * 
	 * 
	 * 
	 * ((TextView)findViewById(R.schedule.date)).setText("");
	 * ((TextView)findViewById(R.schedule.outcome)).setText("");
	 * ((TextView)findViewById(R.schedule.score)).setText("");
	 * ((TextView)findViewById(R.schedule.teamName)).setText("");
	 * ((ImageView)findViewById
	 * (R.schedule.teamIcon)).setImageResource(R.drawable.icon);
	 */
}
