package org.vuphone.wwatch.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Contacts.People;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * An activity that loads a list of all contacts with non-null numbers and 
 * allows the user to check the ones he would like to call in case of an 
 * emergency. The list of contact IDs is saved in a preference file.
 * @author Krzysztof Zienkiewicz
 *
 */
public class ContactPicker extends Activity implements View.OnClickListener {

	// Name of the preference file
	public static final String SAVE_FILE = "ContactPickerPreferences";
	// Used in the preference file to specify the size of the ID list
	public static final String LIST_SIZE_TAG = "ListSize";
	// Used in the preference file to mark an ID item
	public static final String LIST_ITEM_PREFIX_TAG = "ListItem";
	
	// List to hold all contact strings with non-null numbers (Name - Number)
	private final List<String> contactInfoList_ = new ArrayList<String>();
	// List to hold the IDs for contacts in contactList_
	private final List<Integer> contactIdList_ = new ArrayList<Integer>();
	// List to hold the IDs of selected contacts
	private final List<Integer> selectionList_ = new ArrayList<Integer>();

	//private final Context CONTEXT = (new Context()).getApplicationContext();
	
	// Intent used to start the updating service
	private Intent serviceIntent_;
	
	// PendingIntent used to trigger the service
	private PendingIntent serviceTrigger_;
	
	private ListView listView_;
	private Button submitButton_;
	private Button clearButton_;
	private Button cancelButton_;

	/**
	 * Populate the selectionList_ with checked IDs, save to a 
	 * preference file, and exit.
	 */
	private void onSubmitClicked() {
		// Fill selectionList_ with checked contact IDs
		SparseBooleanArray choices = listView_.getCheckedItemPositions();
		for (int i = 0; i < choices.size(); ++i) {
			int realIndex = choices.keyAt(i);
			if (choices.get(realIndex) == true) {	// If selected
				selectionList_.add(contactIdList_.get(realIndex));
			}
		}

		this.savePreferenceFile();
		this.finish();
	}
	
	/**
	 * Saved the IDs of the selected contacts to a private preference file.
	 * This file will only be readable by members of this application. The
	 * file name is ContactPicker.SAVE_FILE and it contains a set of integers
	 * with the following format:
	 * LIST_SIZE_TAG maps to an integer with the size of the ID list
	 * LIST_ITEM_PREFIX_TAG appended with an int between 0 and (LIST_SIZE_TAG-1)
	 * maps to an ID.
	 */
	public void savePreferenceFile() {
		// Save the IDs to a preference file.
		SharedPreferences prefs = super.getSharedPreferences(
				ContactPicker.SAVE_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(ContactPicker.LIST_SIZE_TAG, selectionList_.size());
		for (int i = 0; i < selectionList_.size(); ++i) {
			editor.putInt(ContactPicker.LIST_ITEM_PREFIX_TAG + i, 
					selectionList_.get(i));
		}
		editor.commit();
	}

	/**
	 * Unchecks all of the contacts.
	 * TODO - This makes the SparseBooleanArray as big the the contact list; 
	 * work on making this more efficient
	 */
	private void onClearClicked() {
		for (int i = 0; i < contactInfoList_.size(); ++i)
			listView_.setItemChecked(i, false);
	}

	/**
	 * Simply exit the activity without saving any information.
	 */
	private void onCancelClicked() {
		this.finish();
	}

	/**
	 * Overrides super.finish() to display a Toast with the selectionList_'s 
	 * contents.
	 * TODO - Remove this
	 */
	public void finish() {
		Toast.makeText(this, "selectionList_: " + selectionList_.toString(),
				Toast.LENGTH_LONG).show();
		
		this.scheduleService();
		
		super.finish();
	}

	/**
	 * Schedules the update service to run repeatedly
	 */
	private void scheduleService() {
		AlarmManager man = (AlarmManager) super.getSystemService(
			Context.ALARM_SERVICE);

		// Trigger the first call in 10 seconds.
		final long trigger = SystemClock.elapsedRealtime() + (10 * 1000);

		// Call the service every TIME_INTERVAL milliseconds, waking up the
		// device if necessary.
		man.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				trigger, UpdateContactsService.TIME_INTERVAL,
				this.serviceTrigger_);
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
		} else if (v.equals(cancelButton_)) {
			this.onCancelClicked();
		}
	}

	/**
	 * Set up the UI.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.pickerview);
		
		serviceIntent_ = new Intent(this,
				org.vuphone.wwatch.android.UpdateContactsService.class);

		serviceTrigger_ = PendingIntent.getService(
				this, 0, serviceIntent_, PendingIntent.FLAG_CANCEL_CURRENT);
		
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
		cancelButton_ = (Button) super.findViewById(R.id.cancel_button);

		submitButton_.setOnClickListener(this);
		clearButton_.setOnClickListener(this);
		cancelButton_.setOnClickListener(this);

	}

	/**
	 * Populated this object's lists with contact information based on contacts
	 * with non-null phone numbers.
	 * TODO - Add code to load the preference file and check the emergency 
	 * contacts 
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
				contactInfoList_.add(c.getString(idCol) + " - " + c.getString(nameCol));
				contactIdList_.add(c.getInt(idCol));
			}
		}
		c.close();
	}
}