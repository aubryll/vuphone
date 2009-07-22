/**
 * 
 */
package org.vuphone.vandyupon.android;

import com.google.android.maps.GeoPoint;

import android.app.Service;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author Hamilton Turner
 * 
 */
public class Constants {
	public static final String tag = "Vandy Upon";
	public static final String SERVER = "http://afrl-gift.dre.vanderbilt.edu:8080";

	/** Center of Vanderbilt */
	public static final GeoPoint vandyCenter = new GeoPoint(
			(int) (36.143792 * 1E6), (int) (-86.803279 * 1E6));
	
	public static String getAndroidID(Context c) {
		String aid = ((TelephonyManager) c
				.getSystemService(Service.TELEPHONY_SERVICE)).getDeviceId();
		return aid;
	}
}
