package org.vuphone.wwatch.android.mapview;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.http.HTTPGetter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ImageAdapter extends BaseAdapter implements OnItemClickListener {

    private Bitmap[] images_ = {};
    private int[] imageIDs_ = {};
    private int maxWidth_ = 0;
	private Handler handler_ = new Handler();
	
	public final static int LOADING = 0;
	public final static int EMPTY = 1;
	public final static int FULL = 2;
	
	private Integer state_ = LOADING;
	private static final int PAD = 5;
	private Context context_;
	
    public ImageAdapter(Context c, int id) {
    	context_ = c;
    	loadPictures(id);
    }
    
    //public synchronized int getState() {
    public int getState() {
    	return state_;
    }
    
    public int getMaxWidth() {
    	return maxWidth_ + (2 * PAD);
    }
    
	public void operationComplete(HttpResponse resp) {
		Log.v(VUphone.tag, "ImageAdapter.operationComplete()");
		parseImages(resp);
		Log.v(VUphone.tag, "ImageAdapter set images_ array");
		
		handler_.post(new Runnable() {
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
    
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int imgID = imageIDs_[position];
		Toast.makeText(parent.getContext(), "Item Clicked " + imgID, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(parent.getContext(), org.vuphone.wwatch.android.mapview.FullImageViewer.class);
		intent.putExtra("FullImageID", imgID);
		parent.getContext().startActivity(intent);
	}
	
	private void loadPictures(int id) {
		HTTPGetter.doPictureGet(id, this);
	}
	
    private void parseImages(HttpResponse resp) {
    	if (!resp.containsHeader("ImageCount")) {
    		state_ = EMPTY;
    		return;
    	}
    	
    	maxWidth_ = 0;
    	images_ = null;
    	imageIDs_ = null;
    	
    	HttpEntity ent = resp.getEntity();
    	int imgTotal = Integer.parseInt(resp.getHeaders("ImageCount")[0].getValue());
    	
    	//synchronized (state_) {
	    	if (imgTotal == 0) {
	    		state_ = EMPTY;
	    	} else {
	    		state_ = FULL;
	    	}
    	//}
    	
    	int sizeTotal = (int) ent.getContentLength();
    	String type = ent.getContentType().getValue();
    	Log.v(VUphone.tag, "Received response: " + imgTotal +
    			" images. Total size: " + sizeTotal + ". Sent as " + type);
    	
    	images_ = new Bitmap[imgTotal];
    	imageIDs_ = new int[imgTotal];
    	
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
	    		
	    		images_[i] = BitmapFactory.decodeByteArray(data, 0, data.length);
	    		if (images_[i].getWidth() > maxWidth_)
	    			maxWidth_ = images_[i].getWidth();
	    		
	    		imageIDs_[i] = Integer.parseInt(resp.getHeaders("Image" + i + "ID")[0].getValue());
	    		
	    	}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
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
            imageView = new ImageView(context_);//parent.getContext());
            int w = images_[position].getWidth();
            int h = images_[position].getHeight();
            //Log.v(VUphone.tag, "Setting LayoutParams: " + w + ", " + h);
            imageView.setLayoutParams(new GridView.LayoutParams(w, h));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(PAD, PAD, PAD, PAD);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageBitmap(images_[position]);
        
        return imageView;
    }  
}