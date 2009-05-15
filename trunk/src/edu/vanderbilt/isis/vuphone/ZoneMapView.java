package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class ZoneMapView extends MapView {

	public TouchableZoneGroup zoneGroup_ = null;
	public UntouchableZone editZone_ = null;
	public PinGroup pinGroup_ = null;
	public MyLocationOverlay myLocation_ = null;
	
	private GeoPoint lastPoint_ = null;
	
	private boolean editingZone_ = false;

	public ZoneMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setBuiltInZoomControls(true);
		super.setClickable(true);
		
		zoneGroup_ = new TouchableZoneGroup();
		editZone_ = new UntouchableZone();
		pinGroup_ = new PinGroup();
		myLocation_ = new MyLocationOverlay(context);
		
		List<Overlay> list = super.getOverlays();
		list.add(zoneGroup_);
		list.add(editZone_);
		list.add(pinGroup_);
		list.add(myLocation_);
	}

	public void addPinEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();

		int offset[] = new int[2];
		getLocationOnScreen(offset);

		Projection proj = getProjection();
		GeoPoint pt = proj.fromPixels((int) x, (int) y);
		
		// Guard against multiple event firing for the same point
		if (pt.equals(lastPoint_))
			return;
		
		lastPoint_ = pt;
				
		addPin(pt);
	}

	private void addPin(GeoPoint pt) {
		Zone currentZone = editZone_.getZone();
		if (currentZone.contains(pt)){
			//Toast.makeText(getContext(), "This point is invalid", Toast.LENGTH_SHORT).show();
		}else{
			//Log.v("VUPHONE", "Adding point to current zone");
			currentZone.addPoint(pt);
			// TODO - RESOLVE EDITING ZONE PROBLEM
			String name = "Point " + pinGroup_.size();
			OverlayPin pin = new OverlayPin(pt, name);
			//Log.v("VUPHONE", "Adding pin to pinGroup");
			pinGroup_.addOverlayPin(pin);
		}
		
		postInvalidate();
	}

	public Zone checkCollision(GeoPoint point){
		ArrayList<OverlayZone> list = zoneGroup_.getZoneList();
		for (OverlayZone oZone : list){
			Zone zone = oZone.getZone();
			if (zone.contains(point))
				return zone;
		}
		
		return null;
	}
	
	public boolean onTouchEvent(MotionEvent ev){
		// If we're editing, stop normal dispatch (ie, no navigation)
		// and handle all touches as adding pins.
		if (this.isEditing()){
			
			this.addPinEvent(ev);
			return true;
		}
		
		// passes ev to all overlays and if nothing handles it, navigates 
		return super.onTouchEvent(ev);
	}
	
	public void validateOverlayList(){
		List<Overlay> list = super.getOverlays();
		if (list.size() != 4 || list.get(0) != zoneGroup_ ||
			list.get(1) != editZone_ || list.get(2) != pinGroup_ ||
			list.get(3) != myLocation_)
			throw new RuntimeException("OverlayList has been corrupted!");
	}
	
	public boolean isEditing(){
		return editingZone_;
	}
	
	public void startEdit(){
		if (!editingZone_){
			editingZone_ = true;
			((Map) super.getContext()).setMessage("Editing a zone");
			
			Zone zone = new Zone(super.getProjection());
			OverlayZone overlay = new OverlayZone(zone);
			editZone_.set(overlay);
		}
	}
	
	public void stopEdit(){
		if (editingZone_){
			editingZone_ = false;
			editZone_.getZone().finalize();
			
			OverlayZone finishedZone = editZone_.remove();
			String name = "Zone " + (zoneGroup_.size() + 1);
			finishedZone.getZone().setName(name);
			zoneGroup_.addOverlayZone(finishedZone);			
			
			((Map) super.getContext()).setMessage(name + " submitted");
		}
	}
}


