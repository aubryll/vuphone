package org.vuphone.wwatch.android.mapview;

import org.vuphone.wwatch.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.GridView;

/**
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class AccidentImageDialog extends AlertDialog {

	private final GridView imageGrid_;
	private final ImageAdapter adapter_;
	
	public AccidentImageDialog(Context c, int wreckID) {
		super(c);
		
		adapter_ = new ImageAdapter(c, wreckID);
		imageGrid_ = (GridView) View.inflate(c, R.layout.gallery_grid, null);
		imageGrid_.setAdapter(adapter_);
		
		setTitle("Accident Images");
		setIcon(0);
		setView(imageGrid_);
	}
}
