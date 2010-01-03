package edu.vanderbilt.vuphone.android.map;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.storage.Restaurant;

/**
 * Creates an array of overlay items that are then added to the map view
 * 
 * @author Peter
 */
public class AllOverlays extends ItemizedOverlay<OverlayItem> {
	
	
	private AllLocations map;
	private MapView mapView;
	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();
	private int clickedPosition = -1;

	public AllOverlays(AllLocations map, MapView mapview) {

		super(boundCenterBottom(map.getResources().getDrawable(R.drawable.map_marker)));
		this.map = map;
		this.mapView = mapview;
		
		ArrayList<Long> IDs = Restaurant.getIDs();

		for (int i = 0; i < IDs.size(); i++) {
			OverlayItem overlayItem = new OverlayItem(new GeoPoint(Restaurant.getLat(IDs.get(i)),
					Restaurant.getLon(IDs.get(i))), Restaurant.getName(IDs.get(i)), null);
			overlayItem.setMarker(getMarkerForItem(i, false));
			locationOverlay.add(overlayItem);
		} 
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		if (clickedPosition != -1)
			getItem(clickedPosition).setMarker(getMarkerForItem(clickedPosition, false));
		if (clickedPosition == index) {
			clickedPosition = -1;
			return true; //super.onTap(index);
		}
		clickedPosition = index;

		mapView.getController().animateTo(getItem(index).getPoint());
		getItem(index).setMarker(getMarkerForItem(index, true));
		return true; //super.onTap(index);
	}
	
	
	@Override
	protected OverlayItem createItem(int i) {
		return locationOverlay.get(i);
	}

	@Override
	public int size() {
		return locationOverlay.size();
	}
	
	// TODO make popup drawable for clicked==true, attempt to test for clicks on the popup
	private Drawable getMarkerForItem(int i, boolean clicked) {
		return boundCenterBottom(map.getResources().getDrawable(R.drawable.map_marker));
	}

}
