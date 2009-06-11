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
		
		(new Runnable() {
			public void run() {
				uploadImage(content);
			}
		}).run();
		
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
	
	private void uploadImage(final Uri uri) {

		ContentResolver resolver = getContentResolver();
	
		try {
			final InputStream stream = resolver.openInputStream(uri);
			ByteArrayBuffer array = new ByteArrayBuffer(100);
			int b;
			int size = 0;
			while((b = stream.read()) != -1) {
				size++;
				array.append(b);
			}
			
//			Log.v(VUphone.tag, "Read stream. Size: " + size);
//			byte[] encoded = Base64.base64Encode(array.toByteArray());
//			Log.v(VUphone.tag, "Encoded byte array");
			
			ByteArrayEntity ent = new ByteArrayEntity(array.toByteArray());
			
			String address = "http://129.59.135.177:8080";
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(address);
			
			post.addHeader("Content-Type", resolver.getType(uri));
			post.setEntity(ent);
			
			client.execute(post);
			
			
		} catch (FileNotFoundException e) {
			Log.v(VUphone.tag, "File not found");
			return;
		} catch (IOException e) {
			Log.v(VUphone.tag, "IOException while reading image stream");
		}
	}

}
