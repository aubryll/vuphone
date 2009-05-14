package edu.vanderbilt.isis.vuphone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.maps.MapActivity;

import edu.vanderbilt.isis.R;


public class Map extends MapActivity {

	private final int MENU_ADD_POINT = 0;
	private final int MENU_QUIT = 1;
	private final int MENU_ROUTING = 2;
	
	private int MODE_NAVIGATE = 0;
	private int MODE_PIN = 1;
	
	private int mode_ = MODE_NAVIGATE;
	
	private final int QUIT_DIALOG = 0;
	private final int ROUTING_DIALOG = 1;
	
	private ZoneMapView mapView_ = null;
	private RoutingSettings setting_ = null; // Temp object to test the dialog
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mapView_ = (ZoneMapView) findViewById(R.id.mapview);
        
        setting_ = new RoutingSettings();
    }
    
    public boolean dispatchTouchEvent(MotionEvent event){
    	if (mode_ == MODE_PIN){
    		mapView_.addPinEvent(event);
    		mode_ = MODE_NAVIGATE;
    		return true;
    	}else{    	
    		return super.dispatchTouchEvent(event);
    	}
    }
    
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0, MENU_ADD_POINT, 0, "Add Point");
        menu.add(0, MENU_QUIT, 0, "Quit");
        menu.add(0, MENU_ROUTING, 0, "Routing");
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case MENU_ADD_POINT:
    		mode_ = MODE_PIN;
    		return true;
    	case MENU_QUIT:
    		showDialog(QUIT_DIALOG);
    		return true;
    	case MENU_ROUTING:
    		showDialog(ROUTING_DIALOG);
    		return true;
    	}
    	
    	return true;
    }
    	
	public Dialog onCreateDialog(int id){
		Dialog dialog = null;
		switch (id){
		case QUIT_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			           public void onClick(DialogInterface dialog, int id){
			                Map.this.finish();
			           }
			       })
			       .setNegativeButton("No", null);
							/*new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });*/
			dialog = builder.create();

			break;
		case ROUTING_DIALOG:
			dialog = new RoutingDialog(this, setting_);
			dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
				public void onDismiss(DialogInterface dialog){
					debug(setting_.toString());
				}
			});
			
			break;
		}
		
		return dialog;
	}
	
	public void debug(String str){
		((TextView) Map.this.findViewById(R.id.debug)).setText(str);
	}
}