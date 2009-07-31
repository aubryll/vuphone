package org.vuphone.assassins.android.godfather;

import java.util.ArrayList;
import java.util.List;

import org.vuphone.assassins.android.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * An Activity that loads the list of all non-null contacts currently on the
 * phone and allows the user to choose which of them will be invited to join
 * the game.  Most of this was stolen from Krzysztof's ContactPicker class in
 * Wreck Watch, but the service stuff and the pre-loading saved selections 
 * were removed since they're not necessary for this application.
 * 
 * @author Scott Campbell
 */
public class ActivityChoosePlayers extends Activity 
				implements View.OnClickListener{

	// List to hold all contact strings with non-null numbers (Name - Number)
	private final List<String> contactInfoList_ = new ArrayList<String>();
	// List to hold the IDs for contacts in contactList_
	private final List<Integer> contactIdList_ = new ArrayList<Integer>();
	// List to hold the IDs of selected contacts
	private final List<Integer> selectionList_ = new ArrayList<Integer>();

	private ListView listView_;
	private Button submitButton_;
	private Button clearButton_;
	
	/**
	 * Populated this object's lists with contact information based on contacts
	 * with non-null phone numbers.
	 */
	private void loadContactInformation() {
		// Get a cursor to the contact information sorted alphabetically by name
		Cursor c = super.getContentResolver().query(People.CONTENT_URI, null,
				null, null, People.NAME);

		// Set up contact database constants
		final int nameCol = c.getColumnIndex(People.NAME);
		final int numberCol = c.getColumnIndex(People.NUMBER);
		final int idCol = c.getColumnIndex(People._ID);

		// Initialize the cursor and populate the lists.
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (c.getString(numberCol) != null) {
				contactInfoList_.add(c.getString(nameCol));
				contactIdList_.add(c.getInt(idCol));
			}
		}
		c.close();
	}

	/**
	 * Unchecks all of the contacts.
	 */
	private void onClearClicked() {
		listView_.clearChoices();
		if (contactInfoList_.size() > 0)
			listView_.setItemChecked(0, false); // Necessary to force redraw.
	}
	
	/**
	 * Called when one of the buttons was clicked. Dispatches the appropriate
	 * calls.
	 */
	public void onClick(View v) {
		if (v.equals(submitButton_)) {
			this.onSubmitClicked();
		} else if (v.equals(clearButton_)) {
			this.onClearClicked();
		}
	}
	
	/**
	 * The "constructor". Sets up the fields and the layout. This is either
	 * called because somebody requested this activity to start up or because
	 * the activity is being reloaded due to a keyboard flip.
	 */
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.choose_players);
		
		// Populate the lists.
		this.loadContactInformation();

		// Fetch and setup the ListView
		listView_ = (ListView) super.findViewById(R.id.list_view);
		listView_.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,
				contactInfoList_));
		listView_.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView_.setItemsCanFocus(true);
		listView_.setTextFilterEnabled(true);
		
		// Fetch the buttons and setup the click listeners
		submitButton_ = (Button) super.findViewById(R.id.submit_button);
		clearButton_ = (Button) super.findViewById(R.id.clear_button);

		submitButton_.setOnClickListener(this);
		clearButton_.setOnClickListener(this);

		if (contactInfoList_.size() == 0)
			Toast.makeText(this, "Sorry. You have no contacts to display",
					Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Populate the selectionList_ with checked IDs, save to a preference file,
	 * and exit.
	 */
	private void onSubmitClicked() {
		// Fill selectionList_ with checked contact IDs
		SparseBooleanArray choices = listView_.getCheckedItemPositions();
		for (int i = 0; i < choices.size(); ++i) {
			int realIndex = choices.keyAt(i);
			if (choices.get(realIndex) == true) { // If selected
				selectionList_.add(contactIdList_.get(realIndex));
			}
		}
		
		//TODO - Somehow tell the contacts that they are invited to join
		//the game.

		this.finish();
	}
	
}
