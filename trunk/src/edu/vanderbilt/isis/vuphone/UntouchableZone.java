package edu.vanderbilt.isis.vuphone;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * A class that wraps around an OverlayZone. An object of this class is considered to be
 * a zone which is currently being built. Thus, this zone does not respond to touch events.
 * 
 * @author Krzysztof Zienkiewcz
 *
 */
public class UntouchableZone extends Overlay{

	OverlayZone zone_ = null;
	
	/**
	 * Default constructor;
	 */
	public UntouchableZone(){
		
	}
	
	/**
	 * Sets the zone contained by this object.
	 */
	public UntouchableZone(OverlayZone zone){
		zone_ = zone;
	}
	
    /**
     * Draws the zone.
     *  
     * @param	canvas	The Canvas on which to draw
     * @param	mapView	The MapView that requested the draw
     * @param	shadow	Ignored in this implementation 
     */
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		if (zone_ != null)
			zone_.draw(canvas, mapView, shadow);
	}
	
	/**
	 * Returns a reference to the underlying Zone object
	 * @return
	 */
	public Zone getZone(){
		return zone_.getZone();
	}
	
	/**
	 * Ignores the touch event.
	 * @param	event
	 * @param	mapView
	 */
	public boolean onTouchEvent(MotionEvent event, MapView mapView){
		// We don't care about the event so propagate it.
		return false;
	}
	
	/**
	 * Returns the OverlayZone held by this object and removes it from this object.
	 * Should be used once this zone's construction is finished.
	 * 
	 * @return
	 */
	public OverlayZone remove(){
		OverlayZone copy = zone_;
		zone_ = null;
		return copy;
	}
	
	/**
	 * If this object does not contain a valid zone, this method will 
	 * set it and return true. Else it will return false. This should be used
	 * either after default construction or after a call to remove.
	 * 
	 * @param zone	zone to be set.
	 * @return		the success of this operation.	
	 */
	public boolean set(OverlayZone zone){
		if (zone_ != null)
			return false;
		
		zone_ = zone;
		return true;
	}
}
