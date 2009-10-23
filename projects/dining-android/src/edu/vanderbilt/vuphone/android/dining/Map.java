package edu.vanderbilt.vuphone.android.dining;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity{
	
	LinearLayout linearLayout;
	MapView mapView;
	ZoomControls mZoom;
	List<Overlay> mapOverlays;
	Drawable drawable;
	DiningLocationOverlay overlay;
	
	@Override
    public void onCreate(Bundle ice) {
        super.onCreate(ice);
        
        Bundle extras = getIntent().getExtras();
        int[] longitudes = extras.getIntArray("longitudes");
        int[] latitudes = extras.getIntArray("latitudes");
        String[] locations = extras.getStringArray("locations");
        
        setContentView(R.layout.map);
        
        //add zoom function
        linearLayout = (LinearLayout) findViewById(R.id.zoomview);
        mapView = (MapView) findViewById(R.id.mapview);
        mZoom = (ZoomControls) mapView.getZoomControls();
        linearLayout.addView(mZoom);
        
        //add the icons for dining locations
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.dining);
        overlay = new DiningLocationOverlay(drawable);
        
        for(int x = 0; x<latitudes.length;x++){
        	GeoPoint point = new GeoPoint(longitudes[x],latitudes[x]);
        	OverlayItem overlayitem = new OverlayItem(point, locations[x], "");
        	overlay.addOverlay(overlayitem);
        }
        mapOverlays.add(overlay);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
