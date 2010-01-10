package edu.vanderbilt.vuphone.android.map;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.storage.Restaurant;

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

            Bundle extras = getIntent().getExtras();

            // start map view and enable zoom controls
            setContentView(R.layout.map);
            MapView mapView = (MapView) findViewById(R.map.mapview);
            mapView.setBuiltInZoomControls(true);
            mapView.setClickable(true);

            if (extras.isEmpty()) {
            	mapView.getController().setCenter(AllLocations.CENTER);
            	mapView.getController().setZoom(AllLocations.ZOOM);
            	return;
            }

            Long restaurantID = extras.getLong(RESTAURANT_ID);
           
            mapView.getController().setZoom(ZOOM);
            GeoPoint point = new GeoPoint(Restaurant.getLat(restaurantID), Restaurant
                            .getLon(restaurantID));
            mapView.getController().setCenter(point);

            MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this,
                            mapView);
            myLocationOverlay.enableMyLocation();

            mapView.getOverlays().add(myLocationOverlay);
            mapView.getOverlays().add(new SingleOverlay(mapView, point/*, Restaurant.getName(restaurantID)*/));
        }

        @Override
        protected boolean isRouteDisplayed() {
                return false;
        }
}
