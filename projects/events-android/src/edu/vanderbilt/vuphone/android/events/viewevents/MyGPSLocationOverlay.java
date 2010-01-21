/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * @author Hamilton Turner
 * 
 */
public class MyGPSLocationOverlay extends MyLocationOverlay {

	/**
	 * @param context
	 * @param mapView
	 */
	public MyGPSLocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
	}

	/**
	 * Disables any location updates from the network. They are to broad for our
	 * application.
	 */
	@Override
	public void onLocationChanged(android.location.Location location) {
		if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
			if (false == "1".equals(System.getProperty("ro.kernel.qemu")))
				this.disableCompass();

			return;
		}

		// Are we in emulator or device?
		if (false == "1".equals(System.getProperty("ro.kernel.qemu")))
			this.enableCompass();

		super.onLocationChanged(location);
	}

}
