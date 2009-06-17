package org.vuphone.wwatch.android;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class MediaUploadActivity extends Activity {
	
	private TextView debug_;
	private ImageView img_;
	
	private void debug(String msg) {
		String log = debug_.getText() + "\n" + msg;
		debug_.setText(log);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_upload);
		debug_ = (TextView) findViewById(R.id.debug);
		img_ = (ImageView) findViewById(R.id.image);
		
		debug_.setText("Debug TextView");
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		if (extras == null || !extras.containsKey(Intent.EXTRA_STREAM)) {
			debug("No content data available");
			return;
		}
		
		final Uri content = (Uri) extras.get(Intent.EXTRA_STREAM);
		debug("Uri: " + content);
		
		
		ContentResolver resolver = getContentResolver();
		String type = resolver.getType(content);
		debug("Type: " + type);
		
		if (!type.contains("image")) {
			debug("Content is not an image");
			return;
		}
		
		img_.setImageURI(content);
		
		Intent service = new Intent(this, MediaUploadService.class);
		service.putExtra("Uri", content.toString());
		startService(service);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
    protected void onPause() {
		super.onPause();
	}
	
	@Override
    protected void onRestart() {
    	super.onPause();
    }
	
	@Override
    protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
    protected void onStop() {
		super.onStop();
	}
	
}
