package org.vuphone.wwatch.android.media;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.mapview.AccidentActivity;
import org.vuphone.wwatch.android.services.MediaUploadService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class WreckChooserActivity extends Activity{

	private static final String pre = "WreckChooserActivity: ";
	
	private static final int PICK_WRECK_ID = 0;
	private int accidentId_;
	
	private Uri content_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		if (extras == null || !extras.containsKey(Intent.EXTRA_STREAM)) {
			Log.w(VUphone.tag, pre + "The intent does not contain the " +
					"required picture uri.  Aborting.");
			return;
		}
		
		content_ = (Uri) extras.get(Intent.EXTRA_STREAM);
		
		Intent i = new Intent(this, AccidentActivity.class);
		i.putExtra("LookingForWreckId", true);
		startActivityForResult(i,PICK_WRECK_ID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == PICK_WRECK_ID) {
			accidentId_ = resultCode;
			Log.d(VUphone.tag, pre + "onActivityResult has received the " +
					"following wreckId: "+accidentId_);
			
			Intent service = new Intent(this, MediaUploadService.class);
			service.putExtra("Uri", content_.toString());
			service.putExtra("WreckId", accidentId_);
			startService(service);
			
			// This activity is now done.
			finish();
		}
	}

}
