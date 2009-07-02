package org.vuphone.wwatch.android.services;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.http.HTTPPoster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Contacts.People;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * A service used to update the emergency contact information to the server.
 * This service is triggered at specific intervals using the system's
 * AlarmManager. The initial schedule call is made by ContactPicker.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */

public class UpdateContactsService extends Service {
	// How often to run this service (in milliseconds)
	public static final long TIME_INTERVAL = 1 * (24 * 60 * 60 * 1000); // 1 day

	// A list of 10 digit strings containing only digit characters.
	private final ArrayList<String> numberList_ = new ArrayList<String>();

	/**
	 * Returns null since we're not binding to this service
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Called when the service is first created. A no-op.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * Called right before the service quits.
	 * 
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(VUphone.tag, "onDestroy(). List: " + numberList_.toString());
	}

	/**
	 * Called after Service.onCreate(). Used to set up the service.
	 * 
	 * @param intent
	 *            Intent that started this service
	 * @param startId
	 *            Ignored in this implementation
	 * @see Serive.onStart(Intent, int)
	 */
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Log.v(VUphone.tag, "UpdateContactsService started.");
		this.loadNumbers();
		this.sendToServer();

		super.stopSelf();
	}

	/**
	 * Erases the emergency contact information stored on the server and uploads
	 * the ones specified in the preference file.
	 */
	private void sendToServer() {
		TelephonyManager man = (TelephonyManager) super
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String deviceId = man.getDeviceId();
		
		final Handler handler = new Handler();
		Log.v(VUphone.tag, "Starting contact upload");
		Toast.makeText(this, "Uploading contact information.", Toast.LENGTH_SHORT).show();
		
		new Thread(new Runnable() {
			public void run() {	
				final HttpResponse resp = HTTPPoster.doContactUpdate(deviceId, numberList_);
				Log.v(VUphone.tag, "Finished contact upload");
				
				final int code = (resp == null) ? -1 : resp.getStatusLine().getStatusCode();
				
				handler.postAtFrontOfQueue(new Runnable() {
					public void run() {
						String msg = "Contact upload successful.";
						if (code != 200)
							msg = "Contact upload failed.";
						Toast.makeText(UpdateContactsService.this, msg,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}, "ContactUploadThread").start();
		
	}

	/**
	 * Opens ContactPicker's preference file and populates the list of numbers
	 * based on the IDs stored there.
	 */
	private void loadNumbers() {
		// Open up the preference file

		SharedPreferences pref = super.getSharedPreferences(
				VUphone.PREFERENCES_FILE, Context.MODE_PRIVATE);

		final int size = pref.getInt(VUphone.LIST_SIZE_TAG, -1);

		// Make sure the file is not corrupted or malformed.
		if (size == -1) { // The field doesn't exist so either the preference
			// file is corrupt or it doesn't exist
			super.stopSelf();
		}

		// Build a selection string
		String selection = "";
		for (int i = 0; i < size; ++i) {
			int id = pref.getInt(VUphone.LIST_ITEM_PREFIX_TAG + i, -1);

			// If id is missing, skip it and Log the error.
			if (id == -1) {
				Log.v(VUphone.tag, "ID at index " + i
						+ " not found. Total IDs: " + size);
				continue;
			}
			selection += "OR people._id='" + id + "' ";
		}

		// Strip the first "OR " and trim the last " "
		if (selection.length() > 0)
			selection = selection.substring(3).trim();
		else
			selection = "people._id='-1'"; // This will return an empty Cursor
		// which leads to an empty list

		Log.v(VUphone.tag, "Selection string: \"" + selection + "\"");

		// Query a cursor to a table with a number columns where the ID matches
		// the list of IDs from the preference file.
		String[] projection = { People.NUMBER };

		final Cursor c = super.getContentResolver().query(People.CONTENT_URI,
				projection, selection, null, null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			try {
				String number = this.formatNumber(c.getString(0));
				numberList_.add(number);
			} catch (IllegalArgumentException e) {
				Log.d(VUphone.tag, "A malformed number passed into "
						+ "UpdateContactsService.formatNumber()");
			}
		}

		c.close();
	}

	/**
	 * Returns a phone number string formated to contain only 10 digit
	 * characters. If an area code is not provided in str, the user's area code
	 * will be used.
	 * 
	 * @param str
	 *            A phone number to format.
	 * @return A formatted number.
	 */
	private String formatNumber(String str) {
		// First, strip all non digit characters.
		String number = "";
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if (Character.isDigit(c))
				number += c;
		}

		if (number.length() == 10)
			return number;

		if (number.length() != 7)
			throw new IllegalArgumentException("Unable to convert the give "
					+ "number to a valid format");

		// We have a 7 digit number so fetch user's area code and prepend it
		TelephonyManager man = (TelephonyManager) super
				.getSystemService(Context.TELEPHONY_SERVICE);
		String myNumber = man.getLine1Number(); // Reported as 11 digits
		if (myNumber.length() != 11)
			throw new RuntimeException(
					"User's phone number returned in an unexpected format");
		String areaCode = myNumber.substring(1, 4);
		number = areaCode + number;

		return number;
	}
}
