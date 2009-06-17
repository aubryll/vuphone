package org.vuphone.wwatch.android.mapping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.http.HTTPGetter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context context_;
    
    private String point_ = "";

    private Bitmap[] images_ = {};
    
	private static final String LOG_PREFIX = "ImageAdapter: ";
    
    public ImageAdapter(Context c, String str) {
    	context_ = c;
    	point_ = str;
    	images_ = parseImageFromServer(HTTPGetter.doPictureGet(point_));
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
    
    private Bitmap[] parseImageFromServer(HttpResponse resp) {
    	
    	Map<Integer, Integer> imgLengths = new HashMap<Integer, Integer>();
    	int numImages = 0;
		for (Header h : resp.getAllHeaders()) {
			if (h.getName().equals("Number of Images")) {
				numImages = Integer.parseInt(h.getValue());
			}
			else if (h.getName().startsWith("Image")) {
				int ind = Character.getNumericValue(h.getName().charAt(5));
				imgLengths.put(ind, Integer.parseInt(h.getValue()));
			}
		}

	    Bitmap[] bitmapArr = new Bitmap[numImages];
	    
	    try {
	    	ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
		
	    	byte[] array = bao.toByteArray();
	    	int arrCount = 0;

    		int size = imgLengths.get(arrCount);
    		int index = 0;
    		// Eventually, Integer.parseInt will throw an exception, so this is
    		// not actually an infinite loop.
    		while (true) {
    			byte[] img = new byte[size];
    			for(int i = 0; i < size; i++) {
    				img[i] = array[i+index];
    			}
    			ByteArrayInputStream is = new ByteArrayInputStream(img);
    			BitmapDrawable bmd = new BitmapDrawable(is);
    			Bitmap b = bmd.getBitmap();

    			bitmapArr[arrCount] = b;
    			arrCount++;
    			index = index + size;
    			size = imgLengths.get(arrCount);
    		}
    	}
	    catch (IOException e) {
	    	
	    	Log.d(VUphone.tag, LOG_PREFIX + "There was an IOException.");
	    	e.printStackTrace();
	    	return null;
	    }
    	catch (Exception e) {

    		return bitmapArr;
    	}
    }
    
}
