package org.vuphone.wwatch.android.mapview;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.http.HTTPGetter;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

public class FullImageViewer extends Activity {

	private ImageView image_;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Intent intent = getIntent();
		if (!intent.hasExtra("FullImageID")) {
			finish();
			return;
		}
		
		image_ = new ImageView(this);
		int h = getWindowManager().getDefaultDisplay().getHeight();
		int w = getWindowManager().getDefaultDisplay().getWidth();
		//image_.setLayoutParams(new LayoutParams(w, h));
		setContentView(image_);
		
		int id = intent.getIntExtra("FullImageID", -1);
		HTTPGetter.doFullPictureGet(id, this);
		
	}
	
	public void operationComplete(HttpResponse resp) {
	   	if (!resp.containsHeader("ImageCount"))
	   		return;
	   	
		final HttpEntity ent = resp.getEntity();
    	final int size = (int) ent.getContentLength();
		
    	runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(image_.getContext(), "Got pic " + size, Toast.LENGTH_SHORT).show();
				Bitmap bmap = null;
				try {
					bmap = BitmapFactory.decodeStream(ent.getContent());
				} catch (IOException e) {
					Toast.makeText(image_.getContext(), "IOException", Toast.LENGTH_SHORT).show();
				}
				
				if (bmap == null)
					Toast.makeText(image_.getContext(), "Decode failed", Toast.LENGTH_SHORT).show();
				
				image_.setImageBitmap(bmap);
			}
		});
	}
	
}
