package edu.vanderbilt.isis.vuphone;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import edu.vanderbilt.isis.R;

public class ZoneMapView extends MapView {

	private TouchableZoneGroup zoneGroup_ = null;
	private UntouchableZone editZone_ = null;
	private PinGroup pinGroup_ = null;
	private MyLocationOverlay myLocation_ = null;

	
	public ZoneMapView(Context context, AttributeSet attrs){
		super(context, attrs);
		super.setBuiltInZoomControls(true);
		super.setClickable(true);
		super.setSatellite(false);
		
		// Must create the DB before the mapView tries to access it!
		ZoneDB db = new ZoneDB(getContext());
		db.open();
		db.useProjection(getProjection());
		
        Log.v("VUPHONE", "created DB");
        
		
		zoneGroup_ = new TouchableZoneGroup();
		editZone_ = new UntouchableZone();
		pinGroup_ = new PinGroup();
		myLocation_ = new MyLocationOverlay(context);
		
		List<Overlay> list = super.getOverlays();
		list.add(zoneGroup_);
		list.add(editZone_);
		list.add(pinGroup_);
		list.add(myLocation_);
		
		// We just added all the initial zones, so redraw
		invalidate();
	}

	public int numberTouchableZones(){
		return zoneGroup_.size();
	}
	
	public void addPinEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();

		Projection proj = getProjection();
		GeoPoint pt = proj.fromPixels((int) x, (int) y);
		
		addPin(pt);
	}

	private void addPin(GeoPoint pt){
		Zone currentZone = editZone_.getZone();
		if (currentZone.addPoint(pt)){
			pinGroup_.addPin(pt);
			this.postInvalidate();
		}else{
			Toast.makeText(getContext(), "This point is invalid", Toast.LENGTH_SHORT).show();
		}
	}

	public Zone checkCollision(GeoPoint point){
		ArrayList<Zone> list = zoneGroup_.getZoneList();
		for (Zone zone : list){
		if (zone.contains(point))
			return zone;
		}
		
		return null;
	}
	
	public boolean onTouchEvent(MotionEvent ev){
		if (LogicController.isAddingPin() && ev.getAction() == MotionEvent.ACTION_UP){
			addPinEvent(ev);
			LogicController.setAddingPin(false);
			return true;
		}
		
		// Let the dispatch thread pass this event to all overlays and let them
		// respond to the events based on different states.
		
		// The order of even dispatch is zoneGroup_, editZone_, pinGroup_, myLocation_
		// followed by the MapView itself.
		
		return super.onTouchEvent(ev);
	}
	
	public void validateOverlayList(){
		List<Overlay> list = super.getOverlays();
		if (list.size() != 4 || list.get(0) != zoneGroup_ ||
			list.get(1) != editZone_ || list.get(2) != pinGroup_ ||
			list.get(3) != myLocation_)
			throw new RuntimeException("OverlayList has been corrupted!");
	}
	
	/**
	 * This should only be called if LogicController.isAddingZone() returns false
	 */
	public void startEdit(){
		LogicController.setAddingZone(true);
		Toast.makeText(super.getContext(), "Add pins and click Submit Zone to finish", Toast.LENGTH_SHORT)
			.show();
		
		Zone zone = new Zone(super.getProjection());
		editZone_.set(zone);
	}

	/**
	 * This should only be called if LogicController.isAddingZone() returns true
	 */
	public boolean stopEdit(){
		
		boolean wasFinalized = editZone_.getZone().finalizePath();
		if (wasFinalized == false)
			return false;
		
		Zone finishedZone = editZone_.remove();
		if (finishedZone.getSize() == 0){
			return false;
		}
		
		LogicController.setAddingZone(false);
		
		String name = "Zone " + (zoneGroup_.size() + 1);
		finishedZone.setName(name);
		zoneGroup_.addZone(finishedZone);
		
		ZoneDB.getInstance().createZone(name, finishedZone.getPoints());
		
		((Map) super.getContext()).message(name + " submitted", false);
		
		// The path has been updated
		invalidate();
		return true;
	}
	
	public class ButtonBarListener implements View.OnClickListener{
		private View removeAll_ = null;
		private View removeLast_ = null;
		private View addPin_ = null;
		
		public ButtonBarListener(View root){
			removeAll_ = root.findViewById(R.id.remove_all_pins);
			removeAll_.setOnClickListener(this);
			
			removeLast_ = root.findViewById(R.id.remove_last_pin);
			removeLast_.setOnClickListener(this);
			
			addPin_ = root.findViewById(R.id.add_pin);
			addPin_.setOnClickListener(this);
		}
		
		public void onClick(View v){
			// If this has occurred then we are already in adding mode.
			if (v == removeAll_){
				Zone zone = ZoneMapView.this.editZone_.getZone();
				while (zone.getSize() > 0){
					if (zone.removeLastPoint())
						ZoneMapView.this.pinGroup_.removeLastPoint();
				}
				
				invalidate();
			}else if (v == removeLast_){
				// Zone will handle cases where it is empty.
				if (ZoneMapView.this.editZone_.getZone().removeLastPoint())
					ZoneMapView.this.pinGroup_.removeLastPoint();
				invalidate();
			}else if (v == addPin_){
				LogicController.setAddingPin(true);
			}
		}
	}
}


