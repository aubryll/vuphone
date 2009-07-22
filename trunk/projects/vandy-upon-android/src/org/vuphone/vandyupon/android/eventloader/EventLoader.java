/**
 * 
 */
package org.vuphone.vandyupon.android.eventloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.eventstore.DBAdapter;
import org.xml.sax.InputSource;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

/**
 * @author Hamilton Turner
 * 
 */
public class EventLoader extends Service {
	private static final String tag = Constants.tag;
	private static final String pre = "EventLoader: ";

	private DBAdapter database_;

	@Override
	public void onCreate() {
		super.onCreate();

		database_ = new DBAdapter(this);
		database_.openWritable();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(tag, pre + "onDestroy");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i(tag, pre + "onStart");
		Toast.makeText(this, "Starting", Toast.LENGTH_LONG).show();
		loadEvents();
		Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
	}

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void loadEvents() {
		long time = database_.getLargestUpdatedTime();
		ByteArrayOutputStream xmlResponse = EventRequestor.doEventRequest(
				Constants.vandyCenter, 10000, time, this);

		if (xmlResponse == null) {
			Log.e(tag, pre + "Unable to continue, there is no XML response");
			return;
		}

		EventHandler handler = new EventHandler();

		handler.processXML(new InputSource(new ByteArrayInputStream(xmlResponse
				.toByteArray())), this);

	}

	protected void handleEvent(String name, double latitude, double longitude,
			boolean owner, long startTime, long endTime, long updateTime,
			long serverId) {
		final int latE6 = (int) (latitude * 1E6);
		final int lonE6 = (int) (longitude * 1E6);
		final GeoPoint location = new GeoPoint(latE6, lonE6);
		boolean handled = false;
		int count = 0;

		do {
			handled = database_.insertOrUpdateEvent(name, startTime, endTime,
					location, updateTime, owner, serverId);
			++count;
			if (count > 1)
				Log.w(tag, pre + "Database appears to be locked");
			if (count == 5)
				Log.e(tag, pre + "Giving up on storing event");
		} while (handled == false && count < 5);
		Log.d(tag, pre + "Done handlng event");
	}
}
