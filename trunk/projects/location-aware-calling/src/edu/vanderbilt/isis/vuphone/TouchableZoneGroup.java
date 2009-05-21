package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
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

	private ArrayList<Zone> list_ = null;
	
	/**
	 * Default constructor.
	 */
	public TouchableZoneGroup(){
		list_ = new ArrayList<Zone>();
		
		Log.v("VUPHONE", "accessing DB");
		
		List<Zone> zones = ZoneDB.getInstance().getAllZones();
		list_.addAll(zones);
	}
	
	/**
	 * Adds a Zone object to this group. 
	 * @param zone	A zone which is to be drawn and to be responsive to touch events.
	 */
	public void addZone(Zone zone){
		list_.add(zone);
	}
	
    /**
     * Draws each Zone object in this group.
     *  
     * @param	canvas	The Canvas on which to draw
     * @param	mapView	The MapView that requested the draw
     * @param	shadow	Ignored in this implementation 
     */
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(4);
		
		for (Zone zone : list_){
			Path path = zone.getPath();
			int x = zone.getCenter().x;
			int y = zone.getCenter().y;
			
			canvas.drawPath(path, paint);
			canvas.drawText(zone.getName(), x, y, new Paint());
		}
	}
	
	/**
	 * Returns an ArrayList of the Zone objects stored in this class.
	 * @return
	 */
	public ArrayList<Zone> getZoneList(){
		return list_;
	}
	
	/**
	 * Handles a touch event based on the current state.
	 * 
	 * @param event
	 * @param mapView
	 */
	public boolean onTouchEvent(MotionEvent event, MapView mapView){
		// Only handle the event if we're trying to Pick a Zone
		if (!LogicController.isPickingZone() || event.getAction() != MotionEvent.ACTION_UP)
			return false;

		// Walk the list and see which zone was touched
		for (Zone zone : list_){
			Point pt = new Point((int) event.getX(), (int) event.getY());
			
			if (zone.contains(pt)){
				
				// Get ready for the dialog
				ZoneManager.getInstance().setEditing(zone);
				((Map) mapView.getContext()).showDialog(ProgramConstants.DIALOG_ROUTING);
				LogicController.setPickingZone(false);
				
				// The event was handled
				return true;
			}
		}

		// No zone handled the event so propagate it.
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