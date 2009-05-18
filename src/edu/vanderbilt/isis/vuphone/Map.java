package edu.vanderbilt.isis.vuphone;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.MapActivity;

import edu.vanderbilt.isis.R;
import edu.vanderbilt.isis.trixbox.TrixboxManipulator;

class MenuConstants {
	static final int QUIT = 1;
	static final int ADD_OR_SAVE_ZONE = 2;
	static final int EDIT_ZONE = 3;
}

class DialogConstants {
	static final int QUIT = 4;
	static final int ROUTING = 5;
}

public class Map extends MapActivity {

	private ZoneMapView mapView_ = null;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapView_ = (ZoneMapView) findViewById(R.id.mapview);

		// When they click the add button, tell mapView
		final Button button = (Button) findViewById(R.id.addPin);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ZoneMapController.setAddingPin(true);
			}
		});

	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MenuConstants.QUIT, 0, "Quit");
		menu.add(0, MenuConstants.ADD_OR_SAVE_ZONE, 0, "Add Zone");
		menu.add(0, MenuConstants.EDIT_ZONE, 0, "Edit Zone");
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem editItem = menu.getItem(1);

		if (ZoneMapController.getEditingZone())
			editItem.setTitle("Save Zone");
		else
			editItem.setTitle("Add Zone");
		
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case MenuConstants.QUIT:
			showDialog(DialogConstants.QUIT);
			break;
		case MenuConstants.ADD_OR_SAVE_ZONE:
			if (ZoneMapController.getEditingZone()) {
				boolean isValid = mapView_.stopEdit();
				if (isValid) {
					ZoneMapController.setAddingPin(false);
					Button b = (Button) findViewById(R.id.addPin);
					b.setVisibility(Button.INVISIBLE);
				}
			} else {
				mapView_.startEdit();
				Button b = (Button) findViewById(R.id.addPin);
				b.setVisibility(Button.VISIBLE);
			}
			break;
		case MenuConstants.EDIT_ZONE:
			// TODO - capture the return value
			ZoneMapController.setSelectingZone(true);
			break;
		}

		return true;
	}

	public Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case DialogConstants.QUIT:
			dialog = DialogFactory.createQuitDialog(this);
			break;
		case DialogConstants.ROUTING:
			dialog = DialogFactory.createRoutingDialog(this);
			break;
		}

		return dialog;
	}

	public void onPrepareDialog(int id, Dialog dialog) {
		if (id != DialogConstants.ROUTING)
			return;

		// Update the routing dialog
		DialogFactory.updateRoutingDialog(dialog);
	}

	public void debug(String str) {
		((TextView) Map.this.findViewById(R.id.debug)).setText(str);
	}

	public void setMessage(String str) {
		((TextView) findViewById(R.id.message)).setText(str);
	}
}