package edu.vanderbilt.vuphone.android.map;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.storage.Restaurant;

/**
 * Creates the map that displays the location of one dining facilities
 * 
 * @author Peter
 */
public class OneLocation extends MapView {

	public static final String RESTAURANT_ID = "RESTAURANT_ID";

	/**
	 * sets the zoom so that enough of surrounding campus is displayed for
	 * context but focus is kept on particular restaurant
	 */
	public static final int ZOOM = 18;

	public OneLocation(Context c, AttributeSet attset) {
		super(c, attset);
		
		// start map view and enable zoom controls
		setBuiltInZoomControls(true);
		setClickable(true);

		getController().setZoom(ZOOM);
		
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
				getContext(), this);
		myLocationOverlay.enableMyLocation();
		
		getOverlays().add(myLocationOverlay);
	}
	
	public void setRestaurant(long restaurantID)
	{
		Restaurant restaurant = Restaurant.get(restaurantID);
		SingleOverlay diningOverlay;

		GeoPoint point = new GeoPoint(restaurant.getLat(), restaurant
				.getLon());
		getController().animateTo(point);

		// add the icons for dining locations
		diningOverlay = new SingleOverlay(this, point, restaurant.getName());

		getOverlays().add(diningOverlay);
		
		requestLayout();
	}
}
