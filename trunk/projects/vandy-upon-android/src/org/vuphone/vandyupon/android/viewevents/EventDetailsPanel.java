/**
 * 
 */
package org.vuphone.vandyupon.android.viewevents;

import org.vuphone.vandyupon.android.R;

import android.app.Activity;
import android.widget.ImageView;

/**
 * @author Hamilton Turner
 * 
 */
public class EventDetailsPanel {
	private boolean isVisible_ = false;
	private ImageView heightManager_;

	public EventDetailsPanel(Activity a) {
		heightManager_ = (ImageView) a
				.findViewById(R.id.IMG_event_panel_height_manager);
	}

	/** Returns whether or not the Event Details panel is being displayed */
	protected boolean isVisible() {
		return isVisible_;
	}

	/** Sets whether or not the Event Details panel is being displayed */
	protected void setVisible(boolean visible) {
		isVisible_ = visible;

		if (isVisible_) {
			heightManager_.setAdjustViewBounds(true);
			heightManager_.setMaxHeight(20);
			heightManager_.invalidate();
			
//			Thread t = new Thread(new Runnable() {
//				public void run() {
//					heightManager_.setAdjustViewBounds(true);
//					heightManager_.setMinimumHeight(0);
//					for (int i = 1; i < 200; ++i) {
//						heightManager_.setMaxHeight(i);
//						heightManager_.postInvalidate();
//						try {
//							Thread.sleep(500);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//			});
//			t.setDaemon(true);
//			t.setName("Event Panel Open Animation");
//			t.start();
		}

	}
}
