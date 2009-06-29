package org.vuphone.wwatch.android.mapview;

import org.vuphone.wwatch.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class AccidentImageDialog extends AlertDialog {

	private final AccidentImageView imageGrid_;
	private final ImageAdapter adapter_;
	
	public static final String EMPTY_STRING = "No images available.";
	public static final String LOADING_STRING = "Downloading images...";
	
	public AccidentImageDialog(Context c, int wreckID) {
		super(c);
		
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.image_grid, null);
		
		imageGrid_ = (AccidentImageView) layout.findViewById(R.id.image_grid);
		adapter_ = new ImageAdapter(c, wreckID);
		imageGrid_.setAdapter(adapter_);
		imageGrid_.setEmptyView(layout.findViewById(R.id.empty_view));

		
		setTitle("Accident Images");
		setIcon(0);
		setView(layout);
	}
}
