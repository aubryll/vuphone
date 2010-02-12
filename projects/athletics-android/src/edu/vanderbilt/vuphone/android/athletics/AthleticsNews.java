package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AthleticsNews extends ListActivity {
	ArrayAdapter<String> adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter news;
	private static final int ADD_ITEM_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_layout);

		news = new SimpleAdapter(this, list, R.layout.news_cell, new String[] {
				"news_title", "news_date" }, new int[] { R.news.title,
				R.news.date });
		setListAdapter(news);
		this.addItem();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent details = new Intent(this, NewsDetails.class);
		details.putExtra("news_title",((TextView)findViewById(R.news.title)).getText());
		details.putExtra("news_date",((TextView)findViewById(R.news.date)).getText());
		startActivity(details);
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
		String[] title = new String[] { "Taylor breaks out at the right time",
				"Vanderbilt-Tennessee postgame quotes",
				"Vanderbilt hosts Tennessee Tuesday" };
		String[] date = new String[] { "2/25/10", "2/22/10", "2/20/10" };

		for (int i = 0; i < 3; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("news_title", title[i]);
			item.put("news_date", date[i]);

			list.add(item);
			news.notifyDataSetChanged();
		}
	}

}
