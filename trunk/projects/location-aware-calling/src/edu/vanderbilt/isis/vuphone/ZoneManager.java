package edu.vanderbilt.isis.vuphone;

import java.util.HashMap;



/**
 * A singleton that manages some aspect of Zone configurations. This class handles
 * mapping between RoutingSettings and zone names as well holds a reference to the
 * zone whose settings are currently being edited (needed for proper dialog
 * behavior). 
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class ZoneManager{
	
	private static ZoneManager instance_ = null;
	
	private Zone editing_ = null;
	private HashMap<String, RoutingSettings> settingsMap_ = null;
	
	/**
	 * Private constructor.
	 */
	private ZoneManager(){
		settingsMap_ = new HashMap<String, RoutingSettings>();
	}
	
	/**
	 * Throws an exception
	 */
	public Object clone() throws CloneNotSupportedException{
	    throw new CloneNotSupportedException("Can't copy a Singleton"); 
	}
	
	/**
	 * Returns a reference to the Zone that is to be edited next.
	 * @return
	 */
	public Zone getEditing(){
		return editing_;
	}
	
	/**
	 * Fetches the RoutingSettings object for a Zone object identified by name.
	 * If this was not saved before with setSettings() then null is returned.
	 * @param name
	 * @return
	 */
	public RoutingSettings getSettings(String name){
		if (!settingsMap_.containsKey(name))
			return null;
		
		return settingsMap_.get(name);
	}
	
	/**
	 * Returns an instance of a ZoneManager
	 * @return
	 */
	public static ZoneManager getInstance(){
		if (instance_ == null)
			instance_ = new ZoneManager();
		return instance_;
	}
	
	/**
	 * Sets a reference to the Zone to be edited next.
	 * @param zone
	 */
	public void setEditing(Zone zone){
		editing_ = zone;
	}
	
	/**
	 * Saves the setting set and associates it with a Zone identified by name
	 * @param name
	 * @param set
	 */
	public void setSettings(String name, RoutingSettings set){
		settingsMap_.put(name, set);
	}

}
