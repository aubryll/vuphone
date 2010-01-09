package edu.vanderbilt.vuphone.android.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.storage.Restaurant;

/**
 * Creates the map that displays the location of all dining facilities
 * 
 * @author Peter
 */
public class AllLocations extends MapActivity {

	MapView mapView;
	AllOverlays diningOverlay;
	MyLocationOverlay myLocationOverlay;
	
	/**
	 * sets zoom so all dining locations and most of Vanderbilts campus are
	 * visible without panning
	 */
	public static final int ZOOM = 16;
	public static final GeoPoint CENTER = new GeoPoint(36143299, -86802464);

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		// start map view and enable zoom controls
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.map.mapview);
		mapView.setBuiltInZoomControls(true);


		mapView.getController().setZoom(ZOOM);
		mapView.getController().setCenter(CENTER);
		
		// creates the overlay containing markers for all dining locations
		diningOverlay = new AllOverlays(this, mapView);
		
		myLocationOverlay = new MyLocationOverlay(this,
				mapView);
		myLocationOverlay.enableMyLocation(); 

		mapView.getOverlays().add(diningOverlay);
		mapView.getOverlays().add(myLocationOverlay);
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public AllOverlays getDiningOverlay() {
		return diningOverlay;
	}
	
	// MENU FUNCTIONS
	
	public static final int MENU_SETTINGS = 0;
	public static final int MENU_CURRENT_LOC = 1;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, "Settings").
				setIcon(getResources().getDrawable(android.R.drawable.ic_menu_preferences));
		menu.add(Menu.NONE, MENU_CURRENT_LOC, Menu.NONE, "My Location").
				setIcon(getResources().getDrawable(android.R.drawable.ic_menu_mylocation));
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_SETTINGS:
			showDialog(DIALOG_SETTINGS);
			return true;
		case MENU_CURRENT_LOC:
			if (myLocationOverlay.getMyLocation() != null)
				mapView.getController().animateTo(myLocationOverlay.getMyLocation());
			else Toast.makeText(this, "Your location is temporarily unavailable", Toast.LENGTH_SHORT).show();
			// TODO make getting location device type text, same as in Main
			return true;
		}
		return true;
	}
	
	// DIALOG FUNCTIONS
	
	public static final int DIALOG_SETTINGS = 0;
	
	private static final boolean [] SETTINGS_DEFAULT = {false, false, false};
	private boolean [] settingsChecked;
	
	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);
		switch (id) {
		case DIALOG_SETTINGS:
		default: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			CharSequence[] settings = { "Hide closed locations", "Hide no meal plan", "Hide no meal money" };
			settingsChecked = SETTINGS_DEFAULT.clone();

			builder.setMultiChoiceItems(settings, settingsChecked,
					new DialogInterface.OnMultiChoiceClickListener() {

						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							settingsChecked[which] = isChecked;
							((AlertDialog) dialog).getListView()
									.setItemChecked(which, isChecked);

						}
					});
			
			builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
	
				public void onClick(DialogInterface dialog, int which) {
					AllLocations.this.updateSettings();
					dialog.dismiss();
				}
			});
			
			builder.setNegativeButton("Set Defaults", new DialogInterface.OnClickListener() {
				

				public void onClick(DialogInterface dialog, int which) {
					settingsChecked = SETTINGS_DEFAULT.clone();
					AllLocations.this.updateSettings();
					dialog.dismiss();
				}
			});

			builder.setTitle("Settings");

			return builder.create();
		}
		}
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DIALOG_SETTINGS:
			for (int i = 0; i < settingsChecked.length; i++) {
				// TODO make this work correctly, (messes up if set Defaults is pressed
				((AlertDialog)dialog).getListView().setItemChecked(i, settingsChecked[i]);
			}
		}
	}
	
	private void updateSettings() {
		// update this function with more entries as more settings are added
		diningOverlay.setHideForFilter(settingsChecked[0], AllOverlays.FILTER_CLOSED);
		diningOverlay.setHideForFilter(settingsChecked[1], AllOverlays.FILTER_PLAN);
		diningOverlay.setHideForFilter(settingsChecked[2], AllOverlays.FILTER_MONEY);
		diningOverlay.notifyDataSetChanged();
		mapView.invalidate();
	}
	
}
