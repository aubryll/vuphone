package org.vuphone.augmentedreality.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class Launcher extends Activity {
	
	private CameraView camView_;
	private GLView glView_;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
    
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    
		final Window win = getWindow(); 
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
    
		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		camView_ = new CameraView(this);
		glView_ = new GLView(this);
		
		setContentView(glView_);
		addContentView(camView_, new LayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.FILL_PARENT));
}
	
	protected void onPause() {
		super.onPause();
	}
}