/**
 * 
 */
package org.vuphone.vandyupon.android;

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

	public static String getAndroidID(Context c) {
		String aid = ((TelephonyManager) c
				.getSystemService(Service.TELEPHONY_SERVICE)).getDeviceId();
		return aid;
	}
}
