package edu.vanderbilt.vuphone.android.map;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

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

	/**
	 * sets zoom so all dining locations and most of Vanderbilts campus are
	 * visible without panning
	 */
	public static final int ZOOM = 16;
	public static final GeoPoint CENTER = new GeoPoint(36143299, -86802464);

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);
		
		MapView mapView; 
		AllOverlays diningOverlay;

		// start map view and enable zoom controls
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.map.mapview);
		mapView.setBuiltInZoomControls(true);


		mapView.getController().setZoom(ZOOM);
		mapView.getController().setCenter(CENTER);
		
		// creates the overlay containing markers for all dining locations
		diningOverlay = new AllOverlays(this, mapView);
		
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
