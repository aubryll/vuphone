package org.vuphone.wwatch.android;

import android.content.Context;

/**
 * A convenience class for storing application wide constants.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */

public class VUphone {
	/**
	 * Tag used for the Log utility. Lower-case because eclipse automatically
	 * fills this in.
	 */
	public static final String tag = "VUPHONE";
	public static final String PREFERENCES_FILE = "WreckWatchPrefs";
	
	public static final String BATTERY_LEVEL_TAG = "WreckWatchBat";
	public static final String TIMEOUT_TAG = "WreckWatchDialogTimeout";
	public static final String LOCATION_TAG = "DefaultLocation";
	public static final String SERVER_TAG = "ServerAddress";
	public static final String SERVER = "http://129.59.129.151:8080";
	
	/**
	 * Used in the preference file to specify the size of the ID list
	 */
	public static final String LIST_SIZE_TAG = "ListSize";
	
	/**
	 * Used in the preference file to mark an ID item
	 */
	public static final String LIST_ITEM_PREFIX_TAG = "ListItem";
	
	public static final String SPEED_SCALE = "SpeedScale";
	public static final String ACCEL_SCALE = "AccelScale";
	
	
	static private Context context_;
	
	public static void setContext(Context c) {
		if (c != null && context_ == null)
			context_ = c;
	}
	
	public static final String getServer() {
		return SERVER;
//		if (context_ == null)
//			throw new IllegalStateException("VUphone.setContext() has not been called");
//		return context_.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).getString(SERVER_TAG, "INVALID");
	}
}
