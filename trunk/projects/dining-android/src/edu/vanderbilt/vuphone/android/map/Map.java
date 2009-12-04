package edu.vanderbilt.vuphone.android.map;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.dining.R.layout;
import edu.vanderbilt.vuphone.android.dining.R.map;

/**
 * Creates the map that displays the location of one dining facilities
 * 
 * @author Peter
 */
public class Map extends MapActivity {

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		MapView mapView;
		DiningLocationOverlay diningOverlay;

		Bundle extras = getIntent().getExtras();
		int longitude = extras.getInt("longitude");
		int latitude = extras.getInt("latitude");
		String location = extras.getString("location");

		// start map view and enable zoom controls
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.map.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);

		mapView.getController().setZoom(18);
		GeoPoint point = new GeoPoint(longitude, latitude);
		mapView.getController().setCenter(point);

		// add the icons for dining locations
		diningOverlay = new DiningLocationOverlay(this, longitude, latitude,
				location);

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
