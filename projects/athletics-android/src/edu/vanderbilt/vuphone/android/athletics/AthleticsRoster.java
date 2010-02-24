package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class AthleticsRoster extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roster);
		
		ArrayList<HashMap<String, String>> roster = new 
			ArrayList<HashMap<String, String>>();

		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", "Larry");
		m.put("position", "QB");
		m.put("number", "1");
		
		HashMap<String, String> m2 = new HashMap<String, String>();
		m2.put("name", "Adam");
		m2.put("position", "TE");
		m2.put("number", "2");

		roster.add(m);
		roster.add(m2);

		String[] columns = {"number", "position", "name"};
		int[] column_ids = {R.roster_entry.number,
			R.roster_entry.position, R.roster_entry.name};
		setListAdapter(new SimpleAdapter(this, roster,
			R.layout.roster_entry, columns, column_ids));
	}
	
}
