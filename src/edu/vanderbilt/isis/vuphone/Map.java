package edu.vanderbilt.isis.vuphone;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.MapActivity;

import edu.vanderbilt.isis.R;

class ProgramConstants {
	static final int MENU_QUIT = 0;
	static final int MENU_ADD_OR_SAVE_ZONE = 1;
	static final int MENU_PICK_ZONE = 2;
	static final int MENU_TOGGLE_VIEW = 3;

	static final int DIALOG_QUIT = 4;
	static final int DIALOG_ROUTING = 5;
}

public class Map extends MapActivity {

	private ZoneMapView mapView_ = null;
	private View buttonBar_ = null;

	/**
	 * Called when this activity first starts.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapView_ = (ZoneMapView) findViewById(R.id.mapview);
		buttonBar_ = findViewById(R.id.button_bar);
		setupButtonBar();
	}

	/**
	 * Assigns listeners to the buttons on the editing bar and hides the bar.
	 */
	private void setupButtonBar() {
		buttonBar_
				.setOnClickListener(mapView_.new ButtonBarListener(buttonBar_));
		this.hideButtonBar();
	}

	public void hideButtonBar() {
		buttonBar_.setVisibility(View.GONE);
	}

	public void showButtonBar() {
		buttonBar_.setVisibility(View.VISIBLE);
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ProgramConstants.MENU_QUIT, 0, "Quit");
		menu.add(0, ProgramConstants.MENU_ADD_OR_SAVE_ZONE, 0, "Add Zone");
		menu.add(0, ProgramConstants.MENU_PICK_ZONE, 0, "Edit Zone");
		menu.add(0, ProgramConstants.MENU_TOGGLE_VIEW, 0, "Toggle View");
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem editItem = menu.getItem(1);

		if (LogicController.isAddingZone())
			editItem.setTitle("Submit Zone");
		else
			editItem.setTitle("Add Zone");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case ProgramConstants.MENU_QUIT:
			showDialog(ProgramConstants.DIALOG_QUIT);
			return true;
		case ProgramConstants.MENU_ADD_OR_SAVE_ZONE:
			if (LogicController.isAddingZone()) {
				boolean wasStopped = mapView_.stopEdit();
				if (wasStopped == false)
				{
					this
							.message(
									"Zone not submitted, invalid line from last pin to first pin",
									true);
				}
				else
					this.hideButtonBar();
			} else {
				this.showButtonBar();
				mapView_.startEdit();
			}

			return true;
		case ProgramConstants.MENU_PICK_ZONE:
			if (LogicController.isAddingZone()) {
				Toast.makeText(this, "Can't edit a zone while creating one",
						Toast.LENGTH_LONG).show();
				return true;
			}
			if (mapView_.numberTouchableZones() == 0) {
				Toast.makeText(this, "No zones to edit", Toast.LENGTH_SHORT)
						.show();
				return true;
			}

			Toast.makeText(this, "Click on a zone to edit its settings.",
					Toast.LENGTH_LONG).show();
			LogicController.setPickingZone(true);

			return true;
		case ProgramConstants.MENU_TOGGLE_VIEW:
			mapView_.setSatellite(!mapView_.isSatellite());
		}

		return true;
	}

	public Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case ProgramConstants.DIALOG_QUIT:
			dialog = DialogFactory.createQuitDialog(this);
			break;
		case ProgramConstants.DIALOG_ROUTING:
			dialog = DialogFactory.createRoutingDialog(this);
			break;
		}

		return dialog;
	}

	public void onPrepareDialog(int id, Dialog dialog) {
		if (id != ProgramConstants.DIALOG_ROUTING)
			return;

		// Update the routing dialog
		DialogFactory.updateRoutingDialog(dialog);
	}

	public void message(String str, boolean showLong) {
		int duration = Toast.LENGTH_SHORT;
		if (showLong)
			duration = Toast.LENGTH_LONG;

		Toast.makeText(this, str, duration).show();
	}
}