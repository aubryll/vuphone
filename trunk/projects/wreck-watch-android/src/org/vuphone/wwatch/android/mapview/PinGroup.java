package org.vuphone.wwatch.android.mapview;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * A wrapper class responsible for holding all the GeoPoint objects and
 * displaying them.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class PinGroup extends Overlay {
	private List<Waypoint> points_ = null;

	private long lastTime_ = 0;

	private long[] wreckTimes_ = null;

	private GeoPoint[] wrecks_ = null;

	private int numWrecks_ = 0;

	private static final String LOG_PREFIX = "PinGroup: ";

	private Context context_ = null;

	private Bitmap pinIcon_;

	/**
	 * Default constructor.
	 */
	public PinGroup(Bitmap icon) {
		points_ = new CopyOnWriteArrayList<Waypoint>();
		pinIcon_ = icon;
	}

	public PinGroup(Bitmap icon, Context context) {
		points_ = new CopyOnWriteArrayList<Waypoint>();
		pinIcon_ = icon;
		context_ = context;
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
	public void addPins(final List<Waypoint> points) {
		// Intersect lists
		points_.removeAll(points);
		points_.addAll(points);
	}

	/**
	 * Draws all the pins
	 * 
	 * @param canvas
	 *            The Canvas on which to draw
	 * @param mapView
	 *            The MapView that requested the draw
	 * @param shadow
	 *            Ignored in this implementation
	 */
	static final Paint paint = new Paint();
	static int radius = 5;
	static {
		paint.setColor(Color.RED);
		paint.setTextSize(15);
		paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
	}

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();

		Iterator<Waypoint> i = points_.iterator();
		while (i.hasNext()) {
			Point scrPt = projection.toPixels(i.next().getGeoPoint(), null);
			canvas.drawCircle(scrPt.x, scrPt.y, radius, paint);

		}

	}

	public boolean onTouchEvent(MotionEvent event, MapView view) {

		if (event.getAction() == MotionEvent.ACTION_DOWN && numWrecks_ > 0) {
			Log.d(VUphone.tag, LOG_PREFIX + "Touch detected at ("
					+ event.getX() + ", " + event.getY() + ").");

			// Figure out which point was touched.
			Projection projection = view.getProjection();
			for (GeoPoint wreck : wrecks_) {
				Point scrPt = projection.toPixels(wreck, null);
				float x = scrPt.x;
				float y = scrPt.y;
				int radius = 20;
				if (event.getX() > x - radius && event.getX() < x + radius
						&& event.getY() > y - radius
						&& event.getY() < y + radius) {
					Log.d(VUphone.tag, LOG_PREFIX + "Found the point "
							+ wreck.toString());

					// When the pin is clicked on, we will display the
					// GalleryActivity
					Intent i = new Intent(context_, GalleryActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i
							.putExtra(
									"org.vuphone.wwatch.android.mapping.GalleryActivity.point",
									"latitude=" + wreck.getLatitudeE6()
											+ "&longitude="
											+ wreck.getLongitudeE6());
					context_.startActivity(i);
					// view.getOverlays().add(new GalleryOverlay(context_));
					// view.postInvalidate();

					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes the last added point.
	 */
	public void removeLastPoint() {
		int size = points_.size();
		if (size > 0) {
			points_.remove(size - 1);
		}
	}

	/**
	 * Get the number of OverlPin objects in this group.
	 * 
	 * @return size of this group.
	 */
	public int size() {
		return points_.size();
	}

	/**
	 * Return a human readable representation of this object
	 * 
	 * @return
	 */
	public String toString() {
		int index = 0;
		String str = "PinGroup: ";
		for (Waypoint point : points_) {
			str += "[" + point.toString() + "] ";
			++index;
		}
		return str;
	}
}
