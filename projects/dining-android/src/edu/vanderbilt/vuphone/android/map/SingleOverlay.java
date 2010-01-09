package edu.vanderbilt.vuphone.android.map;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.dining.R;

/**
 * Creates an array of overlay for a particular restaurant to the map view
 * 
 * @author Peter
 */
public class SingleOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();

	public SingleOverlay(OneLocation map, GeoPoint point, String location) {
		super(null);

		OverlayItem overlayItem = new OverlayItem(point, location, "");
		overlayItem.setMarker(boundCenterBottom(map.getResources().getDrawable(
				R.drawable.map_marker_v)));

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
