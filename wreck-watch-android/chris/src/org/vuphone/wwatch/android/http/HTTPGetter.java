/**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.wwatch.android.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.wwatch.android.VUphone;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class HTTPGetter {
	// Chris's test server\\
	// private static final String SERVER = "http://dorm.cmthompson.net:8081";
	// private static final String PATH = "/wreckwatch/notifications";

	// Jules's Jetty server\\
	// Note this is equiv to localhost although the phone has to have an
	// IP because it's not running it! :)

	private static final String PATH = "/wreckwatch/notifications";

	private static final String LOG_LABEL = "VUPHONE";
	private static final String LOG_MSG_PREFIX = "HTTPGetter: ";

	// There might be a better way to do these methods....
	/**
	 * This method will be responsible for posting data to the WreckWatch
	 * server.
	 * 
	 * @param message
	 */
	public static void doAccidentGet(final GeoPoint br,
			final GeoPoint tl, final long maxTime,
			final HttpOperationListener listener) {

		try {

			Log.v(LOG_LABEL, LOG_MSG_PREFIX
					+ "Entering HTTPGetter.doAccidentGet");
			final HttpClient c = new DefaultHttpClient();


			String params = "?type=info&latbr="
					+ br.getLatitudeE6() + "&lonbr=" + br.getLongitudeE6()
					+ "&lattl=" + tl.getLatitudeE6() + "&lontl="
					+ tl.getLongitudeE6();

			final HttpGet get = new HttpGet(VUphone.getServer() + PATH + params);

			// Add the parameters
			Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Created parameter string: "
					+ params);


			//Do it
			Log.i(LOG_LABEL, LOG_MSG_PREFIX + "Executing get to " + VUphone.getServer() + PATH + params);

			Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Spawning thread for HTTP get");
			new Thread(new Runnable() {

				public void run() {
					HttpResponse resp;
					try {
						Log
								.d(
										LOG_LABEL,
										LOG_MSG_PREFIX
												+ "Requesting accident information for coordinates: TopLeft: "
												+ tl.getLatitudeE6() + ", "
												+ tl.getLongitudeE6()
												+ "\nBottomRight: "
												+ br.getLatitudeE6() + ", "
												+ br.getLongitudeE6()
												+ "\nTime: " + maxTime);
						resp = c.execute(get);

						listener.operationComplete(resp);

					} catch (ClientProtocolException e) {
						Log.e(LOG_LABEL, LOG_MSG_PREFIX
								+ "ClientProtocolException executing post: "
								+ e.getMessage());
					} catch (IOException e) {
						Log
								.e(
										LOG_LABEL,
										LOG_MSG_PREFIX
												+ "IOException writing to ByteArrayOutputStream: "
												+ e.getMessage());
					} catch (Exception e) {
						Log.e(LOG_LABEL, LOG_MSG_PREFIX
								+ "Other Exception of type:" + e.getClass());
						Log.e(LOG_LABEL, LOG_MSG_PREFIX + "The message is: "
								+ e.getMessage());
						e.printStackTrace();
					}
				}

			}).start();
			Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Thread for HTTP post started");

		} catch (Exception e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "Other Exception of type:"
					+ e.getClass());
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "The message is: "
					+ e.getMessage());

		}

		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Leaving HTTPGetter.doAccidentGet");
	}

	public static void doPictureGet(int wreckID, final HttpOperationListener list) {

		final HttpClient c = new DefaultHttpClient();
		String params = "?type=imageRequest&wreckID=" + wreckID;
		Log
				.d(LOG_LABEL, LOG_MSG_PREFIX + "Params for doPictureGet = "
						+ params);
		final HttpGet get = new HttpGet(VUphone.getServer() + PATH + params);

		new Thread(new Runnable() {
			public void run() {
				try {
					HttpResponse resp = c.execute(get);
					list.operationComplete(resp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "PictureGetter").start();
	}
}
