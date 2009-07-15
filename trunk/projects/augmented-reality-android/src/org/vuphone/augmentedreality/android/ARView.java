package org.vuphone.augmentedreality.android;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.widget.FrameLayout;

// TODO - Figure out how to do Full-Screen Exclusive and the correct params for camera lens.
public class ARView extends FrameLayout {

	private CameraPreview preview_;
	private OverlayView overlay_;

	public ARView(Context context) {
		super(context.getApplicationContext());
		preview_ = new CameraPreview(context);
		overlay_ = new OverlayView(context);

		addView(preview_);
		addView(overlay_);
	}

	public void setDrawer(ARDrawer drawer) {
		overlay_.addDrawer(drawer);
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
			Parameters params = camera_.getParameters();
			params.setPreviewSize(w, h);
			camera_.setParameters(params);
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
