package org.vuphone.wwatch.android.mapview;

import org.vuphone.wwatch.android.VUphone;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class AccidentImageView extends GridView {

	public AccidentImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setEmptyView(View e) {
		super.setEmptyView(e);
		TextView view = (TextView) e;
		
		ListAdapter adapt = getAdapter();
		if (adapt == null || ((ImageAdapter) adapt).getState() == ImageAdapter.LOADING) {
			view.setText(AccidentImageDialog.LOADING_STRING);
		}
	}
	
	@Override
	protected void handleDataChanged() {
		super.handleDataChanged();
		Log.v(VUphone.tag, "handleDataChanged()");
 
		/*
		TextView view = (TextView) getEmptyView();
		ImageAdapter adapt = (ImageAdapter) getAdapter();
		if (adapt.getState() == ImageAdapter.EMPTY) {
			view.setText(AccidentImageDialog.EMPTY_STRING);
		}

		int w = adapt.getMaxWidth();
		Log.v(VUphone.tag, "Setting column width: " + w);
		setColumnWidth(w);
		*/
		
		postInvalidate();
	}

}