/**
 * 
 */
package org.vuphone.vandyupon.android;

import java.util.HashMap;

import com.google.android.maps.GeoPoint;

/**
 * 
 * Probably the worst data structure ever. Stores the groups of locations, along
 * with the locations themselves, and a mapping from a location to a GeoPoint
 * 
 * @author Hamilton Turner
 * 
 */
public class LocationManager {

	/** Center of Vanderbilt */
	public static final GeoPoint vandyCenter_ = new GeoPoint(
			(int) (36.143792 * 1E6), (int) (-86.803279 * 1E6));
	
	/** The top-level categories */
	public static final String groups[] = { "Engineering Buildings",
			"A&S Buildings", "Lawns", "Restaurants near campus", "Other" };

	/** Items in each category */
	public static final String[][] locations = {
			{ "Featheringill", "Stevenson" }, { "Garland", "Calhoun" },
			{ "Alumni", "Wilson" }, { "Qudoba", "Cafe Coco" }, { "Other" } };

	/** Mapping from location names to coordinates */
	public static final HashMap<String, GeoPoint> coordinates = new HashMap<String, GeoPoint>();

	/** Fill in mapping */
	static {
		coordinates.put("Featheringill", new GeoPoint(
				(int) (36.14480610055561 * 1E6),
				(int) (-86.80304288864136 * 1E6)));
		coordinates.put("Stevenson", new GeoPoint(
				(int) (36.14509200691745 * 1E6),
				(int) (-86.80154085159302 * 1E6)));
		coordinates.put("Garland", new GeoPoint(
				(int) (36.14662548689234 * 1E6),
				(int) (-86.80147647857666 * 1E6)));
		coordinates.put("Calhoun", new GeoPoint(
				(int) (36.14732723891594 * 1E6),
				(int) (-86.80132627487183 * 1E6)));
		coordinates.put("Alumni", new GeoPoint((int) (36.14743120164476 * 1E6),
				(int) (-86.80389046669006 * 1E6)));
		coordinates.put("Wilson", new GeoPoint((int) (36.14873072412794 * 1E6),
				(int) (-86.8010687828064 * 1E6)));
		coordinates.put("Qudoba", new GeoPoint((int) (36.15057600905914 * 1E6),
				(int) (-86.80013537406921 * 1E6)));
		coordinates.put("Cafe Coco", new GeoPoint(
				(int) (36.152204675180286 * 1E6),
				(int) (-86.80517792701721 * 1E6)));
	}
}
