/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import edu.vanderbilt.vuphone.android.events.R;
import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;

/**
 * @author Hamilton Turner
 * 
 */
public class EventListActivity extends ListActivity {
	private EditText filterText = null;
	SimpleCursorAdapter adapter = null;

	private DBAdapter dbadapter_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.events_list);

		filterText = (EditText) findViewById(R.events_list.search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		dbadapter_ = new DBAdapter(this);
		dbadapter_.openReadable();
		Cursor c = dbadapter_.getAllEntries(null, null, null);

		adapter = new SimpleCursorAdapter(getApplicationContext(),
				android.R.layout.simple_list_item_1, c,
				new String[] { DBAdapter.COLUMN_NAME },
				new int[] { android.R.id.text1 });

		adapter.setCursorToStringConverter(new CursorToStringConverter() {

			public CharSequence convertToString(Cursor cursor) {
				return cursor.getString(cursor
						.getColumnIndex(DBAdapter.COLUMN_NAME));
			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {

			public Cursor runQuery(CharSequence constraint) {
				Cursor c = dbadapter_.getDatabase().query(
						DBAdapter.TABLE_NAME,
						new String[] { DBAdapter.COLUMN_ID,
								DBAdapter.COLUMN_NAME },
						DBAdapter.COLUMN_NAME + " LIKE '%" + constraint + "%'",
						null, null, null, null);
				return c;
			}
		});

		setListAdapter(adapter);
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			adapter.getFilter().filter(s);
		}

	};
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(this, EventDetailsActivity.class);
		i.putExtra(EventDetailsActivity.EVENT_ID, id);
		startActivity(i);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
		dbadapter_.close();
	}
}
