package edu.vanderbilt.vuphone.android.map;

import java.util.ArrayList;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.storage.Restaurant;

/**
 * Creates an array of overlay items that are then added to the map view
 * 
 * @author Peter
 */
public class AllOverlays extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();

	public AllOverlays(AllLocations map) {

		super(new ShapeDrawable(new OvalShape()));

		ArrayList<Long> restaurantIDs = Restaurant.getIDs();
		int[] locationLatitudes = new int[restaurantIDs.size()], locationLongitudes = new int[restaurantIDs
				.size()];
		String[] restaurants = new String[restaurantIDs.size()];

		for (int x = 0; x < restaurantIDs.size(); ++x) {
			locationLatitudes[x] = Restaurant.getLat(restaurantIDs.get(x));
			locationLongitudes[x] = Restaurant.getLon(restaurantIDs.get(x));
			restaurants[x] = Restaurant.getName(restaurantIDs.get(x));
		}

		for (int x = 0; x < locationLatitudes.length; ++x) {
			GeoPoint point = new GeoPoint(locationLongitudes[x],
					locationLatitudes[x]);
			OverlayItem overlayItem = new OverlayItem(point, restaurants[x], "");
			overlayItem.setMarker(boundCenterBottom(map.getResources()
					.getDrawable(Restaurant.getIcon(restaurantIDs.get(x)))));
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
