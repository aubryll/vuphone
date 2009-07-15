/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.android.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.assassins.android.VUphone;
import org.vuphone.assassins.android.landmine.LandMine;

import android.util.Log;

public class HTTPPoster {
	
	private static final String pre = "HTTPPoster: ";
	
	private static final String PATH = "/assassins/notifications";
	
	private static final HttpClient c = new DefaultHttpClient();

	public static void doLandMinePost(LandMine lm) {
		
		final HttpPost post = new HttpPost(VUphone.SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		StringBuffer params = new StringBuffer();
		params.append("type=landMinePost&lat="+lm.getLatitude()+
				"&lon="+lm.getLongitude()+"&radius="+lm.getRadius());
		
		Log.v(VUphone.tag, pre + "Created parameter string: "+params);
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Do it
		Log.i(VUphone.tag, pre + "Executing post to " + VUphone.SERVER + PATH);
		
		HttpResponse resp = null;
		try {
			resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			Log.d(VUphone.tag, pre + "Response from server: "
					+ new String(bao.toByteArray()));					
		} catch (ClientProtocolException e) {
			Log.e(VUphone.tag, pre+"ClientProtocolException executing post: "
					+ e.getMessage());
		} catch (IOException e) {
			Log.e(VUphone.tag, pre 
					+ "IOException writing to ByteArrayOutputStream: "
					+ e.getMessage());
		} catch (Exception e) {
			Log.e(VUphone.tag, pre + "Other Exception of type:" 
					+ e.getClass());
			Log.e(VUphone.tag, pre + "The message is: "
					+ e.getMessage());
		}
	}
	
	public static void doLandMineRemove(LandMine lm) {
		
		final HttpPost post = new HttpPost(VUphone.SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		StringBuffer params = new StringBuffer();
		params.append("type=landMineRemove&lat="+lm.getLatitude()+
				"&lon="+lm.getLongitude());
		
		Log.v(VUphone.tag, pre + "Created parameter string: "+params);
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Do it
		Log.i(VUphone.tag, pre + "Executing post to " + VUphone.SERVER + PATH);
		
		HttpResponse resp = null;
		try {
			resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			Log.d(VUphone.tag, pre + "Response from server: "
					+ new String(bao.toByteArray()));					
		} catch (ClientProtocolException e) {
			Log.e(VUphone.tag, pre+"ClientProtocolException executing post: "
					+ e.getMessage());
		} catch (IOException e) {
			Log.e(VUphone.tag, pre 
					+ "IOException writing to ByteArrayOutputStream: "
					+ e.getMessage());
		} catch (Exception e) {
			Log.e(VUphone.tag, pre + "Other Exception of type:" 
					+ e.getClass());
			Log.e(VUphone.tag, pre + "The message is: "
					+ e.getMessage());
		}
	}
}
