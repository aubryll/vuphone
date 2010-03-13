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
import edu.vanderbilt.vuphone.android.athletics.R;

/*
 * Lots of work to do. Started when clicked on in
 * HistoryMain
 * 
 * @author Grayson Sharpe
 */
public class TraditionsPage extends ListActivity {

	ArrayAdapter<String> adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter main;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		main = new SimpleAdapter(this, list, R.layout.main_cell,
				new String[] { "history_title" }, new int[] { R.main.text });
		setListAdapter(main);
		this.addItem();
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startActivity(new Intent(this, HistoryPage.class).putExtra(
				"listPosition", position));
	}

	private void addItem() {

		String[] title = new String[] { "Traditions" };

		for (int i = 0; i < 1; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("history_title", title[i]);

			list.add(item);
			main.notifyDataSetChanged();
		}

	}

}
