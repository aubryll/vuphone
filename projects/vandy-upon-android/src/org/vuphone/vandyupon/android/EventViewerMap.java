/**
 * 
 */
package org.vuphone.vandyupon.android;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

/**
 * @author Hamilton Turner
 * 
 */
public class EventViewerMap extends MapView {
	
	/** Center of Vanderbilt */
	private static final GeoPoint locationCenter_ = new GeoPoint(
			(int) (36.143792 * 1E6), (int) (-86.803279 * 1E6));
	
	/**
	 * @param context
	 * @param attrs
	 */
	public EventViewerMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBuiltInZoomControls(true);
		
		
		getController().setCenter(locationCenter_);
		getController().setZoom(15);
	}
}
