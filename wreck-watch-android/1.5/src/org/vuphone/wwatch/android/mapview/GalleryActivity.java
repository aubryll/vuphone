package org.vuphone.wwatch.android.mapview;

import org.vuphone.wwatch.android.VUphone;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

public class GalleryActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Dialog d = new AccidentImageDialog(this, -1);
	    d.show();
	}
}
