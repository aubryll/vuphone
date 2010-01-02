package edu.vanderbilt.vuphone.android.map;

import java.util.ArrayList;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.objects.RestaurantAdapter;
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

		ArrayList<Long> IDs = Restaurant.getIDs();

		for (int i = 0; i < IDs.size(); i++) {
			OverlayItem overlayItem = new OverlayItem(new GeoPoint(Restaurant.getLat(IDs.get(i)),
					Restaurant.getLon(IDs.get(i))), Restaurant.getName(IDs.get(i)), RestaurantAdapter.hoursText(IDs.get(i)));
			overlayItem.setMarker(boundCenterBottom(map.getResources()
					.getDrawable(Restaurant.getIcon(IDs.get(i)))));
			locationOverlay.add(overlayItem);
		} 
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
