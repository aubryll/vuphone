package edu.vanderbilt.isis.vuphone;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class ZoneMapView extends MapView {

	private OverlayZone zoneOverlay_ = null;
	public Zone zone_ = null;

	public ZoneMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBuiltInZoomControls(true);
		setClickable(true);
	}

	public void addPinEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		int offset[] = new int[2];
		getLocationOnScreen(offset);
		x -= (float) offset[0];
		y -= (float) offset[1];

		Projection proj = getProjection();
		GeoPoint pt = proj.fromPixels((int) x, (int) y);

		addPin(pt);
	}

	private void addPin(GeoPoint pt) {
		if (zone_ == null) {
			Projection proj = getProjection();
			Log.v("VUPHONE", "getProjection returned " + proj.toString());
			zone_ = new Zone(proj);
			Log.v("VUPHONE", "Created Zone");
			zoneOverlay_ = new OverlayZone(zone_);
		}

		// TODO - show error message here
		if (zone_.addPoint(pt) == false)
			return;

		// Overlay should always be at index 0.
		if (getOverlays().size() == 0)
			getOverlays().add(zoneOverlay_);
		else
			getOverlays().set(0, zoneOverlay_);

		String name = "Point " + getOverlays().size();
		OverlayPin pin = new OverlayPin(pt, name);
		getOverlays().add(pin);

		postInvalidate();

	}
	
}
