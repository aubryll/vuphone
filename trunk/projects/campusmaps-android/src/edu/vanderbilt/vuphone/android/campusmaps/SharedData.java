package edu.vanderbilt.vuphone.android.campusmaps;

import java.util.ArrayList;

/**
 * Singleton for holding data passed between activities
 */
public class SharedData {
	private static SharedData instance_ = null;
	private static ArrayList<Building> buildingList_ = null;

	protected SharedData() {
	}

	/**
	 * Singleton instantiator
	 */
	public static SharedData getInstance() {
		if (instance_ == null)
			instance_ = new SharedData();

		return instance_;
	}

	/**
	 * Accessor for building data
	 * 
	 * @return list of buildings array
	 */
	public ArrayList<Building> getBuildingList() {
		if (buildingList_ == null)
			buildingList_ = new ArrayList<Building>();

		return buildingList_;
	}

}
