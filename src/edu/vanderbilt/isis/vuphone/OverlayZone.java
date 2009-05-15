package edu.vanderbilt.isis.vuphone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * A class that draws a zone onto the map. Zone information comes from the Zone reference that is
 * passed into the constructor. This class also intercepts and processes touch events. If such an
 * event occurs, the activity's edit routing information dialog will be activated.
 * @author Krzysztof Zienkiewicz
 *
 */
public class OverlayZone extends Overlay{

	private Zone zone_;	// Reference to the zone to be displayed
	
	/**
	 * Default constructor.
	 * @param zone	A reference to the zone that is to be displayed.
	 */
	public OverlayZone(Zone zone){
		zone_ = zone;
	}
	
    /**
     * Draw the overlay over the map. 
     * @param	canvas	The Canvas on which to draw
     * @param	mapView	The MapView that requested the draw
     * @param	shadow	Ignored in this implementation 
     */
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		Path path = zone_.getPath();
		
		if (path == null)
			return;
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(path, paint);
		
		/*
		Point center = zone_.getCenter();		
		paint.setColor(Color.RED);
		canvas.drawCircle(center.x,center.y, 10, new Paint());
		canvas.drawText(zone_.getName(), center.x, center.y - 10, paint);
		*/
	}
	
	/**
	 * Returns a reference to the underlying Zone object
	 * @return
	 */
	public Zone getZone(){
		return zone_;
	}

	/**
	 * Handles touch events. If this event originated by clicking on this zone then a routing dialog
	 * will be activated. Else, the event will be propagated until handled by someone else.
	 * 
	 * @param	event	The motion event.
	 * @param	mapView	The MapView that originated the touch event.
	 */
	public boolean onTouchEvent(MotionEvent event, MapView mapView){
		Point pt = new Point((int) event.getX(), (int) event.getY());

		if (zone_.contains(pt)){
			((Map) mapView.getContext()).setMessage(zone_.getName() + " was touched");
			
			// Get ready for the dialog
			ZoneManager.getInstance().setEditing(zone_);
			((Map) mapView.getContext()).showDialog(DialogConstants.ROUTING);
						
			return true;
		}		
		return false;
	}
}