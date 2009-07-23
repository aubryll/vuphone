/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.R;
import org.vuphone.vandyupon.android.eventloader.EventLoader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.maps.MapActivity;

/**
 * The main application window that pops up. Allows the user to see the events
 * on a map, and their current location on the map as well.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventViewer extends MapActivity {
	private EventViewerMap map_;

	/**
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/** Called when the Activity is first created */
	@Override
	protected void onCreate(Bundle ice) {
		super.onCreate(ice);
		setContentView(R.layout.event_map);

		map_ = (EventViewerMap) findViewById(R.id.event_map);

		// Schedule the EventLoader to run, if it has not been scheduled yet
		Intent loaderIntent = new Intent(this, EventLoader.class);
		PendingIntent loader = PendingIntent.getService(this, 0, loaderIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
//		AlarmManager am = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
//		am.cancel(loader);
//		am.setInexactRepeating(AlarmManager.RTC,
//				System.currentTimeMillis(),
//				30000, loader);
	}

	/** Called when the Activity is no longer visible */
	@Override
	protected void onPause() {
		super.onPause();
		map_.disableMyLocation();
	}

	/** Called when the Activity is about to be visible */
	@Override
	protected void onResume() {
		super.onResume();
		map_.enableMyLocation();
	}
}
