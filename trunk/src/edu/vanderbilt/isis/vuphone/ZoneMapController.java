package edu.vanderbilt.isis.vuphone;

public class ZoneMapController {

	// Used to indicate editing of some sort, be it creating a new zone or
	// editing a created zone
	private static boolean editingZone_ = false;
	private static boolean addingPin_ = false;
	private static boolean selectingZone_ = false;

	private ZoneMapController() {}

	public static boolean getAddingPin() {
		return addingPin_;
	}

	public static boolean getEditingZone() {
		return editingZone_;
	}

	public static boolean getSelectingZone() {
		return selectingZone_;
	}

	//===========================
	//      Setters
	//===========================
	
	public static void setAddingPin(boolean newValue) {
		if (newValue && editingZone_)
			addingPin_ = true;
		else
			addingPin_ = false;
	}

	public static void setEditingZone(boolean newValue) {
		editingZone_ = newValue;
	}

	public static boolean setSelectingZone(boolean newValue) {
		if (editingZone_ || newValue == false)
		{
			selectingZone_ = false;
			return false;
		}
		selectingZone_ = true;
		return true;
	}
}
