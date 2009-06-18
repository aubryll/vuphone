package org.vuphone.wwatch.android.mapview;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;

/**
 * A wrapper class responsible for holding all the GeoPoint objects and
 * displaying them.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class PinGroup extends ItemizedOverlay<Waypoint> {
	private List<Waypoint> points_ = null;

	private static final String LOG_PREFIX = "PinGroup: ";

	private Context context_ = null;

	public PinGroup(Context context) {
		super(new ShapeDrawable(new OvalShape()));

		points_ = new CopyOnWriteArrayList<Waypoint>();
		context_ = context;

		populate();
	}

	/**
	 * Intersects the List of Waypoints given with the List of Waypoints already
	 * displayed on the map. Note that updating the list of pins on the map is
	 * expensive, so it will always be better to perform larger pin updates,
	 * less frequently, than smaller, more frequent updates
	 * 
	 * @param points
	 *            the list of Pins to be displayed if they are not already
	 * 
	 */
	public void updatePins(final List<Waypoint> points) {
		// Intersect lists
		points_.clear();
		points_.addAll(points);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		Log.d(VUphone.tag, "onTap called with index " +  index);
		Log.d(VUphone.tag, "onTap called - waypoint "
				+ points_.get(index).toString());
		return false;
	}

	/**
	 * Uses the super's draw method, which automagically asks all the pins to
	 * draw themselves
	 * 
	 * @see com.google.android.maps.ItemizedOverlay#draw(Canvas, MapView,
	 *      boolean)
	 */
//	@Override
//	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
//		super.draw(canvas, mapView, shadow);
//	}

	// public boolean onTouchEvent(MotionEvent event, MapView view) {
	//
	// if (event.getAction() == MotionEvent.ACTION_DOWN && numWrecks_ > 0) {
	// Log.d(VUphone.tag, LOG_PREFIX + "Touch detected at ("
	// + event.getX() + ", " + event.getY() + ").");
	//
	// // Figure out which point was touched.
	// Projection projection = view.getProjection();
	// for (GeoPoint wreck : wrecks_) {
	// Point scrPt = projection.toPixels(wreck, null);
	// float x = scrPt.x;
	// float y = scrPt.y;
	// int radius = 20;
	// if (event.getX() > x - radius && event.getX() < x + radius
	// && event.getY() > y - radius
	// && event.getY() < y + radius) {
	// Log.d(VUphone.tag, LOG_PREFIX + "Found the point "
	// + wreck.toString());
	//
	// // When the pin is clicked on, we will display the
	// // GalleryActivity
	// Intent i = new Intent(context_, GalleryActivity.class);
	// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// i
	// .putExtra(
	// "org.vuphone.wwatch.android.mapping.GalleryActivity.point",
	// "latitude=" + wreck.getLatitudeE6()
	// + "&longitude="
	// + wreck.getLongitudeE6());
	// context_.startActivity(i);
	// // view.getOverlays().add(new GalleryOverlay(context_));
	// // view.postInvalidate();
	//
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	/**
	 * Get the number of OverlPin objects in this group.
	 * 
	 * @return size of this group.
	 */
	@Override
	public int size() {
		Log.i(VUphone.tag, "Size called, returning " + points_.size());
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

	@Override
	protected Waypoint createItem(int i) {
		Log.i(VUphone.tag, "Create item " + i + " returning "
				+ points_.get(i).toString());
		return points_.get(i);
	}
}
