package edu.vanderbilt.isis.vuphone;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * A class that draws a zone onto the map. Zone information comes from the Zone
 * reference that is passed into the constructor. This class also intercepts and
 * processes touch events. If such an event occurs, the activity's edit routing
 * information dialog will be activated only if the ZoneMapController is
 * currently in selecting mode
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class OverlayZone extends Overlay {

	private Zone zone_;

	/**
	 * Default constructor.
	 * 
	 * @param zone
	 *            A reference to the zone that is to be displayed.
	 */
	public OverlayZone(Zone zone) {
		zone_ = zone;
	}

	/**
	 * Draw the overlay over the map.
	 * 
	 * @param canvas
	 *            The Canvas on which to draw
	 * @param mapView
	 *            The MapView that requested the draw
	 * @param shadow
	 *            Ignored in this implementation
	 */
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Path path = zone_.getPath();

		if (path == null)
			return;

		boolean isFinal = zone_.getFinalized();

		Paint paint = new Paint();
		if (isFinal == false)
			paint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(path, paint);

		if (isFinal) {
			Point center = zone_.getCenter();
			paint.setColor(Color.RED);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTextSize(12);
			canvas.drawText(zone_.getName(), center.x, center.y, paint);
		}

	}

	/**
	 * Returns a reference to the underlying Zone object
	 * 
	 * @return
	 */
	public Zone getZone() {
		return zone_;
	}

	/**
	 * Handles touch events. If we are currently selecting a zone, and this
	 * event originated by clicking on this zone, then a routing dialog will be
	 * activated. Else, the event will be propagated until handled by someone
	 * else.
	 * 
	 * @param event
	 *            The motion event.
	 * @param mapView
	 *            The MapView that originated the touch event.
	 * @return True if the event was handled by this function, and should not be
	 *         seen by anyone else. False otherwise
	 * 
	 */
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		Point pt = new Point((int) event.getX(), (int) event.getY());

		// If we are not selecting a zone, ignore the event
		if (ZoneMapController.getSelectingZone() == false)
			return false;

		if (zone_.contains(pt)) {

			ZoneMapController.setSelectingZone(false);

			((Map) mapView.getContext()).setMessage(zone_.getName()
					+ " was touched");

			// Get ready for the dialog
			ZoneManager.getInstance().setEditing(zone_);
			((Map) mapView.getContext()).showDialog(DialogConstants.ROUTING);

			Log.v("VUPHONE", "OverlayZone received a touch event, eating");
			return true;
		}
		Log.v("VUPHONE", "OverlayZone received a touch event, propagating");
		return false;
	}
}