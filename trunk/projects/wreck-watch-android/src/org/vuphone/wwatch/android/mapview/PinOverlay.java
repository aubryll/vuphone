package org.vuphone.wwatch.android.mapview;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.vuphone.wwatch.android.R;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

/**
 * Holds the OverlayItems (Waypoints, here) that should currently be rendered to
 * the map. A class can update those items, but cannot add or remove single
 * items for performance reasons.
 * 
 * Handles onTap events and display's the routes
 */
public class PinOverlay extends ItemizedOverlay<Waypoint> {
	public static enum OverlayState {
		SHOWING_ALL_WRECKS, SHOWING_ONE_WRECK
	};

	private List<Waypoint> points_ = new CopyOnWriteArrayList<Waypoint>();
	private AlertDialog dialog_ = null;
	private Waypoint lastOnTapPoint_ = new Waypoint(new GeoPoint(0, 0), System
			.currentTimeMillis());
	private OverlayState state_ = OverlayState.SHOWING_ALL_WRECKS;

	private static final String pre = "PinOverlay: ";
	private static final String tag = VUphone.tag;

	private MapView mapView_;
	private Context context_ = null;

	public PinOverlay(Context context, MapView mv) {
		super(new ShapeDrawable(new OvalShape()));

		mapView_ = mv;
		context_ = context;

		populate();

		LayoutInflater inflater = LayoutInflater.from(context_);
		View gallery = inflater.inflate(R.layout.wreck_details, null);

		dialog_ = new AlertDialog.Builder(this.context_).setView(gallery)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface di, int what) {
								di.dismiss();
							}
						}).create();
	}

	/**
	 * Allows this class to return items in any manner we would like, if we
	 * wanted to impose our own ordering on drawing. We do not, so we just
	 * return the item as we have them stored
	 */
	@Override
	protected Waypoint createItem(int i) {
		Log.i(tag, pre + "Create item " + i + " returning "
				+ points_.get(i).toString());
		return points_.get(i);
	}

	public OverlayState getState() {
		return state_;
	}
	
	/**
	 * Informs us a tap event occurred on item with index index. First tap
	 * display's that wrecks route, and second tap will pop up the gallery
	 * activity
	 */
	@Override
	protected boolean onTap(int index) {
		Log.d(tag, pre + "onTap called with index " + index);
		Waypoint point = points_.get(index);
		Log.d(tag, pre + "onTap waypoint: " + point);

		mapView_.getController().animateTo(points_.get(index).getPoint());

		// If this is the second tap, open dialog
		if (point.equals(lastOnTapPoint_)) {
			dialog_.show();
			return true;
		}
		
		// If not, show route
		state_ = OverlayState.SHOWING_ONE_WRECK;
		lastOnTapPoint_ = point;
		
		
		return true;
	}

	/**
	 * Get the number of OverlayItem objects currently in this Overlay.
	 * 
	 * @return number of items.
	 */
	@Override
	public int size() {
		Log.i(tag, pre + "Size called, returning " + points_.size());
		return points_.size();
	}

	/**
	 * Return a human readable representation of this object
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		int index = 0;
		String str = "PinGroup: ";
		for (Waypoint point : points_) {
			str += "[" + point.toString() + "] ";
			++index;
		}
		return str;
	}

	/**
	 * Replaces the List of Waypoints already displayed on the map with the new
	 * list. Note that updating the list of pins on the map is expensive, so it
	 * will always be better to perform larger pin updates, less frequently,
	 * than smaller, more frequent updates
	 * 
	 * @param points
	 *            the new list of Pins to be displayed
	 * 
	 */
	public void updatePins(final List<Waypoint> points) {
		// Give each point a context, so it can access resources and get its
		// drawable
		for (Waypoint wp : points)
			wp.setContext(context_);

		points_.clear();
		points_.addAll(points);
		populate();
	}

}
