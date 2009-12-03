package edu.vanderbilt.vuphone.android.dining;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * Creates the map that displays the location of all dining facilities
 * 
 * @author Peter
 */
public class MapAllResturaunts extends MapActivity {

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		MapView mapView;
		AllDiningLocationOverlay diningOverlay;

		// start map view and enable zoom controls
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.map.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);

		mapView.getController().setZoom(16);
		GeoPoint point = new GeoPoint(36143299, -86802464);
		mapView.getController().setCenter(point);

		// add the icons for dining locations
		diningOverlay = new AllDiningLocationOverlay(this);

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
