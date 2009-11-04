package edu.vanderbilt.vuphone.android.dining;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Creates the map that displays the location of all dining facilities
 * 
 * @author Peter
 */
public class Map extends MapActivity {

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		MapView mapView;
		List<Overlay> mapOverlays;
		Drawable drawable;
		DiningLocationOverlay overlay;

		Bundle extras = getIntent().getExtras();
		int[] longitudes = extras.getIntArray("longitudes");
		int[] latitudes = extras.getIntArray("latitudes");
		String[] locations = extras.getStringArray("locations");

		// start map view and enable zoom controls
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		// add the icons for dining locations
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.dining);
		overlay = new DiningLocationOverlay(drawable, longitudes, latitudes,
				locations);

		mapOverlays.add(overlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
