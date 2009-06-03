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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.wwatch.android.Waypoint;

import android.util.Log;



public class HTTPPoster {


	//Chris's test server\\
	//	private static final String SERVER = "http://dorm.cmthompson.net";
	//	private static final String PATH = "/wreckwatch/test.php";

	//Jules's Jetty server\\
	//Note this is equiv to localhost although the phone has to have an
	//IP because it's not running it! :)
	private static final String SERVER = "http://129.59.135.149";
	private static final String PATH = "/wreckwatch/notifications";

	private static final String LOG_LABEL = "VUPHONE";
	private static final String LOG_MSG_PREFIX = "HTTPPoster: ";

	//There might be a better way to do these methods....
	/**
	 * This method will be responsible for posting data to the
	 * WreckWatch server.
	 * @param message
	 */
	public static void doAccidentPost(Long time, Double speed, Double dec, List<Waypoint> route){

		try{
			String timeStr = URLEncoder.encode(time.toString(), "UTF-8");
			String speedStr = URLEncoder.encode(speed.toString(), "UTF-8");
			String decStr = URLEncoder.encode(dec.toString(), "UTF-8");


			Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Entering HTTPPoster.doAccidentPost");
			final HttpClient c = new DefaultHttpClient();
			final HttpPost post = new HttpPost(SERVER + PATH);
			post.addHeader("Content-Type","application/x-www-form-urlencoded");

			StringBuffer params = new StringBuffer();

			//Create the parameter string
			if (route != null){
				params.append("type=accident&user=thompchr%40gmail.com&time="+timeStr+"&speed="+speedStr+"&dec="+decStr+"&numpoints="+route.size());

				for (int i = 0; i < route.size(); ++i){
					params.append("&lat"+i+"="+route.get(i).getLatitude()+"&lon"+i+"="+route.get(i).getLongitude()+"&time"+i+"="+route.get(i).getTime());
				}
			}else{
				params.append("type=accident&user=thompchr%40gmail.com&time="+ timeStr +"&speed="+speedStr+"&dec="+decStr+"&numpoints=0");
			}



			//Add the parameters
			Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Created parameter string: " + params);
			post.setEntity(new ByteArrayEntity(params.toString().getBytes()));


			//Do it
			Log.i(LOG_LABEL, LOG_MSG_PREFIX + "Executing post to " + SERVER + PATH);
			Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Spawning thread for HTTP post");
			new Thread(new Runnable(){

				
				public void run() {
					HttpResponse resp;
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
				}

			}).start();
			Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Thread for HTTP post started");

		} 
		catch (Exception e) {
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "Other Exception of type:"+e.getClass());
			Log.e(LOG_LABEL, LOG_MSG_PREFIX + "The message is: "+e.getMessage());

		}

		Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Leaving HTTPPoster.doAccidentPost");
	}
	
	public static void doContactUpdate(String deviceID, ArrayList<String> numbers){
		
	}


}