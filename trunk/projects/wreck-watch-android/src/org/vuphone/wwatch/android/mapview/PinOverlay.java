package org.vuphone.wwatch.android.mapview;

import java.util.ArrayList;
import java.util.List;

import org.vuphone.wwatch.android.R;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
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

	private List<Waypoint> points_ = new ArrayList<Waypoint>();
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
		context_ = context;//.getApplicationContext();

		populate();
	}

	/**
	 * Allows this class to return items in any manner we would like, if we
	 * wanted to impose our own ordering on drawing. We do not, so we just
	 * return the item as we have them stored
	 */
	@Override
	protected Waypoint createItem(int i) {
		synchronized (points_) {
			Log.i(tag, pre + "Create item " + i + " returning "
					+ points_.get(i).toString());
			return points_.get(i);
		}
	}
	
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		try {
			super.draw(canvas, mapView, false);
		} catch (IndexOutOfBoundsException ie) {
			Log.w(tag, pre + "Index was out of bounds, ignoring");
		}
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
			 //dialog_.show();
			 final int id = point.getAccidentId();
				
			 AlertDialog dialog = new AlertDialog.Builder(context_)
				.setTitle("Wreck Image Options")
				.setItems(new String[]{"View Images", "Option 2", "Cancel"}, new OnClickListener() {
					public void onClick(DialogInterface d, int item) {
						Log.v(VUphone.tag, "Item Clicked " + item);
						switch (item) {
						case 0:
							new AccidentImageDialog(context_, id).show();
							break;
						case 1:
							break;
						case 2:
							d.dismiss();
							break;
						}
					}
				}).create();

			 dialog.show();
			 
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
		synchronized (points_) {
			return points_.size();
		}
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
		synchronized (points) {
			points_ = points;
			populate();
		}

	}

}
