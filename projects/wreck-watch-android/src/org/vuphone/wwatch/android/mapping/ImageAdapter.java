package org.vuphone.wwatch.android.mapping;

import org.vuphone.wwatch.android.R;
import org.vuphone.wwatch.android.http.HTTPGetter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    //int mGalleryItemBackground;
    private Context mContext;
    
    private String point_ = "";

    private Integer[] mImageIds = {
            R.drawable.help_icon,
            R.drawable.icon,
            R.drawable.unhapppy,
            R.drawable.ww_icon2,
    };

    public ImageAdapter(Context c) {
        mContext = c;
        //TypedArray a = obtainStyledAttributes(android.R.styleable.Theme);
        //mGalleryItemBackground = a.getResourceId(
        //        android.R.styleable.Theme_galleryItemBackground, 0);
        //a.recycle();
    }
    
    public ImageAdapter(Context c, String str) {
    	mContext = c;
    	point_ = str;
    	mImageIds = HTTPGetter.doPictureGet(point_);
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(mContext);

        i.setImageResource(mImageIds[position]);
        i.setLayoutParams(new Gallery.LayoutParams(150, 100));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        //i.setBackgroundResource(mGalleryItemBackground);

        return i;
    }
}
