package edu.vanderbilt.vuphone.android.athletics;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;

import edu.vanderbilt.vuphone.android.athletics.storage.DatabaseAdapter;

/*
 * @author Russ Amos
 */
public class AthleticsRoster extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roster);

		/*
		 * String title is assigned the value of the item (name of the sport
		 * information) that the user clicked on in the previous activity plus
		 * the additional Extra of the name of the sport. String title cannot go
		 * before onCreate is called because of a problem with pending Intent.
		 */
		// TODO Readdress implementation when database is implemented on all
		// pages
		String title = getIntent().getExtras().getString("sports_title");
		setTitle("Vanderbilt " + title + " Roster");

		DatabaseAdapter db = new DatabaseAdapter(this);
		db.open();

		Cursor cursor = db
				.fetchFilteredPlayers("number", "`team`='Vanderbilt'");

		String[] columns = { "number", "position", "name" };
		int[] column_ids = { R.roster_entry.number, R.roster_entry.position,
				R.roster_entry.name };
		setListAdapter(new SimpleCursorAdapter(this, R.layout.roster_entry,
				cursor, columns, column_ids));
		db.close();
	}

}
