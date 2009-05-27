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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;



public class HTTPPoster {

	private static final String SERVER = "http://dorm.cmthompson.net";

	/**
	 * This method will be responsible for posting data to the
	 * WreckWatch server.
	 * @param message
	 */
	public String doPost(String message){

		try{
			// Do the first POST to set the FollowMe List
			HttpClient c = new DefaultHttpClient();
			//TODO Figure out what the syntax of the server will be
			HttpPost post = new HttpPost(SERVER + "/wreckwatch/test.php");
			post.addHeader("Content-Type","application/x-www-form-urlencoded");
			message = "msg=" + message;
			post.setEntity(new ByteArrayEntity(message.getBytes()));
			HttpResponse resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			return new String(bao.toByteArray());
			
		} 
		catch (Exception e) {
			Log.v("VUPHONE", "Other Exception of type:"+e.getClass());
			Log.v("VUPHONE", "The message is: "+e.getMessage());
			return null;
		}

	}


}