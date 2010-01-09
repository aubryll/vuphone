package edu.vanderbilt.vuphone.android.map;

import java.util.ArrayList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.dining.RestaurantDetails;
import edu.vanderbilt.vuphone.android.objects.RestaurantAdapter;
import edu.vanderbilt.vuphone.android.storage.Restaurant;

/**
 * Creates an array of overlay items that are then added to the map view
 * 
 * @author Peter
 */
public class AllOverlays extends ItemizedOverlay<OverlayItem> implements View.OnClickListener {
	
	private static final int NUM_FILTERS = 3;
	public static final int FILTER_CLOSED = 0;
	public static final int FILTER_PLAN = 1;
	public static final int FILTER_MONEY = 2;
	
	private int clickedPosition = -1;
	private AllLocations map;
	private MapView mapView;
	private RelativeLayout popup;
	//private ImageView icon;
	private TextView popupText;
	private TextView specialText;
	
	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();
	private boolean [][] show; // will show an item only if every entry in the column is true

	public AllOverlays(AllLocations map, MapView mapview) {

		super(boundCenterBottom(map.getResources().getDrawable(R.drawable.map_marker)));
		this.map = map;
		this.mapView = mapview;
		popup = (RelativeLayout)mapView.findViewById(R.map.popup);
		//icon = (ImageView)popup.findViewById(R.map.icon);
		popupText = (TextView)popup.findViewById(R.map.title);
		specialText = (TextView)popup.findViewById(R.map.specialText);
		
		popup.setOnClickListener(this);
		
		ArrayList<Long> IDs = Restaurant.getIDs();
		show = new boolean [NUM_FILTERS][IDs.size()]; // only 1 possible criteria for showing now

		for (int i = 0; i < IDs.size(); i++) {
			OverlayItem overlayItem = new OverlayItem(new GeoPoint(Restaurant.getLat(IDs.get(i)),
					Restaurant.getLon(IDs.get(i))), Restaurant.getName(IDs.get(i)), RestaurantAdapter.hoursText(IDs.get(i)));
			if (Restaurant.offCampus(IDs.get(i)))
				overlayItem.setMarker(boundCenterBottom(map.getResources().getDrawable(R.drawable.map_marker_n)));
					// TODO get a better custom marker for off campus restaurants and/or make more custom markers for different 
					// types or individual restaurants
			else overlayItem.setMarker(boundCenterBottom(map.getResources().getDrawable(R.drawable.map_marker_v)));
			locationOverlay.add(overlayItem);
			for (int j = 0; j < NUM_FILTERS; j++)
				show[j][i] = true;
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

		//icon.setImageResource(Restaurant.getIcon(Restaurant.getIDs().get(index)));
		popupText.setText(getItem(index).getTitle());
		specialText.setText(getItem(index).getSnippet());
		
		popup.setLayoutParams(new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT, MapView.LayoutParams.WRAP_CONTENT, 
				getItem(index).getPoint(), 0, -getItem(index).getMarker(0).getIntrinsicHeight(), MapView.LayoutParams.BOTTOM_CENTER));
		popup.setVisibility(View.VISIBLE);
		
		mapView.getController().animateTo(getItem(index).getPoint());
		return true; //super.onTap(index);
	}
	
	private int lastI;
	private int lastIndex;
	@Override
	protected OverlayItem createItem(int i) {
		if (lastI == i - 1) {
			for (int j = lastIndex + 1; j < show[0].length; j++)
				if (getShowItem(j)) {
					lastI = i;
					lastIndex = j;
					return locationOverlay.get(j);
				}
		} else {
			int num = -1;
			for (int j = 0; j < show[0].length; j++) {
				if (getShowItem(j))
					num++;
				if (num == i) {
					lastI = i;
					lastIndex = j;
					return locationOverlay.get(j);
				}
			}
		}
		throw new RuntimeException("createItem error");
	}

	@Override
	public int size() {
		int size = 0;
		for (int i = 0; i < show[0].length; i++) 
			if (getShowItem(i))
				size++;
		return size;
	}


	public void onClick(View v) {
		Intent toDetails = new Intent(map, RestaurantDetails.class);
		toDetails.putExtra(RestaurantDetails.RESTAURANT_ID, Restaurant.getIDs().get(clickedPosition));
		map.startActivity(toDetails);
	}
	
	public ArrayList<OverlayItem> getLocationOverlay() {
		return locationOverlay;
	}
	
	public void setHideForFilter(boolean hide, int filter) {
		if (!hide) 
			for (int i = 0; i < show[0].length; i++)
				setShowItem(i, filter, true);
		else {
			switch (filter) {
			case FILTER_CLOSED:
				for (int i = 0; i < show[0].length; i++)
					if (!Restaurant.getHours(Restaurant.getIDs().get(i)).isOpen())
						setShowItem(i, filter, false);
				break;
			case FILTER_PLAN:
				for (int i = 0; i < show[0].length; i++)
					if (!Restaurant.mealPlanAccepted(Restaurant.getIDs().get(i)))
						setShowItem(i, filter, false);
				break;
			case FILTER_MONEY:
				for (int i = 0; i < show[0].length; i++)
					if (!Restaurant.mealMoneyAccepted(Restaurant.getIDs().get(i)))
						setShowItem(i, filter, false);
				break;
			}
		}
	}
	
	public void setShowItem(int i, int filter, boolean display) {
		show[filter][i] = display;
	}
	
	public boolean getShowItem(int i) {
		for (int j = 0; j<NUM_FILTERS; j++)
			if (!show[j][i])
				return false;
		return true;
	}
	
	public void notifyDataSetChanged() {
		populate();
	}

}
