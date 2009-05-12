package edu.vanderbilt.isis.vuphone;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

class DebugTable{
	public TextView mode, screenPos, geoPos, pins;
}

public class Map extends MapActivity {

	private final int MENU_ADD_POINT = 0;
	private final int MENU_QUIT = 1;
	
	private int MODE_NAVIGATE = 0;
	private int MODE_PIN = 1;
	
	private int MODE = MODE_NAVIGATE;
	
	private MapView mapView;
	private DebugTable debug;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapview);
        
        mapView.setBuiltInZoomControls(true);
        
        debug = new DebugTable();
        
        debug.mode = (TextView) findViewById(R.id.mode);
        debug.screenPos = (TextView) findViewById(R.id.screen_pos);
        debug.geoPos = (TextView) findViewById(R.id.geo_pos);
        debug.pins = (TextView) findViewById(R.id.pins);
        
        debug.mode.setText("Mode: " + MODE);
        
        debug.mode.setText("" + mapView.getTop());
    }
    
    // THE CALCULATED GEO POINTS ARE ALWAYS OFF BY THE SAME AMOUNT.
    // SCREEN POS NEEDS TO BE ADJUSTED TO MAPVIEW
    public boolean dispatchTouchEvent(MotionEvent event){
    
    	float x = event.getX();// - mapView.getLeft();
    	float y = event.getY();// - mapView.getTop();
    	
    	int offset[] = new int[2];
    	mapView.getLocationOnScreen(offset);
    	x -= (float) offset[0];
    	y -= (float) offset[1];
    	    	
    	Projection proj = mapView.getProjection();
    	GeoPoint pt = proj.fromPixels((int) x, (int) y);
    	
    	String scr = "Screen: " + x + ", " + y;
    	String geo = "Geo: " + pt.toString();
    	
    	debug.screenPos.setText(scr);
    	debug.geoPos.setText(geo);
    	
    	if (MODE == MODE_PIN){
    		addPoint(pt);
    		MODE = 10;
    		return true;
    	}else{    	
    		return super.dispatchTouchEvent(event);
    	}
    }
    
    public void addPoint(GeoPoint point){
    	OverlayItem item = new OverlayItem(point, "", "");
		MapOverlay overlay = new MapOverlay(getResources().getDrawable(R.drawable.mapmarker));
		overlay.addOverlay(item);
		
		mapView.getOverlays().add(overlay);
		
		String pinsNum = "" + mapView.getOverlays().size();
		debug.pins.setText(pinsNum);
		mapView.postInvalidate();
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
    		MODE = MODE_PIN;
    		debug.mode.setText("Mode: " + MODE);
    		return true;
    	case MENU_QUIT:
    		return true;
    	}
    	
    	return true;
    }
}