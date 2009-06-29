package org.vuphone.wwatch.android.mapview.pinoverlays;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.mapview.AccidentActivity;
import org.vuphone.wwatch.android.mapview.AccidentImageDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

/**
 * Holds the OverlayItems (Waypoints, here) that should currently be rendered to
 * the map. A class can update those items, but cannot add or remove single
 * items for performance reasons.
 * 
 * Handles onTap events and display's the routes
 */
public class WreckOverlay extends ItemizedOverlay<Waypoint> {

	private PinController pc_;

	private static final String pre = "PinOverlay: ";
	private static final String tag = VUphone.tag;

	private Context context_ = null;

	public WreckOverlay(Context context, PinController c) {
		super(new ShapeDrawable(new OvalShape()));

		pc_ = c;
		context_ = context;

		populate();

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
	
	@Override
	public void draw(Canvas canvas, MapView mv, boolean shadow){
		try{
			super.draw(canvas, mv, shadow);
		}catch (IndexOutOfBoundsException e) {
			Log.w(tag, pre + "IndexOutOfBoundsException drawing WreckOverlay");
			
		}
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
	
	public void showGalleryDialog(final int id){
		
         
         AlertDialog dialog = new AlertDialog.Builder(context_)
                .setTitle("Wreck Image Options")
                .setItems(new String[]{"View Images", "Upload Image", "Cancel"}, new OnClickListener() {
                        public void onClick(DialogInterface d, int item) {
                                Log.v(VUphone.tag, "Item Clicked " + item);
                                switch (item) {
                                case 0:
                                        new AccidentImageDialog(context_, id).show();
                                        break;
                                case 1:
                                        ((AccidentActivity) context_).startUploadProcess(id);
                                        break;
                                case 2:
                                        d.dismiss();
                                        break;
                                }
                        }
                }).create();

         dialog.show();
         
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
