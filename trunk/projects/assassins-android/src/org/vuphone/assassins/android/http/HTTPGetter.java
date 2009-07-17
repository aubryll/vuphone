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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.assassins.android.GameObjects;
import org.vuphone.assassins.android.VUphone;
import org.vuphone.assassins.android.landmine.LandMine;
import org.xml.sax.InputSource;

import android.util.Log;

public class HTTPGetter {
	
	private static String pre = "HTTPGetter: ";
	
	private static final String PATH = "/assassins/notifications";
	
	private static final HttpClient c = new DefaultHttpClient();
	
	private static LandMineHandler landMineHandler_ = new LandMineHandler();

	public static ArrayList<LandMine> doLandMineGet() {

		Log.d(VUphone.tag, pre + "doLandMineGet called.");
		
		String params = "?type=landMineGet";
		
		final HttpGet get = new HttpGet(VUphone.SERVER + PATH + params);
		Log.i(VUphone.tag, pre + "Executing get to " + VUphone.SERVER + PATH + params);
		
		return handleLandMineResponse(get);
	}
	
	private static ArrayList<LandMine> handleLandMineResponse(HttpGet get) {
		
		HttpResponse resp;

		// Execute the get
		try {
			resp = c.execute(get);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			Log.e(VUphone.tag, pre + "HTTP error while executing post");
			return null;
		} catch (SocketException se) {
			// If we have no Internet connection, we don't want to wipe the
			// existing list of land mines by returning null.
			Log.e(VUphone.tag, pre + "SocketException: handled by " +
					"returning current land mine list.");
			se.printStackTrace();
			return GameObjects.getInstance().getLandMines();
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.e(VUphone.tag, pre + "HTTP error in server response");
			return null;
		} catch (Exception e) {
			Log.e(VUphone.tag, pre + "An unknown exception was thrown");
			e.printStackTrace();
			return null;
		}
		
		Log.i(VUphone.tag, pre + "HTTP operation complete. Processing response.");

		// Convert Response Entity to usable format
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			resp.getEntity().writeTo(bao);
			Log.v(VUphone.tag, pre + "Http response: " + bao);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(VUphone.tag, pre + "Unable to write response to byte[]");
			return null;
		}

		// Extract Routes from response
		if (bao.size() == 0) {
			Log.w(VUphone.tag, pre + "Response was completely empty, "
					+ "are you sure you are using the "
					+ "same version client and server? "
					+ "At the least, there should have "
					+ "been empty XML here");
		}

		ArrayList<LandMine> mines = new ArrayList<LandMine>(
				landMineHandler_.processXML(new InputSource(
						new ByteArrayInputStream(bao.toByteArray()))));

		//		landMineHandler_.processXML(new InputSource(
		//				new ByteArrayInputStream(bao.toByteArray()))));
 
		return mines;
		//return GameObjects.getInstance().getLandMines();
		//return null;
	}
}
