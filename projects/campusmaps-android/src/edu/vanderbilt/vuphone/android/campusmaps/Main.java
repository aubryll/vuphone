package edu.vanderbilt.vuphone.android.campusmaps;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class Main extends MapActivity {

	private static final int MENU_DROP_PIN = 3;
	private static final int MENU_SHOW_BUILDINGS = 2;
	private static final int MENU_BUILDING_LIST = 1;
	private static final int MENU_MAP_MODE = 0;
	LinearLayout linearLayout_;
	MapView mapView_;
	ZoomControls mZoom_;
	MapController mc_;
	GeoPoint p_;
	

	/**
	 * Called when the activity is first created. Enables user to zoom in/out of
	 * the center of the screen. Also sets the map to open while viewing
	 * Vanderbilt Campus.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView_ = (MapView) findViewById(R.id.mapview);
		mapView_.setBuiltInZoomControls(true);

		mc_ = mapView_.getController();
		String coordinates[] = { "36.142830", "-86.804437" };
		// Vanderbilt GPS coordinates, used to start the map at a Vanderbilt
		// Location.
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);

		p_ = new GeoPoint((int) (lat * 1000000), (int) (lng * 1000000));

		mc_.animateTo(p_);
		mc_.setZoom(17);
		mapView_.invalidate();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Creates a "Map Mode" option when menu is clicked.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, MENU_MAP_MODE, "Map Mode");
		menu.add(0, 1, MENU_BUILDING_LIST, "List Buildings");
		menu.add(0, 2, MENU_SHOW_BUILDINGS, "Show Buildings");
		menu.add(0, 3, MENU_DROP_PIN, "Drop Pin");
		return true;
	}

	/**
	 * Called when an Menu item is clicked
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		super.onMenuItemSelected(featureId, item);

		switch (item.getItemId()) {
		
		case (MENU_MAP_MODE):
			echo("Map Mode");
			break;
		
		case (MENU_BUILDING_LIST):
			echo("Building list");
			break;
		
		case (MENU_SHOW_BUILDINGS):
			echo("Show Buildings");
			break;
		
		case (MENU_DROP_PIN):
			// TODO: Drop the pin at center-screen
			drop_pin(p_);
			break;
		}
		return true;
	}

	/**
	 * Used to set a marker image on the map
	 */
	public void drop_pin(GeoPoint p) {
		MapMarker m = new MapMarker(getBaseContext(), getResources(), mapView_, p);
		m.drop_pin();
	}

	/**
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}

}