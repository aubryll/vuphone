package edu.vanderbilt.vuphone.android.campusmaps;

import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class Main extends MapActivity {
	
	LinearLayout linearLayout;
	MapView mapView;
	ZoomControls mZoom;
	MapController mc;
	GeoPoint p;
	
    /** Called when the activity is first created. */	
    @Override
    /**
     * Enables user to zoom in/out of the center of the screen.
     * 
     * Also sets the map to open while viewing Vanderbilt Campus.  
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        mc = mapView.getController();
        String coordinates[] = {"36.142830", "-86.804437"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
 
        p = new GeoPoint(
            (int) (lat * 1000000), 
            (int) (lng * 1000000));
 
        mc.animateTo(p);
        mc.setZoom(17); 
        mapView.invalidate();
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
		menu.add("Map Mode");
		return true;
	}
	
	
}