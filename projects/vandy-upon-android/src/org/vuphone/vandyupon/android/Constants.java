/**
 * 
 */
package org.vuphone.vandyupon.android;

import com.google.android.maps.GeoPoint;

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
