package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Creates an array of overlay items that are then added to the map view
 * 
 * @author Peter
 */
public class AllDiningLocationOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();

	public AllDiningLocationOverlay(MapAllResturaunts map) {
		super(null);

		for (int x = 0; x < Main.LOCATION_LATITUDES.length; ++x) {
			GeoPoint point = new GeoPoint(Main.LOCATION_LONGITUDES[x],
					Main.LOCATION_LATITUDES[x]);
			OverlayItem overlayItem = new OverlayItem(point,
					Main.RESTAURANTS[x], "");
			overlayItem.setMarker(boundCenterBottom(map.getResources()
					.getDrawable(R.drawable.dining)));
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
