/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.eventloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.xml.sax.InputSource;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.events.Constants;
import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;

/**
 * Occasionally started by AlarmManager to retrieve new events from the server,
 * and process those events (put them into the local database).
 * 
 * Passes off the duty of performing the request to {@link EventRequestor} and
 * the duty of parsing the request to {@link EventHandler}. Called back every
 * time an event is ready for inserting into the database.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventLoader extends Service {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventLoader: ";

	/** Used to insert events into the database */
	private DBAdapter database_ = null;

	/** Called when the Service is first started */
	@Override
	public void onCreate() {
		super.onCreate();

		database_ = new DBAdapter(this);
		database_.openWritable();
	}

	/** Called when the Service is destroyed */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/** Called every time the Service is started */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i(tag, pre + "Starting the load - " + System.currentTimeMillis());
		loadEvents(database_, this);
		Log.i(tag, pre + "Finished the load - " + System.currentTimeMillis());
	}

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Starts the EventRequestor and the EventHandler
	 */
	protected static void loadEvents(DBAdapter openDatabase, Context c) {
		long time = openDatabase.getLargestUpdatedTime();
		ByteArrayOutputStream xmlResponse = EventRequestor.doEventRequest(
				Constants.vandyCenter, 10000, time, c);

		if (xmlResponse == null) {
			Log.e(tag, pre + "Unable to continue, there is no XML response");
			return;
		}

		EventHandler handler = new EventHandler(openDatabase);

		handler.processXML(new InputSource(new ByteArrayInputStream(xmlResponse
				.toByteArray())));

	}

	/**
	 * Inserts events into the database as they are returned by the Handler.
	 * Attempts to insert each event multiple times if a failure occurs, until
	 * finally giving up and printing an error message for that event
	 * 
	 * @param name
	 *            the event name
	 * @param latitude
	 *            the event latitude
	 * @param longitude
	 *            the event longitude
	 * @param owner
	 *            true if this device is the owner of the event, false otherwise
	 * @param startTime
	 *            the event start time
	 * @param endTime
	 *            the event end time
	 * @param updateTime
	 *            the last time this event was updated
	 * @param serverId
	 *            the unique, server-based id for this event
	 */
	protected static void handleEvent(DBAdapter openDatabase, String name,
			double latitude, double longitude, boolean owner, long startTime,
			long endTime, long updateTime, long serverId) {
		final int latE6 = (int) (latitude * 1E6);
		final int lonE6 = (int) (longitude * 1E6);
		final GeoPoint location = new GeoPoint(latE6, lonE6);
		boolean handled = false;
		int count = 0;

		do {
			handled = openDatabase.insertOrUpdateEvent(name, startTime,
					endTime, location, updateTime, owner, serverId);
			++count;
			if (count > 1)
				Log.w(tag, pre + "Database appears to be locked");
			if (count == 5)
				Log.e(tag, pre + "Giving up on storing event");
		} while (handled == false && count < 5);
		Log.d(tag, pre + "Done handlng event");
	}

	public static void manualUpdate(Context c) {
		DBAdapter db = new DBAdapter(c);
		db.openWritable();
		loadEvents(db, c);
	}
}
