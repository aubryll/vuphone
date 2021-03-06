/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

import edu.vanderbilt.vuphone.android.events.About;
import edu.vanderbilt.vuphone.android.events.Constants;
import edu.vanderbilt.vuphone.android.events.R;
import edu.vanderbilt.vuphone.android.events.eventloader.EventLoader;
import edu.vanderbilt.vuphone.android.events.eventloader.LoadingListener;
import edu.vanderbilt.vuphone.android.events.eventstore.DBAdapter;
import edu.vanderbilt.vuphone.android.events.filters.PositionActivity;
import edu.vanderbilt.vuphone.android.events.filters.PositionFilter;
import edu.vanderbilt.vuphone.android.events.filters.TimeActivity;
import edu.vanderbilt.vuphone.android.events.submitevent.SubmitEvent;

/**
 * The main application window that pops up. Allows the user to see the events
 * on a map, and their current location on the map as well.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventViewer extends MapActivity implements LoadingListener {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventViewer: ";

	/** The map we are using */
	private EventViewerMapView map_;

	/** Pane that drops down and allows user to see event details */
	private LinearLayout eventDetailsPane_;

	/**
	 * Handle used to invalidate the current view after showing/hiding the
	 * details pane
	 */
	private RelativeLayout eventMap_;

	// /** Used to update the UI from another thread *
	private Handler uiHandler_;

	/** Constants to identify MenuItems */
	private static final int MENUITEM_NEW_EVENT = 0;
	private static final int MENUITEM_FILTER_POS = 1;
	private static final int MENUITEM_FILTER_TIME = 2;
	private static final int MENUITEM_FILTER_TAG = 3;
	private static final int MENUITEM_MAP_SATELLITE = 4;
	private static final int MENUITEM_MAP_NORM = 5;
	private static final int MENUITEM_MAP_STREET = 6;
	private static final int MENUITEM_MANUAL_UPDATE = 7;
	private static final int MENUITEM_VIEW_LIST = 8;
	private static final int MENUITEM_ABOUT = 9;

	/** Constants to identify activities we requested */
	private static final int REQUEST_POSITION_FILTER = 0;
	private static final int REQUEST_TIME_FILTER = 1;
	private static final int REQUEST_TAGS_FILTER = 2;

	/**
	 * Gets passed to the UI thread every time a new event is loaded
	 */
	final Runnable eventLoaded_ = new Runnable() {
		public void run() {
			map_.invalidate();
		}
	};

	/**
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/** Called when an activity has a result to return to us */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_POSITION_FILTER:
			switch (resultCode) {
			case Constants.RESULT_CLEAR:
				Toast.makeText(this, "Cleared position filter",
						Toast.LENGTH_SHORT).show();
				map_.updatePositionFilter(null);
				break;
			case Constants.RESULT_UPDATE:
				Bundle extras = data.getExtras();
				String name;
				PositionFilter pf;
				boolean isCurrent = extras
						.getBoolean(PositionActivity.EXTRA_LOCATION_IS_CURRENT);
				int rad = extras.getInt(PositionActivity.EXTRA_RADIUS);

				if (isCurrent) {
					name = "My Current Location";
					pf = new PositionFilter(rad, this);
				} else {
					name = data.getExtras().getString(
							PositionActivity.EXTRA_LOCATION_NAME);
					int lat = extras
							.getInt(PositionActivity.EXTRA_LOCATION_LAT);
					int lon = extras
							.getInt(PositionActivity.EXTRA_LOCATION_LON);
					pf = new PositionFilter(new GeoPoint(lat, lon), rad);

				}

				Toast.makeText(this, "Set position to '" + name + "'",
						Toast.LENGTH_SHORT).show();
				map_.updatePositionFilter(pf);

				break;
			default:
			case Constants.RESULT_CANCELED:
				break;
			}
			break;
		case REQUEST_TAGS_FILTER:
			break;
		case REQUEST_TIME_FILTER:
			break;
		}
	}

	/** Called when the Activity is first created */
	@Override
	protected void onCreate(Bundle ice) {
		super.onCreate(ice);
		setContentView(R.layout.event_map);

		eventDetailsPane_ = (LinearLayout) findViewById(R.id.event_details_pane);
		eventMap_ = (RelativeLayout) findViewById(R.id.event_map);

		map_ = (EventViewerMapView) findViewById(R.id.event_viewer_map);
		map_.getEventOverlay().setOnFocusChangedListener(this);

		// Schedule the EventLoader to run, if it has not been scheduled yet
		Intent loaderIntent = new Intent(getApplicationContext(),
				EventLoader.class);
		PendingIntent loader = PendingIntent.getService(
				getApplicationContext(), 0, loaderIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		Log.i(tag, pre + "Registered to update events every 15 min");
		am.setInexactRepeating(AlarmManager.RTC,
				System.currentTimeMillis() + 1500, AlarmManager.INTERVAL_DAY,
				loader);

		Looper loop = Looper.myLooper();
		if (loop == null)
			Looper.getMainLooper();

		uiHandler_ = new Handler(loop);

		EventLoader.registerLoadingListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		EventLoader.unregisterLoadingListener(this);
	}

	/** Called when the options menu is first created */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO - Uncomment after finishing the SubmitEvent class!
		// menu.add(0, MENUITEM_NEW_EVENT, Menu.NONE, "New Event");

		SubMenu filter = menu.addSubMenu(0, -1, Menu.NONE, "Filter");
		filter.setIcon(getResources().getDrawable(
				android.R.drawable.ic_menu_view));
		filter.add(0, MENUITEM_FILTER_POS, Menu.NONE, "By Position");
		filter.add(0, MENUITEM_FILTER_TIME, Menu.NONE, "By Time");
		// filter.add(0, MENUITEM_FILTER_TAG, Menu.NONE, "By Tags");

		SubMenu map = menu.addSubMenu(0, -1, Menu.NONE, "Map Mode");
		map.setIcon(getResources().getDrawable(
				android.R.drawable.ic_menu_mapmode));
		map.add(0, MENUITEM_MAP_NORM, Menu.NONE, "Map");
		map.add(0, MENUITEM_MAP_SATELLITE, Menu.NONE, "Satellite");
		// map.add(0, MENUITEM_MAP_STREET, Menu.NONE, "Street View");

		menu.add(0, MENUITEM_VIEW_LIST, Menu.NONE, "View All").setIcon(
				android.R.drawable.ic_menu_sort_alphabetically);

		SubMenu more = menu.addSubMenu(0, -1, Menu.NONE, "More");
		more.setIcon(getResources()
				.getDrawable(android.R.drawable.ic_menu_more));
		more.add(0, MENUITEM_MANUAL_UPDATE, Menu.NONE, "Manual Update");
		more.add(0, MENUITEM_ABOUT, Menu.NONE, "About");
		return true;
	}

	public void onFocusChanged(EventPin pin) {
		if (pin == null) {
			eventDetailsPane_.setVisibility(View.GONE);
			eventMap_.invalidate();
			return;
		}

		TextView tv = (TextView) findViewById(R.id.TV_event_details_title);

		long rowId = pin.getDBRowId();
		DBAdapter db = new DBAdapter(this);
		db.openReadable();
		String desc = db.getSingleRowDescription(rowId);
		db.close();

		long timeInMilliseconds = Long.parseLong(pin.getStartTime()) * 1000;
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(timeInMilliseconds);

		GregorianCalendar gcEnd = new GregorianCalendar();
		gcEnd.setTimeInMillis(Long.parseLong(pin.getEndTime()) * 1000);

		tv.setText(pin.getName() + "\nStart: " + gc.getTime().toLocaleString()
				+ "\nEnd: " + gcEnd.getTime().toLocaleString());
		if (desc != null)
			if (desc.trim().equalsIgnoreCase("") == false)
				tv.setText(tv.getText() + "\n\n" + desc);

		if (pin.getIsOwner())
			tv.setText(tv.getText() + "\n\nYou are the owner!");

		eventDetailsPane_.setVisibility(View.VISIBLE);
		eventMap_.invalidate();

	}

	/** Handles menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENUITEM_NEW_EVENT:
			Intent submitEvent = new Intent(this, SubmitEvent.class);
			startActivity(submitEvent);
			map_.refreshOverlays();
			map_.postInvalidate();
			break;
		case MENUITEM_FILTER_POS:
			Intent pos = new Intent(this, PositionActivity.class);
			startActivityForResult(pos, REQUEST_POSITION_FILTER);
			break;
		case MENUITEM_FILTER_TAG:

			break;
		case MENUITEM_FILTER_TIME:
			Intent tf = new Intent(this, TimeActivity.class);
			startActivity(tf);
			break;
		case MENUITEM_VIEW_LIST:
			Intent vl = new Intent(this, EventListActivity.class);
			startActivity(vl);
			break;
		case MENUITEM_MAP_NORM:
			map_.setSatellite(false);
			map_.setStreetView(false);
			break;
		case MENUITEM_MAP_SATELLITE:
			map_.setSatellite(true);
			map_.setStreetView(false);
			break;
		case MENUITEM_MAP_STREET:
			map_.setSatellite(false);
			map_.setTraffic(false);
			map_.setStreetView(true);
			break;
		case MENUITEM_MANUAL_UPDATE:
			final DBAdapter adapter = new DBAdapter(getApplicationContext());
			final String androidID = Constants
					.getAndroidID(getApplicationContext());
			Thread t = new Thread(new Runnable() {
				public void run() {
					EventLoader.manualUpdate(adapter, androidID);
				}
			});
			t.setDaemon(true);
			t.setName("EventsRequestor");
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();

			break;
		case MENUITEM_ABOUT:
			Intent ab = new Intent(this, About.class);
			startActivity(ab);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @seeedu.vanderbilt.vuphone.android.events.eventloader.LoadingListener#
	 * OnEventLoadStateChanged
	 * (edu.vanderbilt.vuphone.android.events.eventloader.
	 * LoadingListener.LoadState)
	 */
	public void OnEventLoadStateChanged(LoadState l, Long rowId) {
		if (uiHandler_ == null)
			return;

		switch (l) {
		case STARTED:
			uiHandler_.post(new Runnable() {

				public void run() {
					Toast.makeText(EventViewer.this, "Updating Events",
							Toast.LENGTH_SHORT).show();
				}

			});
			break;
		case FINISHED:
			uiHandler_.post(new Runnable() {

				public void run() {
					Toast.makeText(EventViewer.this, "Done Updating Events",
							Toast.LENGTH_SHORT).show();
				}

			});

			break;
		case FINISHED_WITH_ERROR:
			uiHandler_.post(new Runnable() {

				public void run() {
					Toast.makeText(EventViewer.this,
							"Error Updating. Try again later",
							Toast.LENGTH_LONG).show();
				}

			});

			break;

		}
	}
}
