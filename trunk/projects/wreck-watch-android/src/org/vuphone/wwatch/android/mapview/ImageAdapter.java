package org.vuphone.wwatch.android.mapview;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.http.HTTPGetter;
import org.vuphone.wwatch.android.http.HttpOperationListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter implements HttpOperationListener{

    private Bitmap[] images_ = {};
	private Handler handler_ = new Handler();
	
    public ImageAdapter(Context c, int id) {
    	loadPictures(id);
    }
    
	public void operationComplete(HttpResponse resp) {
		Log.v(VUphone.tag, "ImageAdapter.operationComplete()");
		images_ = parseImages(resp);
		Log.v(VUphone.tag, "ImageAdapter set images_ array");
		
		handler_.post(new Runnable() {
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
    
	private void loadPictures(int id) {
		HTTPGetter.doPictureGet(id, this);
	}
	
    private Bitmap[] parseImages(HttpResponse resp) {
    	HttpEntity ent = resp.getEntity();
    	int imgTotal = Integer.parseInt(resp.getHeaders("ImageCount")[0].getValue());
    	int sizeTotal = (int) ent.getContentLength();
    	String type = ent.getContentType().getValue();
    	Log.v(VUphone.tag, "Received response: " + imgTotal +
    			" images. Total size: " + sizeTotal + ". Sent as " + type);
    	
    	Bitmap[] images = new Bitmap[imgTotal];
    	
    	try {
	    	InputStream is = ent.getContent();	    	
	    	for (int i = 0; i < imgTotal; ++i) {
	    		int sz = Integer.parseInt(resp.getHeaders("Image" + i + "Size")[0].getValue());
	    		byte[] data = new byte[sz];
	    		int offset = 0;
	    		int numRead = 0;
	    		
	    		while ((numRead = is.read(data, offset, sz - offset)) >= 0 && offset < sz) {
	    			offset += numRead;
	    		}
	    		
	    		images[i] = BitmapFactory.decodeByteArray(data, 0, data.length);
	    		
	    	}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	return images;    	
    }

    public int getCount() {
        return images_.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // If it's not recycled, initialize some attributes
            imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(images_[position]);
        
        return imageView;
    }  
}