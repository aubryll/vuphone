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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.services.UpdateContactsService;

import android.util.Log;

public class HTTPPoster {

	// Chris's test server\\
	// private static final String SERVER = "http://dorm.cmthompson.net";
	// private static final String PATH = "/wreckwatch/test.php";


	//Chris's test server\\
	//	private static final String SERVER = "http://dorm.cmthompson.net";
	//	private static final String PATH = "/wreckwatch/test.php";

	//Jules's Jetty server\\
	//Note this is equiv to localhost although the phone has to have an
	//IP because it's not running it! :)
	//private static final String SERVER = "http://129.59.135.165";
	private static final String PATH = "/wreckwatch/notifications";

	private static final String LOG_LABEL = "VUPHONE";
	private static final String LOG_MSG_PREFIX = "HTTPPoster: ";

	// There might be a better way to do these methods....
	/**
	 * This method will be responsible for posting data to the WreckWatch
	 * server and executing a Runnable if the post is successful.
	 * 
	 * @param message
	 * @param okRun		A Runnable to execute if the response comes back with a 200 OK status
	 */
	public static HttpResponse doAccidentPost(String androidid, Long time, Double speed, Double dec,
			double lat, double lon) {

		String timeStr = Long.toString(time.longValue());
		String speedStr = Double.toString(speed.doubleValue());
		String decStr = Double.toString(dec.doubleValue());

		try {
			timeStr = URLEncoder.encode(time.toString(), "UTF-8");
			speedStr = URLEncoder.encode(speed.toString(), "UTF-8");
			decStr = URLEncoder.encode(dec.toString(), "UTF-8");
			androidid = URLEncoder.encode(androidid, "UTF-8");
		} catch (UnsupportedEncodingException use) {
			Log.w(LOG_LABEL,
					"HTTPPoster unable to encode one of the parameters");
		}

		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Entering HTTPPoster.doAccidentPost");
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(VUphone.SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");

		StringBuffer params = new StringBuffer();
		
		// Create the parameter string
		params.append("type=accident&user="+androidid+"&time="+ timeStr +"&speed="+speedStr+"&dec="+decStr+"&lat="
				+ lat + "&lon="+lon);

		
		// Add the parameters
		Log
				.v(LOG_LABEL, LOG_MSG_PREFIX + "Created parameter string: "
						+ params);
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Do it
		Log.i(LOG_LABEL, LOG_MSG_PREFIX + "Executing post to " + VUphone.SERVER + PATH);
		Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Spawning thread for HTTP post");

		
		HttpResponse resp = null;
		try {
			resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Response from server: "
					+ new String(bao.toByteArray()));					
		} catch (ClientProtocolException e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX
					+ "ClientProtocolException executing post: "
					+ e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX
					+ "IOException writing to ByteArrayOutputStream: "
					+ e.getMessage());
		} catch (Exception e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX
					+ "Other Exception of type:" + e.getClass());
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "The message is: "
					+ e.getMessage());
		}
		
		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Leaving HTTPPoster.doAccidentPost");
		return resp;
	}
	
	public static HttpResponse doRoutePost(String androidID, List<Waypoint> route){
		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Entering HTTPPoster.doRoutePost");
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(VUphone.SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");

		StringBuffer params = new StringBuffer();
		
		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Constructing parameter string");
		params.append("type=route&id="+androidID);
		
		for (Waypoint w:route){
			params.append("&lat="+w.getLatitudeDegrees()+"&lon="+w.getLongitudeDegrees()+"&timert="+w.getTime());
		}
		
		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Created parameter string: " + params.toString());
		
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));
		
		Log.i(LOG_LABEL, LOG_MSG_PREFIX + "Executing post to " + VUphone.SERVER + PATH);
		Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Spawning thread for HTTP post");

		HttpResponse resp = null;
		try {
			resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Response from server: "
					+ new String(bao.toByteArray()));

		} catch (ClientProtocolException e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX
					+ "ClientProtocolException executing post: "
					+ e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX
					+ "IOException writing to ByteArrayOutputStream: "
					+ e.getMessage());
		} catch (Exception e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX
					+ "Other Exception of type:" + e.getClass());
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "The message is: "
					+ e.getMessage());
		}

		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Leaving HTTPPoster.doRoutePost");
		return resp;
	}

	
	public static HttpResponse doContactUpdate(String deviceID, ArrayList<String> numbers){
		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Entering HTTPPoster.doContactUpdate");
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(VUphone.SERVER + PATH);
		post.addHeader("Content-Type","application/x-www-form-urlencoded");

		StringBuffer params = new StringBuffer();
		
		try {
			params.append("type=contact&id="+URLEncoder.encode(deviceID, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		for (int i = 0; i < numbers.size(); ++i){
			params.append("&number="+numbers.get(i));
		}
		//Add the parameters
		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Created parameter string: " + params);
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));


		//Do it
		Log.i(LOG_LABEL, LOG_MSG_PREFIX + "Executing post to " + VUphone.SERVER + PATH);
		Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Spawning thread for HTTP post");

		HttpResponse resp = null;
		
		try {
			resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Response from server: " + new String(bao.toByteArray()));
		} catch (ClientProtocolException e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "ClientProtocolException executing post: " + e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "IOException writing to ByteArrayOutputStream: " + e.getMessage());
		} catch (Exception e){
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "Other Exception of type:"+e.getClass());
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "The message is: "+e.getMessage());
		}
		
		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Leaving HTTPPoster.doContactUpdate");
		return resp;
	}


}