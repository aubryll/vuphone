package edu.vanderbilt.vuphone.android.map;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.objects.Restaurant;

/**
 * Creates the map that displays the location of one dining facilities
 * 
 * @author Peter
 */
public class OneLocation extends MapActivity {

	public static final String RESTAURANT_ID = "RESTAURANT_ID";
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
			Long restaurantID = extras.getLong(RESTAURANT_ID);
			Restaurant restaurant = Restaurant.get(restaurantID);

			// start map view and enable zoom controls
			setContentView(R.layout.map);
			mapView = (MapView) findViewById(R.map.mapview);
			mapView.setBuiltInZoomControls(true);
			mapView.setClickable(true);

			mapView.getController().setZoom(ZOOM);
			GeoPoint point = new GeoPoint(restaurant.getLon(), restaurant
					.getLat());
			mapView.getController().setCenter(point);

			// add the icons for dining locations
			diningOverlay = new SingleOverlay(this, point, restaurant.getName());

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
