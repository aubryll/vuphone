package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import edu.vanderbilt.vuphone.android.athletics.storage.DatabaseAdapter;

public class AthleticsStandings extends ListActivity
{
	private SimpleAdapter standings;
	ArrayList <HashMap <String, String>> list = new ArrayList <HashMap <String, String>>();
	ArrayAdapter<String> adapter = null;
	
	@ Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.standings_layout);
		standings = new SimpleAdapter(this, list, R.layout.standings_cell, 
						new String[] {"rank", "team", "record"},
						new int[] { R.standings.rank, R.standings.team,
										R.standings.record });
		setListAdapter(standings);
		this.addItem();
		this.addVandy();
	}	
	
	private void addItem()
	{
		String[] rank = new String[] { "2", "3", "4", "5", "6" };
		String[] team = new String[] { "Georgia", "LSU", "Florida", "Kentucky", "South Carolina" };
		String[] record = new String[] { "12-4", "12-4", "10-6", "8-8", "2-14" };
		for (int i = 0; i < 5; i++)
		{
			HashMap <String, String> item = new HashMap <String, String>();
			item.put("rank", rank[i]);
			item.put("team", team[i]);
			item.put("record", record[i]);
			list.add(item);
			standings.notifyDataSetChanged();
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
	
	private void addVandy()
	{
		CharSequence vandyrank = "1";
		CharSequence vandyteam = "Vanderbilt";
		CharSequence vandyrecord = "16-0";
		
		TextView vandyrankspot = ((TextView)findViewById(R.standings.vandyrank));
		vandyrankspot.setText(vandyrank);
		
		TextView vandyteamspot = ((TextView)findViewById(R.standings.vandyteam));
		vandyteamspot.setText(vandyteam);
		
		TextView vandyrecordspot = ((TextView)findViewById(R.standings.vandyrecord));
		vandyrecordspot.setText(vandyrecord);
	}
}
