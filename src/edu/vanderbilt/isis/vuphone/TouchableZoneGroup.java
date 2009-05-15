package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


/**
 * A wrapper class that contains all zones whose construction is finished and so should be 
 * responsive to touch events.
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class TouchableZoneGroup extends Overlay{

	private ArrayList<OverlayZone> list_ = null;
	
	/**
	 * Default constructor.
	 */
	public TouchableZoneGroup(){
		list_ = new ArrayList<OverlayZone>();
	}
	
	/**
	 * Adds an OverlayZone object to this group. 
	 * @param zone	An overlay which is to be drawn and to be responsive to touch events.
	 */
	public void addOverlayZone(OverlayZone zone){
		list_.add(zone);
	}
	
    /**
     * Draws each OverlayZone object in this group.
     *  
     * @param	canvas	The Canvas on which to draw
     * @param	mapView	The MapView that requested the draw
     * @param	shadow	Ignored in this implementation 
     */
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		for (OverlayZone zone : list_){
			zone.draw(canvas, mapView, shadow);
		}
	}
	
	/**
	 * Returns an ArrayList of the OverlayZone objects stored in this class.
	 * @return
	 */
	public ArrayList<OverlayZone> getZoneList(){
		return list_;
	}
	
	/**
	 * Handles a touch event by dispatching it to the underlaying OverlayZone objects.
	 * 
	 * @param event
	 * @param mapView
	 */
	public boolean onTouchEvent(MotionEvent event, MapView mapView){		
		for (OverlayZone zoneOverlay : list_){
			// Pass the event down to each zone until one handles it
			if (zoneOverlay.onTouchEvent(event, mapView)){
				return true;
			}
		}
		
		// No zone handled the even so propagate it.
		return false;
	}
	
	/**
	 * Get the number of objects in this group.
	 * @return
	 */
	public int size(){
		return list_.size();
	}
	
}