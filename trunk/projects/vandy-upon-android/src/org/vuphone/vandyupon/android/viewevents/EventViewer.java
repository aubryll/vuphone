/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.R;
import org.vuphone.vandyupon.android.eventloader.EventLoader;
import org.vuphone.vandyupon.android.submitevent.SubmitEvent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.google.android.maps.MapActivity;

/**
 * The main application window that pops up. Allows the user to see the events
 * on a map, and their current location on the map as well.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventViewer extends MapActivity {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventViewer: ";

	private EventViewerMap map_;

	/** Constants to identify MenuItems */
	private static final int MENUITEM_NEW_EVENT = 0;
	private static final int MENUITEM_FILTER_POS = 1;
	private static final int MENUITEM_FILTER_TIME = 2;
	private static final int MENUITEM_FILTER_TAG = 3;
	private static final int MENUITEM_MAP_MODE = 4;

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
		AlarmManager am = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		am.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
				AlarmManager.INTERVAL_DAY, loader);
	}

	/** Called when the options menu is first created */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENUITEM_NEW_EVENT, Menu.NONE, "New Event");
		SubMenu filter = menu.addSubMenu(0, -1, Menu.NONE, "Filter");
		filter.add(0, MENUITEM_FILTER_POS, Menu.NONE, "By Position");
		filter.add(0, MENUITEM_FILTER_TIME, Menu.NONE, "By Time");
		filter.add(0, MENUITEM_FILTER_TAG, Menu.NONE, "By Tags");
		menu.add(0, MENUITEM_MAP_MODE, Menu.NONE, "Map Mode");
		return true;
	}

	/** Handles menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENUITEM_NEW_EVENT:
			Intent submitEvent = new Intent(this, SubmitEvent.class);
			startActivity(submitEvent);
			break;
		case MENUITEM_FILTER_POS:
			
			break;
		case MENUITEM_FILTER_TAG:
			
			break;
		case MENUITEM_FILTER_TIME:
			
			break;
		case MENUITEM_MAP_MODE:
			
			
			break;
		default:
			Log.w(tag, pre + "No menu case matched! Did we open a submenu?");
		}
		return true;
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
