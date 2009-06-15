package org.vuphone.wwatch.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class MediaUploadActivity extends Activity {

	private static final String SERVER = "http://129.59.135.144:8080";
	private static final String PATH = "/wreckwatch/notifications";
	
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
		uploadImage(content);		
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
	
	private void showToast(final String msg){
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(MediaUploadActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void uploadImage(final Uri uri) {
		new Thread(new Runnable (){
		public void run() {
			
		final ContentResolver resolver = getContentResolver();
		final String time = Long.toString(System.currentTimeMillis());
	
		try {
			showToast("Reading byte data...");
			
			final InputStream stream = resolver.openInputStream(uri);
			ByteArrayBuffer array = new ByteArrayBuffer(100);
			int b;
			int size = 0;
			while((b = stream.read()) != -1) {
				size++;
				array.append(b);
			}
			
			ByteArrayEntity ent = new ByteArrayEntity(array.toByteArray());
			
				
			final HttpClient client = new DefaultHttpClient();
			
			String params = "?type=image&time=" + time + "&longitude=0.0&latitude=0.0";
			
			final HttpPost post = new HttpPost(SERVER + PATH + params);
			
			post.addHeader("Content-Type", resolver.getType(uri));
			
			post.setEntity(ent);

			showToast("Uploading to server...");
			client.execute(post);
			showToast("Finished uploading.");
//			HTTPPoster.doAccidentPost("", 0L, 0.0, 0.0, 0.0, 0.0, new Runnable() {
//				public void run(){}});
		
		} catch (FileNotFoundException e) {
			Log.v(VUphone.tag, "File not found");
			return;
		} catch (IOException e) {
			Log.v(VUphone.tag, "IOException while reading image stream");
		}
		
		}}).start();
	}

}
