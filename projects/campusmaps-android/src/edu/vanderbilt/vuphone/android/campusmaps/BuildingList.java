package edu.vanderbilt.vuphone.android.campusmaps;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

public class BuildingList extends ListActivity {
	
	// List of buildings -- should get populated at boot
	private ArrayList<Building> building_ = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Parse any input parameters
		}
		
		building_ = SharedData.getInstance().getBuildingList();
		if(building_ == null || building_.size() < 1){
			echo("Building list not found!");
		} else{
			// TODO Create custom adapter so we can show more than just the building name
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
					getBuildingList(null)));
		}
			
		Log.d("mad","Buildings loaded: " + building_.size());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		// Get the clicked building
		Building b = building_.get(position);
		
		//TODO open a menu that asks what they want to do
		
		//Drop a pin
		Main.getInstance().drop_pin(b.getLocation());
		
		super.finish();
	}
	
/**
 * Return the list of buildings near coordinates. I
 * @param point - measure distance from building to this point (can be null)
 * @return
 */
	public ArrayList<String> getBuildingList(GeoPoint point) {
		if(point == null){
			// Don't  measure the distance
		}
		
		// Extract the building names for now
		ArrayList<String> list_ = new ArrayList<String>();
		Iterator<Building> i = building_.iterator();
		while(i.hasNext()){
			list_.add(((Building)i.next()).getName());
		}
		
		return list_;
	}
	
	/**
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}
}
