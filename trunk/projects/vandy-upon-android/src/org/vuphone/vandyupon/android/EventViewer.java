/**
 * 
 */
package org.vuphone.vandyupon.android;

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

	/** Called when the Activity is first created */
	@Override
	protected void onCreate(Bundle ice) {
		super.onCreate(ice);
		setContentView(R.layout.event_map);
	}

	/**
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
