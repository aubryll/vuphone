package org.vuphone.wwatch.android.mapview;

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
    
    private int wreckID_ = -1;

    private Bitmap[] images_ = {};
    
	private static final String LOG_PREFIX = "ImageAdapter: ";
    
    public ImageAdapter(Context c, int id) {
    	context_ = c;
    	wreckID_ = id;
    	images_ = parseImageFromServer(HTTPGetter.doPictureGet(wreckID_));
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
	    try {
	    	ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
	    	byte[] array = bao.toByteArray();
		
	    	// Note: This only works if the headerLength is >20 and < 1,000,000
	    	byte[] headerLengthArr = new byte[20];
	    	for (int i = 0; i < 20; i++) {
	    		headerLengthArr[i] = array[i];
	    	}
	    	String headerLengthStr = new String(headerLengthArr);

	    	String lenStr = headerLengthStr.substring(
	    			headerLengthStr.indexOf("=")+1,headerLengthStr.indexOf(","));
	    	int headLen = Integer.parseInt(lenStr);
	    	
	    	byte[] headersArr = new byte[headLen];
	    	for (int i = 0; i < headLen; i++) {
	    		headersArr[i] = array[i];
	    	}
	    	String headers = new String(headersArr);
	    	
	    	String[] allHeaders = headers.split(",");
	    		for (String h : allHeaders) {
	    			String[] pair = h.split("=");
	    			if (pair[0].equals("NumImages")) {
	    				numImages = Integer.parseInt(pair[1]);
	    			}
	    			else if (pair[0].startsWith("Image")) {
	    				int ind = Character.getNumericValue(pair[0].charAt(5));
	    				imgLengths.put(ind, Integer.parseInt(pair[1]));
	    			}
	    		}
	    		
	    	Bitmap[] bitmapArr = new Bitmap[numImages];

	    	Log.d(VUphone.tag, LOG_PREFIX + "parseImageFromServer received "+array.length+" bytes.");
	    	int arrCount = 0;

	    	int size;
    		int index = headLen;
    		while (arrCount < numImages) {
    			size = imgLengths.get(arrCount);
    			byte[] img = new byte[size];
    			for(int i = 0; i < size; i++) {
    				img[i] = array[i+index];
    			}
    			ByteArrayInputStream is = new ByteArrayInputStream(img);
    			BitmapDrawable bmd = new BitmapDrawable(is);
    			Bitmap b = bmd.getBitmap();

    			Log.d(VUphone.tag, LOG_PREFIX + "added image "+arrCount);
    			bitmapArr[arrCount] = b;
    			arrCount++;
    			index = index + size;
    		}
    		
    		return bitmapArr;
    	}
	    catch (IOException e) {
	    	
	    	Log.d(VUphone.tag, LOG_PREFIX + "There was an IOException.");
	    	e.printStackTrace();
	    	return null;
	    }
    	catch (Exception e) {
    		
	    	Log.d(VUphone.tag, LOG_PREFIX + "There was a different Exception.");
    		e.printStackTrace();
    		return null;
    	}
    }
    
}
