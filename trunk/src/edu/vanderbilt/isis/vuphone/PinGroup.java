package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * A wrapper class responsible for holding all the OverlayPin objects and displaying them.
 * @author Krzysztof Zienkiewicz
 *
 */
public class PinGroup extends Overlay{
	
	private ArrayList<OverlayPin> list_ = null;
	
	/**
	 * Default constructor.
	 */
	public PinGroup(){
		list_ = new ArrayList<OverlayPin>();
	}
	
	/**
	 * Adds the OverlayPin to a list of pins to be drawn.
	 * @param pin
	 */
	public void addOverlayPin(OverlayPin pin){
		list_.add(pin);
	}
	
    /**
     * Draws all the pins by calling draw() on each object stored in this wrapper.
     *  
     * @param	canvas	The Canvas on which to draw
     * @param	mapView	The MapView that requested the draw
     * @param	shadow	Ignored in this implementation 
     */
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		for (OverlayPin pin : list_){
			pin.draw(canvas, mapView);
		}
	}
	
	// TODO - handle pin touches.
	public boolean onTouchEvent(MotionEvent event, MapView view){
		// For now don't do anything.
		return false;	// propagete event. CHANGE THIS!!!!!!
	}
	
	/**
	 * Get the number of OverlPin objects in this group.
	 * @return	size of this group.
	 */
	public int size(){
		return list_.size();
	}
	
	/**
	 * Return a human readable representation of this object
	 * 
	 * @return
	 */
	public String toString(){
		return "PinGroup: " + list_.toString();
	}
}
