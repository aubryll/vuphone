package edu.vanderbilt.vuphone.android.map;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.dining.R;

/**
 * Creates the map that displays the location of all dining facilities
 * 
 * @author Peter
 */
public class AllLocations extends MapActivity {

	public static final String EXTRA_LONGITUDES = "longitudes";
	public static final String EXTRA_LATITUDES = "latitudes";
	public static final String EXTRA_LOCATIONS = "restaurants";
	/** sets zoom so all dining locations are visible without panning */
	public static final int ZOOM = 16;

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		Bundle extras = getIntent().getExtras();
		int[] longitudes = extras.getIntArray(EXTRA_LONGITUDES);
		int[] latitudes = extras.getIntArray(EXTRA_LATITUDES);
		String[] locations = extras.getStringArray(EXTRA_LOCATIONS);

		MapView mapView;
		AllOverlays diningOverlay;

		// start map view and enable zoom controls
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.map.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);

		mapView.getController().setZoom(ZOOM);
		// This GeoPoint is approximately the center of campus
		GeoPoint point = new GeoPoint(36143299, -86802464);
		mapView.getController().setCenter(point);

		// creates the overlay containing markers for all dining locations
		diningOverlay = new AllOverlays(this, longitudes, latitudes, locations);

		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
				mapView);
		myLocationOverlay.enableMyLocation();

		mapView.getOverlays().add(diningOverlay);
		mapView.getOverlays().add(myLocationOverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
