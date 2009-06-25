package org.vuphone.wwatch.android.mapview.pinoverlays;

import org.vuphone.wwatch.android.R;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.maps.ItemizedOverlay;

/**
 * Holds the OverlayItems (Waypoints, here) that should currently be rendered to
 * the map. A class can update those items, but cannot add or remove single
 * items for performance reasons.
 * 
 * Handles onTap events and display's the routes
 */
public class WreckOverlay extends ItemizedOverlay<Waypoint> {

	private AlertDialog dialog_ = null;
	
	private PinController pc_;

	private static final String pre = "PinOverlay: ";
	private static final String tag = VUphone.tag;

	private Context context_ = null;

	public WreckOverlay(Context context, PinController c) {
		super(new ShapeDrawable(new OvalShape()));

		pc_ = c;
		context_ = context;

		populate();

		LayoutInflater inflater = LayoutInflater.from(context_);
		View gallery = inflater.inflate(R.layout.wreck_details, null);

		dialog_ = new AlertDialog.Builder(this.context_).setView(gallery)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface di, int what) {
								di.dismiss();
							}
						}).create();
	}

	/**
	 * Allows this class to return items in any manner we would like, if we
	 * wanted to impose our own ordering on drawing. We do not, so we just
	 * return the item as we have them stored
	 */
	@Override
	protected Waypoint createItem(int i) {
		Log.i(tag, pre + "Create item " + i + " returning "
				+ pc_.getWreck(i).toString());
		return pc_.getWreck(i);
	}

	/**
	 * Informs us a tap event occurred on item with index index. First tap
	 * display's that wrecks route, and second tap will pop up the gallery
	 * activity
	 */
	@Override
	protected boolean onTap(int index) {
		Log.d(tag, pre + "onTap called with index " + index);
		return pc_.onWreckTap(index);
	}
	
	public void showGalleryDialog(){
		dialog_.show();
	}

	/**
	 * Get the number of OverlayItem objects currently in this Overlay.
	 * 
	 * @return number of items.
	 */
	@Override
	public int size() {
		Log.i(tag, pre + "Size called, returning " + pc_.getWreckSize());
		return pc_.getWreckSize();
	}


	public void populatePins(){
		super.populate();
	}

}
