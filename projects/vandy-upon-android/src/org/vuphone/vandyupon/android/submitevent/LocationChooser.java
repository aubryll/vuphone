/**
 * 
 */
package org.vuphone.vandyupon.android.submitevent;

import org.vuphone.vandyupon.android.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.maps.MapActivity;

/**
 * @author Hamilton Turner
 * 
 */
public class LocationChooser extends MapActivity {
	private LocationChooserMap map_;

	/**
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/** Called when the Activity is first created */
	@Override
	protected void onCreate(Bundle ice) {
		super.onCreate(ice);
		setContentView(R.layout.chooser_map);

		map_ = (LocationChooserMap) findViewById(R.id.chooser_map);
	}

	/** Called when the Activity is no longer visible */
	@Override
	protected void onPause() {
		super.onPause();
		map_.disableMyLocation();
	}

	/** Called when the Activity is about to be visible */
	@Override
	protected void onResume() {
		super.onResume();
		map_.enableMyLocation();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& map_.getCurrentLocation() == null) {
			Toast.makeText(this, "Please tap the map to choose a location",
					Toast.LENGTH_LONG).show();
			return true;
		}

		Intent result = new Intent();
		result.putExtra(SubmitEvent.RESULT_LAT, map_.getCurrentLocation()
				.getLatitudeE6());
		result.putExtra(SubmitEvent.RESULT_LNG, map_.getCurrentLocation()
				.getLongitudeE6());

		setResult(SubmitEvent.RESULT_OK, result);
		return super.onKeyDown(keyCode, event);
	}
}
