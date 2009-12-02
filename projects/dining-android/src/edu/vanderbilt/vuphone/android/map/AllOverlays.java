package edu.vanderbilt.vuphone.android.map;

import java.util.ArrayList;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.dining.R;

/**
 * Creates an array of overlay items that are then added to the map view
 * 
 * @author Peter
 */
public class AllOverlays extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();

	public AllOverlays(AllLocations map, int[] locationLongitudes,
			int[] locationLatitudes, String[] restaurants) {
		super(new ShapeDrawable(new OvalShape()));

		for (int x = 0; x < locationLatitudes.length; ++x) {
			GeoPoint point = new GeoPoint(locationLongitudes[x],
					locationLatitudes[x]);
			OverlayItem overlayItem = new OverlayItem(point, restaurants[x], "");
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
