package edu.vanderbilt.isis.vuphone;

/**
 * A class of static methods that keeps track of the state of editing of zones. 
 * 
 * Based on Hamilton Turner's ZoneMapController. Renamed to avoid confusion with MapController.
 * @author Krzysztof Zienkiewicz
 *
 */

public class LogicController{

	private static boolean addingZone_ 	= false;
	private static boolean addingPin_ 	= false;
	private static boolean pickingZone_	= false;

	/**
	 * Private constructor to disable instantiation.
	 */
	private LogicController(){}

	/**
	 * Checks whether we are currently adding a pin. This will only
	 * return true if isAddingZone() returns true.
	 * @return
	 */
	public static boolean isAddingPin(){
		return addingPin_;
	}

	/**
	 * Checks whether we are currently adding a zone. 
	 * @return
	 */
	public static boolean isAddingZone(){
		return addingZone_;
	}

	/**
	 * Checks whether we are currently in the process of selecting a zone.
	 * @return
	 */
	public static boolean isPickingZone() {
		return pickingZone_;
	}
	
	/**
	 * Sets whether or not we are adding a pin and returns a flag representing the
	 * success of the operation. setAddingPin(true) will only be successful if
	 * isAddingZone() is true. 
	 *  
	 * @param isAdding
	 * @return
	 */
	public static boolean setAddingPin(boolean isAdding){
		return addingPin_ = isAdding && addingZone_;
	}

	/**
	 * Sets whether or not we are currently creating a zone.
	 * @param newValue
	 */
	public static void setAddingZone(boolean newValue){
		addingZone_ = newValue;
	}

	/**
	 * Sets whether or not we are currently picking a zone and returns the success
	 * of the operation. setPickingZone(true) will be successful only if isAddingZone()
	 * returns false. 
	 * 
	 * @param isPicking
	 * @return
	 */
	public static boolean setPickingZone(boolean isPicking){
		if (addingZone_ || !isPicking)
			return pickingZone_ = false;
		
		return pickingZone_ = true;
	}
}
