package edu.vanderbilt.vuphone.android.objects;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.vanderbilt.vuphone.android.dining.R;

public class ViewWrapper {
	private View _base;
	private ImageView _favorite;
	private TextView _name;
	private TextView _special;
	
	public ViewWrapper(View base) {
		_base = base;
	}
	
	public ImageView getFavoriteView() {
		if (_favorite == null) 
			_favorite = (ImageView)_base.findViewById(R.mainListItem.favoriteIcon);
		return _favorite;
	}
	
	public TextView getNameView() {
		if (_name == null) 
			_name=(TextView)_base.findViewById(R.mainListItem.name);
		return _name;
	}
	
	public TextView getSpecialView() {
		if (_special == null)
			_special = (TextView)_base.findViewById(R.mainListItem.specialText);
		return _special;
	}
}
