package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**Creates an array of overlay items that are then added to the map view*/
public class DiningLocationOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();

	public DiningLocationOverlay(Drawable defaultMarker, int[] longitudes, int[] latitudes, String[] locations) {
		super(boundCenterBottom(defaultMarker));
		
		for(int x = 0; x<latitudes.length;++x){
        	GeoPoint point = new GeoPoint(longitudes[x],latitudes[x]);
        	OverlayItem overlayItem = new OverlayItem(point, locations[x], "");
        	locationOverlay.add(overlayItem);
        	populate();
        }
	}

	@Override
	protected OverlayItem createItem(int i) {
		return locationOverlay.get(i);
	}

	@Override
	public int size() {
		return locationOverlay.size();
	}
	
}
