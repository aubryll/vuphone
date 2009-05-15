package edu.vanderbilt.isis.vuphone;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.maps.MapActivity;

import edu.vanderbilt.isis.R;
import edu.vanderbilt.isis.trixbox.TrixboxManipulator;

class MenuConstants{
	static final int QUIT		= 1;
	static final int EDIT_ZONE	= 2;
}

class DialogConstants{
	static final int QUIT		= 3;
	static final int ROUTING	= 4;
}

public class Map extends MapActivity {

	private ZoneMapView mapView_ = null;
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mapView_ = (ZoneMapView) findViewById(R.id.mapview);
    }
    
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, MenuConstants.QUIT, 0, "Quit");
        menu.add(0, MenuConstants.EDIT_ZONE, 0, "Edit Zone");
        menu.add(0, 10101, 0, "Fire Trixbox event");
    	return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
    	MenuItem editItem = menu.getItem(1);
    	
    	if (mapView_.isEditing())
    		editItem.setTitle("Submit Zone");
    	else
    		editItem.setTitle("Edit Zone");
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	switch(item.getItemId()){
    	case MenuConstants.QUIT:
    		showDialog(DialogConstants.QUIT);
    		return true;
    	case MenuConstants.EDIT_ZONE:
    		if (mapView_.isEditing())
    			mapView_.stopEdit();
    		else
    			mapView_.startEdit();
    		return true;
    	case 10101:
    		String name = "Zone 1";
    		String config = ZoneManager.getInstance().getSettings(name).getStringA();
    		TrixboxManipulator.doPost(config);
    		return true;
    	}
    	
    	return true;
    }
    	
	public Dialog onCreateDialog(int id){
		Dialog dialog = null;
		switch (id){
		case DialogConstants.QUIT:
			dialog = DialogFactory.createQuitDialog(this);
			break;
		case DialogConstants.ROUTING:
			dialog = DialogFactory.createRoutingDialog(this);			
			break;
		}
		
		return dialog;
	}

	public void onPrepareDialog(int id, Dialog dialog){
		if (id != DialogConstants.ROUTING)
			return;
		
		// Update the routing dialog
		DialogFactory.updateRoutingDialog(dialog);
	}
	
	public void debug(String str){
		((TextView) Map.this.findViewById(R.id.debug)).setText(str);
	}
	
	public void setMessage(String str){
		((TextView) findViewById(R.id.message)).setText(str);
	}
}