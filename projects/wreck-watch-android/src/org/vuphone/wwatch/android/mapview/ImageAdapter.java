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
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter implements HttpOperationListener{

    private Context context_;
    
    private int wreckID_ = -1;

    private Bitmap[] images_ = {};
    
	private static final String LOG_PREFIX = "ImageAdapter: ";
    
    public ImageAdapter(Context c, int id) {
    	context_ = c;
    	wreckID_ = id;
    	HTTPGetter.doPictureGet(wreckID_, this);
    }
    
	public void operationComplete(HttpResponse resp) {
		Log.v(VUphone.tag, "ImageAdapter.operationComplete()");
		images_ = parseImages(resp);
		Log.v(VUphone.tag, "ImageAdapter set images_ array");
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
	    		
	    		// TODO - This is too indirect. Work on efficiency.
	    		ByteArrayInputStream bis = new ByteArrayInputStream(data);
	    		images[i] = new BitmapDrawable(bis).getBitmap();
	    		
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
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(context_);

        i.setImageBitmap(images_[position]);
        // The line below sets the size of the pictures you are displaying.
        i.setLayoutParams(new Gallery.LayoutParams(225, 150));
        i.setScaleType(ImageView.ScaleType.FIT_XY);

        return i;
    }  
}