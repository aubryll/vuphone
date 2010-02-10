package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;


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

		schedule = new SimpleAdapter(
				this,
				list,
				R.layout.schedule_cell,
				new String[] { "team_name", "game_outcome", "our_score",
						"their_score", "team_icon", "game_date", "score_hash" },
				new int[] { R.schedule.teamName, R.schedule.outcome,
						R.schedule.our_score, R.schedule.their_score,
						R.schedule.teamIcon, R.schedule.date, R.schedule.hash });
		setListAdapter(schedule);
		this.addItem();
		/*
		if (((TextView) findViewById(R.schedule.outcome)).getText() == "L") {
			((TextView) findViewById(R.schedule.outcome))
					.setTextColor(Color.RED);
		} else {
			((TextView) findViewById(R.schedule.outcome))
					.setTextColor(Color.GREEN);
		}
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
		String[] team = new String[] { "@ Florida", "Tennessee", "@ Kentucky" };
		String[] outcome = new String[] { "W", "W", "" };
		String[] ourScore = new String[] { "23", "28", "" };
		String[] theirScore = new String[] { "13", "27", "" };
		String[] date = new String[] { "", "", "10/1/2010" };
		String[] hash = new String[] { "-", "-", "" };
		Integer[] logo = new Integer[] {R.drawable.fla_logo, R.drawable.tenn_logo, R.drawable.kty_logo};

		for (int i = 0; i < 3; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("team_name", team[i]);
			item.put("game_outcome", outcome[i]);
			item.put("our_score", ourScore[i]);
			item.put("their_score", theirScore[i]);
			item.put("team_icon", Integer.toString(logo[i]));
			item.put("game_date", date[i]);
			item.put("score_hash", hash[i]);
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
