package org.vuphone.wwatch.android;

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
}
