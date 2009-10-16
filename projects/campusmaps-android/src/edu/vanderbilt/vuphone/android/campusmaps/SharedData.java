package edu.vanderbilt.vuphone.android.campusmaps;

import java.util.ArrayList;

/*
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

	public ArrayList<Building> getBuildingList() {
		if(buildingList_ == null)
			buildingList_ = new ArrayList<Building>();
		
//		ArrayList<Building > b = new ArrayList<Building>();
//		b.add(new Building(null,"fgh"));
//		return b;
//		
		return buildingList_;
	}

}
