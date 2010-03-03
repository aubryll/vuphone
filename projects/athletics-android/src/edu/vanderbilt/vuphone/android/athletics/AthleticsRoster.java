package edu.vanderbilt.vuphone.android.athletics;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;

import edu.vanderbilt.vuphone.android.athletics.storage.DatabaseAdapter;

public class AthleticsRoster extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roster);

		DatabaseAdapter db = new DatabaseAdapter(this);
		db.open();
		
		Cursor cursor = db.fetchFilteredPlayers("number", "`team`='Vanderbilt'");
		
		
		String[] columns = {"number", "position", "name"};
		int[] column_ids = {R.roster_entry.number, R.roster_entry.position, R.roster_entry.name};
		setListAdapter(new SimpleCursorAdapter(this, R.layout.roster_entry, cursor, columns, column_ids));
		db.close();
	}
}
