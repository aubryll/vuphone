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

	private void addPinEvent(MotionEvent event) {
		if (ZoneMapController.getAddingPin() == false)
			return;

		ZoneMapController.setAddingPin(false);

		// TODO - What are we doing with the offset here? Are we ever using it?
		int offset[] = new int[2];
		getLocationOnScreen(offset);

		int x = (int) event.getX();
		int y = (int) event.getY();

		Projection proj = getProjection();
		GeoPoint pt = proj.fromPixels(x, y);

		boolean added = editZone_.getZone().addPoint(pt);

		if (added == false)
			return;
		
		String name = Integer.toString(pinGroup_.size());
		OverlayPin pin = new OverlayPin(pt, name);
		pinGroup_.addOverlayPin(pin);

		postInvalidate();
	}

	public Zone checkCollision(GeoPoint point) {
		ArrayList<OverlayZone> list = zoneGroup_.getZoneList();
		for (OverlayZone oZone : list) {
			Zone zone = oZone.getZone();
			if (zone.contains(point))
				return zone;
		}

		return null;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		// If we are editing, they can move around. If we are adding a pin, they
		// cannot
		if (ZoneMapController.getAddingPin()) {
			Log.v("VUPHONE", "In onTouchEvent, adding pin");
			this.addPinEvent(ev);
			return true;
		}

		
		// passes ev to all overlays and if nothing handles it, navigates
		return super.onTouchEvent(ev);
	}

	public void validateOverlayList() {
		List<Overlay> list = super.getOverlays();
		if (list.size() != 4 || list.get(0) != zoneGroup_
				|| list.get(1) != editZone_ || list.get(2) != pinGroup_
				|| list.get(3) != myLocation_)
			throw new RuntimeException("OverlayList has been corrupted!");
	}
	

	public void startEdit() {
		if (ZoneMapController.getEditingZone() == false) {
			ZoneMapController.setEditingZone(true);
			((Map) super.getContext()).setMessage("Editing a zone");

			Zone zone = new Zone(super.getProjection());
			OverlayZone overlay = new OverlayZone(zone);
			editZone_.set(overlay);
		}
	}

	/**
	 * Attempts to stop editing, and save the zone currently being edited.
	 * 
	 * @return true if the zone is valid, and was saved. False otherwise
	 */
	public boolean stopEdit() {
		if (ZoneMapController.getEditingZone()) {
			ZoneMapController.setEditingZone(false);
			boolean couldFinalize = editZone_.getZone().finalizePath();

			if (couldFinalize == false)
				return false;

			OverlayZone finishedZone = editZone_.remove();
			String name = "Zone " + (zoneGroup_.size() + 1);
			finishedZone.getZone().setName(name);
			zoneGroup_.addOverlayZone(finishedZone);

			((Map) super.getContext()).setMessage(name + " submitted");

			return true;
		}

		// If we are not editing, just tell them we are stopping
		return true;
	}
}
