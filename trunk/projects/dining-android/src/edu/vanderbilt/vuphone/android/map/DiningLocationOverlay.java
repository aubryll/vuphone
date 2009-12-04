package edu.vanderbilt.vuphone.android.map;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.dining.R.drawable;

/**
 * Creates an array of overlay for a particular restaurant to the map view
 * 
 * @author Peter
 */
public class DiningLocationOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();

	public DiningLocationOverlay(Map map, int longitude, int latitude,
			String location) {
		super(null);

		GeoPoint point = new GeoPoint(longitude, latitude);
		OverlayItem overlayItem = new OverlayItem(point, location, "");
		overlayItem.setMarker(boundCenterBottom(map.getResources().getDrawable(
				R.drawable.dining)));
		locationOverlay.add(overlayItem);
		populate();
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
