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
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.assassins.android.GameArea;
import org.vuphone.assassins.android.GameObjects;
import org.vuphone.assassins.android.VUphone;
import org.vuphone.assassins.android.landmine.LandMine;
import org.xml.sax.InputSource;

import android.util.Log;

/**
 * This class is responsible for all Get requests to the Assassins server.
 * It is assumed that most calls to these methods will be threaded, since
 * they can take a long time to return.
 * 
 * @author Scott Campbell
 */
public class HTTPGetter {
	
	private static String pre = "HTTPGetter: ";
	
	private static final String PATH = "/assassins/notifications";
	
	private static final HttpClient c = new DefaultHttpClient();
	
	private static LandMineHandler landMineHandler_ = new LandMineHandler();
	
	public static HashMap<String, Double> doGameAreaGet() {
		
		Log.d(VUphone.tag, pre + "doGameAreaGet called.");
		
		String params = "?type=gameAreaGet";
		
		final HttpGet get = new HttpGet(VUphone.SERVER + PATH + params);
		Log.i(VUphone.tag, pre + "Executing get to " + VUphone.SERVER + PATH + params);
		
		return handleGameAreaResponse(get);
	}

	public static ArrayList<LandMine> doLandMineGet() {

		Log.d(VUphone.tag, pre + "doLandMineGet called.");
		
		String params = "?type=landMineGet";
		
		final HttpGet get = new HttpGet(VUphone.SERVER + PATH + params);
		Log.i(VUphone.tag, pre + "Executing get to " + VUphone.SERVER + PATH + params);
		
		return handleLandMineResponse(get);
	}
	
	private static HashMap<String, Double> handleGameAreaResponse(HttpGet get) {
		
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
			Log.e(VUphone.tag, pre + "SocketException: Invalid Internet " +
					"Connection");
			se.printStackTrace();
			return null;
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
		
		if (bao.size() == 0) {
			Log.w(VUphone.tag, pre + "Response was completely empty, "
					+ "are you sure you are using the "
					+ "same version client and server? "
					+ "At the least, there should have "
					+ "been empty XML here");
		}
		
		HashMap<String, Double> data = new HashMap<String, Double>();
		
		String response = bao.toString();
		String[] vals = response.split("&");
		for (int i = 0; i < vals.length; i++) {
			String[] val = vals[i].split("=");
			data.put(val[0], Double.valueOf(val[1]));
		}
		
		return data;
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

		// Extract Land Mines from response
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
 
		return mines;
		//return GameObjects.getInstance().getLandMines();
		//return null;
	}
}
