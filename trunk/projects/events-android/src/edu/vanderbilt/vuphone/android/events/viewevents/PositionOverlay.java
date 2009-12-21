/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.viewevents;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import edu.vanderbilt.vuphone.android.events.filters.PositionFilter;

/**
 * Draws a circle on the map representing the current position filter
 * 
 * @author Hamilton Turner
 * 
 */
public class PositionOverlay extends Overlay {
	private PositionFilter filter_ = null;

	// We only want to create these once
	private Point topRight_ = new Point(0, 0);
	private Point bottomLeft_ = new Point(0, 0);
	private RectF oval = new RectF();
	private static final Paint paint = new Paint();
	private static final Paint whitePaint = new Paint();

	static {
		// Color is greenish
		paint.setStrokeWidth(4);
		paint.setStyle(Paint.Style.STROKE);
		paint.setARGB(255, 50, 205, 50);
		paint.setAntiAlias(true);
		
		whitePaint.setStrokeWidth(1);
		whitePaint.setStyle(Paint.Style.STROKE);
		whitePaint.setAntiAlias(true);
		whitePaint.setARGB(255, 255, 255, 255);
	}

	protected void setPositionFilter(PositionFilter f) {
		filter_ = f;
	}

	/** Called when this overlay is rendered */
	@Override
	public void draw(Canvas c, MapView mapView, boolean shadow) {
		if (filter_ == null)
			return;

		Projection p = mapView.getProjection();
		GeoPoint tr = filter_.getTopRight();
		GeoPoint bl = filter_.getBottomLeft();
		
		
		topRight_ = p.toPixels(tr, null);
		bottomLeft_ = p.toPixels(bl, null);
		oval.set(bottomLeft_.x, topRight_.y, topRight_.x, bottomLeft_.y);
		c.drawArc(oval, 0, 360, false, paint);
		
		oval.set(bottomLeft_.x - 1, topRight_.y - 1, topRight_.x + 1, bottomLeft_.y + 1);
		c.drawArc(oval, 0, 360, false, whitePaint);
		
		oval.set(bottomLeft_.x + 1, topRight_.y + 1, topRight_.x - 1, bottomLeft_.y - 1);
		c.drawArc(oval, 0, 360, false, whitePaint);
	}
}
