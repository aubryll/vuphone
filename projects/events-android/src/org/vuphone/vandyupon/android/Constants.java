/**
 * 
 */
package org.vuphone.vandyupon.android;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Contains global application constants.
 * 
 * @author Hamilton Turner
 * 
 */
public class Constants {
	/** Used for logging */
	public static final String tag = "Vandy Upon";

	/** Server location */
	public static final String SERVER = "http://afrl-gift.dre.vanderbilt.edu:8081";

	/**
	 * Used to have global constants for various activity results. Note that
	 * these correlate to the default activity RESULT_OK and RESULT_CANCELLED,
	 * to help prevent accidents
	 */
	public static final int RESULT_OK = Activity.RESULT_OK;
	public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
	public static final int RESULT_UNKNOWN = RESULT_CANCELED + 1;
	
	/** Used to declare that a filter was cleared */
	public static final int RESULT_CLEAR = RESULT_CANCELED + 2;
	
	/** Used to declare that a filter was updated */
	public static final int RESULT_UPDATE = RESULT_CANCELED + 3;

	/** Center of Vanderbilt */
	public static final GeoPoint vandyCenter = new GeoPoint(
			(int) (36.143792 * 1E6), (int) (-86.803279 * 1E6));

	/** Gets the unique device ID in a uniform way */
	public static String getAndroidID(Context c) {
		String aid = ((TelephonyManager) c
				.getSystemService(Service.TELEPHONY_SERVICE)).getDeviceId();
		return aid;
	}
}
