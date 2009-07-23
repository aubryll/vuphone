/*******************************************************************************
 * Copyright 2009 Krzysztof Zienkiewicz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package org.vuphone.augmentedreality.android;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.widget.FrameLayout;

/**
 * A view used for Augmented Reality applications. It composites a SurfaceView
 * that displays the camera preview with another view on which the user can
 * draw. To draw an overlay, implement the ARDrawer interface and register your
 * object with addDrawer().
 * 
 * @author Krzysztof Zienkiewicz
 */

public class ARView extends FrameLayout {

	private CameraPreview preview_;
	private OverlayView overlay_;
	private InvalidatorThread invalidator_;

	public ARView(Context context) {
		super(context.getApplicationContext());
		preview_ = new CameraPreview(context);
		overlay_ = new OverlayView(context);
		invalidator_ = new InvalidatorThread(overlay_);

		addView(preview_);
		addView(overlay_);
		
		invalidator_.start();
	}

	/**
	 * This method registers the user's ARDrawer. Multiple drawers can be added.
	 * The canvas is passed to each drawer in sequence based on the order in
	 * which they were added.
	 * 
	 * @param drawer
	 *            An ARDrawer object which will receive the overlay canvas.
	 */
	public void addDrawer(ARDrawer drawer) {
		overlay_.addDrawer(drawer);
	}
	
	public void destroy() {
		invalidator_.kill();
	}
	
	public void pause() {
		invalidator_.pause();
		overlay_.pause();
	}
	
	public void resume() {
		invalidator_.unpause();
		overlay_.resume();
	}

	private class InvalidatorThread extends Thread {

		private final View view_;
		private boolean shouldRun_ = true;
		private boolean isPaused_ = false;

		public InvalidatorThread(View view) {
			super("OverlayInvalidator");
			view_ = view;
		}
		
		public void kill() {
			shouldRun_ = false;
		}

		public void pause() {
			isPaused_ = true;
		}

		@Override
		public void run() {
			while (shouldRun_) {
				if (!isPaused_)
					view_.postInvalidate();
				else {
					try {
						// Give someone a chance to reset the flag.
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}

		public void unpause() {
			isPaused_ = false;
		}
	}

	private class OverlayView extends View {

		private ArrayList<ARDrawer> drawerList_;
		private boolean isReady_;

		public OverlayView(Context context) {
			super(context.getApplicationContext());
			drawerList_ = new ArrayList<ARDrawer>();
		}

		public void addDrawer(ARDrawer drawer) {
			drawerList_.add(drawer);
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			if (!isReady_) {
				isReady_ = true;
				for (int i = 0; i < drawerList_.size(); i++)
					drawerList_.get(i).surfaceReady(canvas.getWidth(),
							canvas.getHeight());
			}

			for (int i = 0; i < drawerList_.size(); i++)
				drawerList_.get(i).draw(canvas);
		}
		
		public void pause() {
			for (int i = 0; i < drawerList_.size(); i++)
				drawerList_.get(i).pause();
		}
		
		public void resume() {
			for (int i = 0; i < drawerList_.size(); i++)
				drawerList_.get(i).resume();
		}
	}

	private class CameraPreview extends SurfaceView implements Callback {

		private Camera camera_;

		public CameraPreview(Context context) {
			super(context.getApplicationContext());
			SurfaceHolder holder = getHolder();
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			holder.addCallback(this);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
			camera_.startPreview();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			camera_ = Camera.open();
			try {
				camera_.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
				camera_.release();
				camera_ = null;
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			camera_.stopPreview();
			camera_.release();
			camera_ = null;
		}
	}
}
