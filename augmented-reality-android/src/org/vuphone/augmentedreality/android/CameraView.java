package org.vuphone.augmentedreality.android;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class CameraView extends SurfaceView implements Callback {

	private Camera camera_;

	public CameraView(Context context) {
		super(context.getApplicationContext());
		
		SurfaceHolder holder = getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {
		camera_.startPreview();
		Log.v("CameraPreview", "Started cam preview");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera_ = Camera.open();
		Log.v("CameraPreview", "Obtained camera");
		
		try {
			camera_.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
			camera_.release();
			camera_ = null;
			Log.v("CameraPreview", "Released camera");
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera_.stopPreview();
		camera_.release();
		camera_ = null;
		Log.v("CameraPreview", "Released camera");
	}
}
