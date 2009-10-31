/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Adam Albright
 * @date Oct 16, 2009
 * 
 * Copyright 2009 VUPhone Team
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 *  implied. See the License for the specific language governing 
 *  permissions and limitations under the License. 
 */

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
