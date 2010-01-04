package edu.vanderbilt.vuphone.android.map;

import java.util.ArrayList;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.dining.RestaurantDetails;
import edu.vanderbilt.vuphone.android.storage.Restaurant;

/**
 * Creates an array of overlay items that are then added to the map view
 * 
 * @author Peter
 */
public class AllOverlays extends ItemizedOverlay<OverlayItem> implements View.OnClickListener {
	
	private int clickedPosition = -1;
	private AllLocations map;
	private MapView mapView;
	private RelativeLayout popup;
	private TextView popupText;
	
	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();

	public AllOverlays(AllLocations map, MapView mapview) {

		super(boundCenterBottom(map.getResources().getDrawable(R.drawable.map_marker)));
		this.map = map;
		this.mapView = mapview;
		popup = (RelativeLayout)mapView.findViewById(R.map.popup);
		popupText = (TextView)popup.findViewById(R.map.textButton);
		popup.setOnClickListener(this);
		
		ArrayList<Long> IDs = Restaurant.getIDs();

		for (int i = 0; i < IDs.size(); i++) {
			OverlayItem overlayItem = new OverlayItem(new GeoPoint(Restaurant.getLat(IDs.get(i)),
					Restaurant.getLon(IDs.get(i))), Restaurant.getName(IDs.get(i)), String.valueOf(IDs.get(i))); 
						// hackish way of storing the restaurant id inside the snippet text
			// TODO make custom markers for each restaurant
			if (Restaurant.offCampus(IDs.get(i)))
				overlayItem.setMarker(boundCenterBottom(map.getResources().getDrawable(R.drawable.map_marker)));
			else overlayItem.setMarker(boundCenterBottom(map.getResources().getDrawable(R.drawable.map_marker_v)));
			locationOverlay.add(overlayItem);
		} 
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		if (clickedPosition == index) {
			clickedPosition = -1;
			popup.setVisibility(View.GONE);
			return true; //super.onTap(index);
		}
		clickedPosition = index;
		popup.setLayoutParams(new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, 
				getItem(index).getPoint(), 0, -getItem(index).getMarker(0).getIntrinsicHeight(), MapView.LayoutParams.BOTTOM_CENTER));
		popup.setVisibility(View.VISIBLE);
		
		popupText.setText(getItem(index).getTitle());
		
		mapView.getController().animateTo(getItem(index).getPoint());
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

	@Override
	public void onClick(View v) {
		Intent toDetails = new Intent(map, RestaurantDetails.class);
		toDetails.putExtra(RestaurantDetails.RESTAURANT_ID, Long.valueOf(getItem(clickedPosition).getSnippet()));
		map.startActivity(toDetails);
	}
	

}
