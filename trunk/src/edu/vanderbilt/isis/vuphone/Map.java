package edu.vanderbilt.isis.vuphone;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity {

	private final int MENU_ADD_POINT = 0;
	private final int MENU_QUIT = 1;
	
	private MapView mapView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView = new MapView(this,"0zz3AmUYDBIjlLBRuJlLd74N-jLfx2mDwJG4cDQ");
        mapView.setBuiltInZoomControls(true);
        
        GeoPoint point = new GeoPoint(19240000,-99120000);
        addPoint(point);
    }
    
    public boolean onTouchEvent(MotionEvent event){
    	return super.onTouchEvent(event);
    }
    
    public void addPoint(GeoPoint point){
    	OverlayItem item = new OverlayItem(point, "", "");
		MapOverlay overlay = new MapOverlay(getResources().getDrawable(R.drawable.mapmarker));
		overlay.addOverlay(item);
		
		mapView.getOverlays().add(overlay);
    }
    
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, MENU_ADD_POINT, 0, "Add Point");
        menu.add(0, MENU_QUIT, 0, "Quit");
    	
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case MENU_ADD_POINT:
    		return true;
    	case MENU_QUIT:
    		return true;
    	}
    	
    	return true;
    }
}