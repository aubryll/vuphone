package edu.vanderbilt.vuphone.android.athletics;


import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import edu.vanderbilt.vuphone.android.athletics.storage.DatabaseAdapter;

public class AthleticsScores extends ListActivity
{
	private SimpleAdapter scores;
	ArrayList <HashMap <String, String>> list = new ArrayList <HashMap <String, String>>();
	ArrayAdapter<String> adapter = null;
	@ Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_layout);
		scores = new SimpleAdapter(this, list, R.layout.score_cell, new String[] {
				"home_team", "away_team", "our_score", "their_score", "home_logo", "away_logo" },
				new int[] { R.scores.home_team, R.scores.away_team,
						R.scores.our_score, R.scores.their_score, R.scores.home_logo, R.scores.away_logo });
		setListAdapter(scores);
		this.addItem();
	}

	private void addItem()
	{
		
		String[] away_team = new String[] { "Florida", "Vanderbilt", "Kentucky" };
		String[] home_team = new String[] { "Vanderbilt", "Tennessee", "Vanderbilt" };
		Integer[] home_logo = new Integer[] { R.drawable.vandy_logo, R.drawable.tenn_logo, R.drawable.vandy_logo };
		Integer[] away_logo = new Integer[] { R.drawable.fla_logo, R.drawable.vandy_logo, R.drawable.kty_logo };
		String[] ourScore = new String[] { "23", "28", "32" };
		String[] theirScore = new String[] { "13", "27", "22" };
		for (int i = 0; i < 3; i++)
		{
			HashMap <String, String> item = new HashMap <String, String>();
			item.put("away_team", away_team[i]);
			item.put("home_team", home_team[i]);
			item.put("our_score", ourScore[i]);
			item.put("their_score", theirScore[i]);
			item.put("away_logo", Integer.toString(away_logo[i]));
			item.put("home_logo", Integer.toString(home_logo[i]));
			list.add(item);
			scores.notifyDataSetChanged();
		}
		
		/*
		DatabaseAdapter db = new DatabaseAdapter(this); db.open();
		db.createGame("Vanderbilt", "Florida", "Football", "season",
		"2010-03-02", "23", "44");
		db.createGame("Tennessee", "Vanderbilt", "Football", "season", "2010-03-02", "0", "100");
		Cursor cursor = db.fetchAllGames();
		
		String[] columns = {"hometeam", "homescore", "awayteam", "awayscore"}; 
		int[] column_ids = {R.scores.home_team,
		R.scores.our_score, R.scores.away_team, R.scores.their_score };
		setListAdapter(new SimpleCursorAdapter(this, R.layout.score_cell,
		cursor, columns, column_ids)); db.close();
		*/
	}
}
	
