package org.vuphone.wwatch.android.mapview;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.http.HTTPGetter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
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
		//image_.setLayoutParams(new LayoutParams(w, h));
		setContentView(image_);
		
		int id = intent.getIntExtra("FullImageID", -1);
		HTTPGetter.doFullPictureGet(id, this);
		
	}
	
	public void operationComplete(final HttpResponse resp) {
	   	if (!resp.containsHeader("ImageCount"))
	   		return;
	   	
		final HttpEntity ent = resp.getEntity();
		
    	runOnUiThread(new Runnable() {
			public void run() {
				Bitmap bmap = null;
				Options opt = new Options();
				
				Options dims = new Options();
				dims.inJustDecodeBounds = true;
				
				final int height = getWindowManager().getDefaultDisplay().getHeight();
				final int width = getWindowManager().getDefaultDisplay().getWidth();
				
				try {
					InputStream is = ent.getContent();
					
		    		int sz = Integer.parseInt(resp.getHeaders("Image0Size")[0].getValue());
		    		final byte[] data = new byte[sz];
		    		int offset = 0;
		    		int numRead = 0;
		    		
		    		while ((numRead = is.read(data, offset, sz - offset)) >= 0 && offset < sz) {
		    			offset += numRead;
		    		}
		    		
		    		BitmapFactory.decodeByteArray(data, 0, data.length, dims);
		    		
		    		int w = dims.outWidth;
		    		int h = dims.outHeight;
		    		
		    		float multX = (float) w / width;
		    		float multY = (float) h / height;
		    		
		    		int scale = (int) Math.max(multX, multY);
		    		opt.inSampleSize = scale;
		    		
		    		bmap = BitmapFactory.decodeByteArray(data, 0, data.length, opt);

				
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (bmap == null)
					Toast.makeText(image_.getContext(), "Decode failed", Toast.LENGTH_SHORT).show();
				
				image_.setImageBitmap(bmap);
			}
		});
	}
	
}
