package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class DiningLocationOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> locationOverlay = new ArrayList<OverlayItem>();

	public DiningLocationOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	@Override
	protected OverlayItem createItem(int i) {
		return locationOverlay.get(i);
	}

	@Override
	public int size() {
		return locationOverlay.size();
	}
	
	public void addOverlay(OverlayItem overlay)	{
		locationOverlay.add(overlay);
		populate();
	}
}
