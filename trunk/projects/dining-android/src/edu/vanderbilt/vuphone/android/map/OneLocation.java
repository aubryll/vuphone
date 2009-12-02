package edu.vanderbilt.vuphone.android.map;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.dining.R;

/**
 * Creates the map that displays the location of one dining facilities
 * 
 * @author Peter
 */
public class OneLocation extends MapActivity {

	public static final String EXTRA_LONGITUDES = "longitude";
	public static final String EXTRA_LATITUDES = "latitudes";
	public static final String EXTRA_LOCATIONS = "locations";
	/**
	 * sets the zoom so that enough of surrounding campus is displayed for
	 * context but focus is kept on particular restaurant
	 */
	public static final int ZOOM = 18;

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		MapView mapView;
		SingleOverlay diningOverlay;

		Bundle extras = getIntent().getExtras();
		if (!extras.isEmpty()) {
			int longitude = extras.getInt(EXTRA_LONGITUDES);
			int latitude = extras.getInt(EXTRA_LATITUDES);
			String location = extras.getString(EXTRA_LOCATIONS);

			// start map view and enable zoom controls
			setContentView(R.layout.map);
			mapView = (MapView) findViewById(R.map.mapview);
			mapView.setBuiltInZoomControls(true);
			mapView.setClickable(true);

			mapView.getController().setZoom(ZOOM);
			GeoPoint point = new GeoPoint(longitude, latitude);
			mapView.getController().setCenter(point);

			// add the icons for dining locations
			diningOverlay = new SingleOverlay(this, point, location);

			MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
					mapView);
			myLocationOverlay.enableMyLocation();

			mapView.getOverlays().add(diningOverlay);
			mapView.getOverlays().add(myLocationOverlay);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
