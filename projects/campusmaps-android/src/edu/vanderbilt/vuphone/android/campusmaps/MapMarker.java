package edu.vanderbilt.vuphone.android.campusmaps;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 
 * @author Adam Albright
 * 
 *         This class implements a marker/pin that can be placed and drug around
 *         on the map
 * 
 */
public class MapMarker extends com.google.android.maps.Overlay {
	GeoPoint p_;
	MapView mapView_;
	int marker_image_;
	Context context_;
	Resources resources_;
	Boolean dragging_ = false;

	public MapMarker(Context context, Resources resources, MapView mapView, GeoPoint p) {
		mapView_ = mapView;
		p_ = p;
		context_ = context;
		resources_ = resources;

		// Select a random pin color
		int images[] = { R.drawable.marker_yellow, R.drawable.marker_blue, R.drawable.marker_red,
				R.drawable.marker_black };
		Random generator = new Random();
		marker_image_ = images[generator.nextInt(4)];
	}

	/**
	 * Used to place the pin on the map
	 */
	public void drop_pin() {
		List<Overlay> listOfOverlays = mapView_.getOverlays();
		listOfOverlays.add(this);

		mapView_.invalidate();
	}

	/**
	 * Used to remove the pin from the map
	 */
	public void remove_pin() {
		List<Overlay> listOfOverlays = mapView_.getOverlays();
		listOfOverlays.remove(this);
		mapView_.invalidate();

	}

	public void remove_all_overlays() {
		List<Overlay> listOfOverlays = mapView_.getOverlays();
		listOfOverlays.clear();

		mapView_.invalidate();
	}

	/**
	 * Called several times per second when the screen refreshes
	 */
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
		super.draw(canvas, mapView, shadow);

		// convert GeoPoint to screen pixels
		Point screenPts = new Point();
		mapView.getProjection().toPixels(p_, screenPts);

		// drop a random colored pin
		Bitmap bmp = BitmapFactory.decodeResource(resources_, marker_image_);
		canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);
		return true;
	}

	/**
	 * Called when the user taps or drags the screen
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());

		// are they are starting or stopping a drag?
		if (event.getAction() != MotionEvent.ACTION_MOVE) {
			int diff_lat = p.getLatitudeE6() - p_.getLatitudeE6();
			int diff_long = p.getLongitudeE6() - p_.getLongitudeE6();

			// Hit test
			if (diff_lat < 800 && diff_lat > -50 && diff_long < 700 && diff_long > -150) {
				dragging_ = true;
				// TODO: Disable the map from panning while the user drags the marker
				// mapView.setEnabled(false);
			} else
				dragging_ = false;
		}

		// update the marker's position to that of the user's finger
		if (dragging_)
			p_ = p;

		return false;
	}

	/*
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(context_, s, Toast.LENGTH_SHORT).show();
	}
}
