package org.vuphone.wwatch.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.Contacts.PeopleColumns;
import android.provider.Contacts.Phones;
import android.util.Log;
import android.widget.Toast;


/**
 * A service used to update the emergency contact information to the server.
 * This service is triggered at specific intervals using the system's 
 * AlarmManager. The initial schedule call is made by ContactPicker. Each 
 * subsequent call is made by this service.  
 * @author Krzysztof Zienkiewicz
 *
 */
public class UpdateContactsService extends Service {

	private final List<String> numberList_ = new ArrayList<String>();
	
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public void onCreate() {
		
	}
	
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "UpdateContactsService started", Toast.LENGTH_SHORT).show();
		this.scheduleNextRun(intent);
		this.loadNumbers();
	}
	
	/**
	 * Opens ContactPicker's preference file and populates numberList_ based
	 * on the IDs stored there.
	 */
	private void loadNumbers() {
		// Open up the preference file and make sure it's OK
		
		SharedPreferences pref = super.getSharedPreferences(
				ContactPicker.SAVE_FILE, Context.MODE_PRIVATE);
		
		final int size = pref.getInt(ContactPicker.LIST_SIZE_TAG, -1);
		if (size == -1) {	// The field doesn't exist so either the preference
							// file is corrupt or it doesn't exist
			Toast.makeText(this, "UpdateContact Service can't load" +
					" preference file", Toast.LENGTH_LONG).show();
			super.stopSelf();
		}
		
		// Load the IDs into an array
		final String[] idArray = new String[size];
		for (int i = 0; i < size; ++i) {
			int id = pref.getInt(ContactPicker.LIST_ITEM_PREFIX_TAG + i, -1);
			Log.v("VUPHONE", "ID: " + id);
			if (id == -1)
				throw new RuntimeException("UpdateContactsService detected a " +
						"corrupted preference file");
			idArray[i] = (new Integer(id)).toString();
		}
		

		// Query a cursor to a table with a name columns, sorted alphabetically
		// where the ID matches idArray.
		String[] projection = {PeopleColumns.NAME};
		String selection = "people._id=?"; //'5' OR people._id='6' OR people._id='7'";
		String sortOrder = PeopleColumns.NAME;

		
		final Cursor c = super.getContentResolver().query(
				People.CONTENT_URI, projection, selection,
				idArray, sortOrder);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String name = c.getString(0);	// There is only one column so 0.
			Log.v("VUPHONE", name);
		}
		
		c.close();

		
	}
		
	private void scheduleNextRun(Intent intent) {
		
	}

}
