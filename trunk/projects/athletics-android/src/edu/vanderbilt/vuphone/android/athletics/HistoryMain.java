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
 * This page is for the implementation of a page listing
 * 3 items: History, Traditions, Stadiums.
 * 
 * When clicked, History could have a blurb about
 * interesting facts about Vanderbilt's Athletics such as
 * the reason why we don't have an athletic department or 
 * how we started the SEC.
 * 
 * When clicked, Traditions could have another list
 * of our most popular traditions: School Colors, Football gameday traditions
 * ("Commodore Creed" in the football locker room, "Corridor of Captains", 
 * "the Star Walk", "the Star V", "Touchdown Foghorn", "Mr. Commodore" our 
 * mascot, "Freshman Walk", "Victory Flag", "Alma Mater" sung at end of games), 
 * our Rivalries, the V-U Hand Sign, our Fight Song "Dynamite"
 * (cool if a button when clicked plays an audio file with the song),
 * popular chants, the fact that our Men's Basketball Program is 
 * 1 of only 3 teams that has hit a 3-pointer every game since the line 
 * was invented.
 * 
 * When clicked, Stadiums could have another list of our
 * sport venues and when clicked will have a picture of 
 * the stadium, a history, capacity, location, and date opened. 
 * 
 * Idea for this page is credited to Moses Morjain
 * 
 * @author Grayson Sharpe
 */
public class HistoryMain extends ListActivity {

	ArrayAdapter<String> adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter main;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		setTitle("Vanderbilt History");

		main = new SimpleAdapter(this, list, R.layout.main_cell,
				new String[] { "history_title" }, new int[] { R.main.text });
		setListAdapter(main);
		this.addItem();
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		case 0:
			startActivity(new Intent(this, HistoryPage.class));
			break;
		default:
			startActivity(new Intent(this, TraditionsPage.class));
			break;

		}
	}

	private void addItem() {
		String[] title = new String[] { "History", "Traditions", "Stadiums" };

		for (int i = 0; i < 3; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("history_title", title[i]);

			list.add(item);
			main.notifyDataSetChanged();
		}
	}

}
