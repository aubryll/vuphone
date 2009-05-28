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
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
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
	private static final String SERVER = "http://129.59.129.151:8080";
	private static final String PATH = "/wreckwatch/notifications";
	
	private static final String LOG_LABEL = "VUPHONE";

	//There might be a better way to do these methods....
	/**
	 * This method will be responsible for posting data to the
	 * WreckWatch server.
	 * @param message
	 */
	public String doAccidentPost(Long time, Double speed, Double dec, ArrayList<Waypoint> route){

		try{
			String timeStr = URLEncoder.encode(time.toString(), "UTF-8");
			String speedStr = URLEncoder.encode(speed.toString(), "UTF-8");
			String decStr = URLEncoder.encode(dec.toString(), "UTF-8");
			
			
			Log.v(LOG_LABEL, "Entering HTTPPoster.doPost");
			HttpClient c = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVER + PATH);
			post.addHeader("Content-Type","application/x-www-form-urlencoded");

			String params;
			
			//Create the parameter string
			if (route != null){
				params = "type=accident&time="+timeStr+"&speed="+speedStr+"&dec="+decStr+"&numpoints="+route.size();

				for (int i = 0; i < route.size(); ++i){
					params = params + "&point"+i+"="+URLEncoder.encode(route.get(i).toString(), "UTF-8");
				}
			}else{
				params = "type=accident&time="+ timeStr +"&speed="+speedStr+"&dec="+decStr+"&numpoints=0";
			}
			

			//Add the parameters
			Log.v(LOG_LABEL, "Created parameter string: " + params);
			post.setEntity(new ByteArrayEntity(params.getBytes()));

			//Do it
			Log.i(LOG_LABEL, "Executing post to " + SERVER + PATH);
			HttpResponse resp = c.execute(post);

			//Return the HTTP response back to the user in the form of 
			//a String
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			String s = new String(bao.toByteArray());
			Log.d(LOG_LABEL, "Response from server: " + s);
			Log.v(LOG_LABEL, "Leaving HTTPPoster.doPost");
			return s;

		} 
		catch (Exception e) {
			Log.e("VUPHONE", "Other Exception of type:"+e.getClass());
			Log.e("VUPHONE", "The message is: "+e.getMessage());
			return null;
		}

	}


}