package edu.vanderbilt.vuphone.android.campusmaps;

import java.util.HashMap;

/**
 * Singleton for holding data passed between activities
 */
public class SharedData {
	private static SharedData instance_ = null;
	private static HashMap<Integer, Building> buildingList_ = null;

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
	public HashMap<Integer, Building> getBuildingList() {
		if (buildingList_ == null)
			buildingList_ = new HashMap<Integer, Building>();

		return buildingList_;
	}

}
