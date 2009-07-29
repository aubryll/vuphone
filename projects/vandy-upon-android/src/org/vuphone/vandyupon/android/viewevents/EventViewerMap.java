/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.LocationManager;
import org.vuphone.vandyupon.android.filters.PositionFilter;
import org.vuphone.vandyupon.android.filters.TagsFilter;
import org.vuphone.vandyupon.android.filters.TimeFilter;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * Contains the View that draws the map, and contains any overlays added to the
 * map
 * 
 * @author Hamilton Turner
 * 
 */
public class EventViewerMap extends MapView {

	/** Used to draw the current location, accuracy, and compass */
	private MyLocationOverlay currentLocation_;

	/** Draws visuals about the position filter on the map */
	private PositionOverlay positionOverlay_;

	/** Draws the events to the map */
	private EventOverlay eventOverlay_;

	/** Handles to the current filters */
	private PositionFilter positionFilter_;
	private TimeFilter timeFilter_;
	private TagsFilter tagsFilter_;

	/**
	 * @param context
	 * @param attrs
	 */
	public EventViewerMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBuiltInZoomControls(true);

		getController().setCenter(LocationManager.vandyCenter_);
		getController().setZoom(15);
		currentLocation_ = new MyLocationOverlay(getContext(), this);
		getOverlays().add(currentLocation_);

		eventOverlay_ = new EventOverlay(null, null,
				null, context);
		getOverlays().add(eventOverlay_);

		positionOverlay_ = new PositionOverlay();
		getOverlays().add(positionOverlay_);
	}

	/** Used to turn off the various sensors */
	protected void disableMyLocation() {
		currentLocation_.disableCompass();
		currentLocation_.disableMyLocation();
	}

	/** Used to turn on the various sensors */
	protected void enableMyLocation() {
		currentLocation_.enableMyLocation();
		currentLocation_.enableCompass();
	}
	
	/** Request that all Overlays refresh */
	protected void refreshOverlays() {
		eventOverlay_.receiveNewFilters(positionFilter_, timeFilter_, tagsFilter_);
		positionOverlay_.setPositionFilter(positionFilter_);
	}

	protected void updatePositionFilter(PositionFilter filter) {
		// Turn off any current location threads
		if (positionFilter_ != null)
			positionFilter_.stop();
		positionFilter_ = filter;
		positionOverlay_.setPositionFilter(positionFilter_);
		eventOverlay_.receiveNewFilters(positionFilter_, null, null);
	}
}
