/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.android;

import java.util.ArrayList;

import org.vuphone.assassins.android.landmine.LandMine;

import android.content.Context;
import android.util.Log;

/**
 * This class is where we will store all certralized objects that
 * come from the server and are necessary for game play.  For example,
 * it will contain the currently active list of land mines, as well
 * as the list of safe zones or fortresses when we add those.
 * 
 * @author Scott Campbell
 *
 */
public class GameObjects {

	private String pre = "GameObjects: ";
	
	// Use the singleton pattern since there should never be more than
	// one copy of any of these lists.
	
	private static GameObjects instance_;
	
	private GameObjects() {}
	
	public static GameObjects getInstance() {
		if (instance_ == null) {
			instance_ = new GameObjects();
		}
		return instance_;
	}
	
	/**
	 * This is the current authoritative list of active landmines in
	 * the game.
	 */
	private ArrayList<LandMine> landmines_ = new ArrayList<LandMine>();
	
	public synchronized ArrayList<LandMine> getLandMines() {
		return landmines_;
	}
	
	/**
	 * Gets the landmine with the given id.  It does not assume the
	 * landmine is still at position id in the array.
	 * 
	 * Throws IllegalArgumentException if the id is not found in the
	 * array.
	 * 
	 * @param id
	 * @return - the landmine if found, no return if exception thrown.
	 */
	public synchronized LandMine getLandMine(int id) {
		for (int i = 0; i < landmines_.size(); i++) {
			if (landmines_.get(i).getId() == id) {
				return landmines_.get(i);
			}
		}
		throw new IllegalArgumentException("The given id, "+id+", does not "+
				"exist in the list of currently active landmines of size "+
				landmines_.size());
	}
	
	/**
	 * This method should only be used to add a new list of landmines
	 * received from the HTTPGetter.
	 * 
	 * @param mines
	 * @param c - The Context to use to activate the mines.
	 */
	public synchronized void setLandMines(ArrayList<LandMine> mines, Context c) {
		Log.d(VUphone.tag, pre + "setLandMines called.");
		if (mines != null) {
			Log.i(VUphone.tag, pre + "The new land mines are: ");
			for (int i = 0; i < mines.size(); i++) {
				Log.v(VUphone.tag, pre + "Lat: "+mines.get(i).getLatitude());
				Log.v(VUphone.tag, pre + "Lon: "+mines.get(i).getLongitude());
				Log.v(VUphone.tag, pre + "Radius: "+mines.get(i).getRadius());
			}
			for (int i = 0; i < landmines_.size(); i++) {
				boolean found = false;
				for (int j = 0; j < mines.size(); j++) {
					if (landmines_.get(i).equals(mines.get(j))) {
						found = true;
						// if this mine is already in landmines_, don't add it
						Log.i(VUphone.tag, pre + "removing duplicate land " +
								"mine from the list to be added.");
						mines.remove(j);
					}
				}
				if (!found) {
					// if this mine is not in mines, remove it
					Log.i(VUphone.tag, pre + "landmine at "+i+
							"will be deactivated and removed.");
					landmines_.get(i).deactivate(c);
					landmines_.remove(i);
				}
			}

			// Activate the new landmines
			for (int i = 0; i < mines.size(); i++) {
				mines.get(i).setId(i);
				mines.get(i).activate(c);
				Log.i(VUphone.tag, pre + "landmine with id "+i+" created and activated.");
			}

			// Actually set the landmines.
			landmines_.addAll(mines);
		}
		else {
			// Deactivate the old landmines
			Log.i(VUphone.tag, pre + "The new land mines are: null");
			for (int i = 0; i < landmines_.size(); i++) {
				Log.i(VUphone.tag, pre + "landmine at "+i+"will be deactivated and removed.");
				landmines_.get(i).deactivate(c);
			}
			landmines_ = new ArrayList<LandMine>();
		}

	}
	
	/**
	 * Adds the given landmine to the list.  You do not need to activate
	 * the landmine yourself before calling this method.
	 * 
	 * @param mine
	 * @param c - the Context to use to activate the mine
	 * @return - the new id of the land mine after it has been added
	 * 			to the list.
	 */
	public synchronized int addLandMine(LandMine mine, Context c) {
		int id = landmines_.size();
		mine.setId(id);
		mine.activate(c);
		landmines_.add(mine);
		Log.i(VUphone.tag, pre + "landmine with id "+id+" created and activated.");
		return id;
	}
	
	/**
	 * 
	 * @param id
	 * @param c - the Context to use to deactivate the mine
	 * @return - the id of the land mine that was removed from the list.
	 */
	public synchronized int removeLandMine(int id, Context c) {
		Log.i(VUphone.tag, pre + "landmine with id "+id+" will be deactivated and removed.");
		landmines_.get(id).deactivate(c);
		landmines_.remove(id);
		return id;
	}
	
	/** 
	 * 
	 * @param mine
	 * @param c - the Context to use to deactivate the mine
	 * @return - the id of the land mine that was removed from the list.
	 */
	public synchronized int removeLandMine(LandMine mine, Context c) {
		int id = landmines_.get(landmines_.indexOf(mine)).getId();
		return removeLandMine(id, c);
	}
}
