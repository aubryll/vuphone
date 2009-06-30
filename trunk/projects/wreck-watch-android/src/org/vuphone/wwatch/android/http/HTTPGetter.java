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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.http.parsers.RouteHandler;
import org.vuphone.wwatch.android.http.parsers.WreckHandler;
import org.vuphone.wwatch.android.mapview.FullImageViewer;
import org.vuphone.wwatch.android.mapview.GeoRegion;
import org.vuphone.wwatch.android.mapview.ImageAdapter;
import org.vuphone.wwatch.android.mapview.Route;
import org.xml.sax.InputSource;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class HTTPGetter {
	private static final String tag = VUphone.tag;
	private static final String pre = "HTTPGetter: ";

	private static final String PATH = "/wreckwatch/notifications";

	private static final HttpClient c = new DefaultHttpClient();
	private static final RouteHandler routeHandler_ = new RouteHandler();
	private static final WreckHandler wreckHandler_ = new WreckHandler();

	public static void doPictureGet(int wreckID, final ImageAdapter list) {

		final HttpClient c = new DefaultHttpClient();
		String params = "?type=imageRequest&wreckID=" + wreckID;
		Log.d(tag, pre + "Params for doPictureGet = " + params);
		final HttpGet get = new HttpGet(VUphone.SERVER + PATH + params);

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
	
	public static Route doRouteGet(final long wreckId) {
		Log.v(tag, pre + "Entering doRouteGet");

		// Add the parameters
		String params = "?type=routeGet&wreckd=" + wreckId;
		Log.v(tag, pre + "Created parameter string: " + params);

		final HttpGet get = new HttpGet(VUphone.SERVER + PATH + params);
		Log.i(tag, pre + "Executing get to " + VUphone.SERVER + PATH + params);

		Log.i(tag, pre + "Starting HTTP Get");
		return handleRouteResponse(get);
	}

	/**
	 * This method will be responsible for setting up the GET to the server with
	 * the correct values, and passing the created GET off to be executed
	 * 
	 * @param br
	 *            The bottom right GeoPoint
	 * @param tl
	 *            The top left GeoPoint
	 * @param listener
	 *            The AccidentList to pass the (possibly) new routes to
	 */
	public static ArrayList<Waypoint> doWreckGet(final GeoRegion region,
			long time) {

		Log.v(tag, pre + "Entering doWreckGet");
		if (region == null)
			return null;

		final GeoPoint br = region.getBottomRight();
		final GeoPoint tl = region.getTopLeft();

		// Add the parameters
		String params = "?type=info&latbr=" + br.getLatitudeE6() + "&lonbr="
				+ br.getLongitudeE6() + "&lattl=" + tl.getLatitudeE6()
				+ "&lontl=" + tl.getLongitudeE6() + "&maxtime=" + time;
		Log.v(tag, pre + "Created parameter string: " + params);

		final HttpGet get = new HttpGet(VUphone.SERVER + PATH + params);
		Log.i(tag, pre + "Executing get to " + VUphone.SERVER + PATH + params);

		Log.i(tag, pre + "Starting HTTP Get");
		return handleWreckResponse(br, tl, get);
	}

	public static void doFullPictureGet(int imageID,
			final FullImageViewer viewer) {

		final HttpClient c = new DefaultHttpClient();
		String params = "?type=imageRequest&imageID=" + imageID;
		Log.d(tag, pre + "Params for doFullPictureGet = " + params);
		final HttpGet get = new HttpGet(VUphone.SERVER + PATH + params);

		new Thread(new Runnable() {
			public void run() {
				try {
					HttpResponse resp = c.execute(get);
					viewer.operationComplete(resp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "FullPictureGetter").start();
	}

	private static ArrayList<Waypoint> handleWreckResponse(
			GeoPoint bottomRight, GeoPoint topLeft, HttpGet get) {

		HttpResponse resp;

		Log.d(tag, pre + "Requesting accident information for coordinates:");
		Log.d(tag, pre + "TopLeft: " + topLeft);
		Log.d(tag, pre + "BottomRight: " + bottomRight);

		// Execute the post
		try {
			resp = c.execute(get);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			Log.e(tag, pre + "HTTP error while executing post");
			return null;
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.e(tag, pre + "HTTP error in server response");
			return null;
		} catch (Exception e) {
			Log.e(tag, pre + "An unknown exception was thrown");
			e.printStackTrace();
			return null;
		}
		Log.i(tag, pre + "HTTP operation complete. Processing response.");

		// Convert Response Entity to usable format
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			resp.getEntity().writeTo(bao);
			Log.v(tag, pre + "Http response: " + bao);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(tag, pre + "Unable to write response to byte[]");
			return null;
		}

		// Extract Routes from response
		if (bao.size() == 0) {
			Log.w(tag, pre + "Response was completely empty, "
					+ "are you sure you are using the "
					+ "same version client and server? "
					+ "At the least, there should have "
					+ "been empty XML here");
		}
		
		ArrayList<Waypoint> wrecks = new ArrayList<Waypoint>(wreckHandler_.processXML(new InputSource(
				new ByteArrayInputStream(bao.toByteArray()))));
 
		return wrecks;
	}
	
	private static Route handleRouteResponse(HttpGet get) {

		HttpResponse resp;

		// Execute the post
		try {
			resp = c.execute(get);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
			Log.e(tag, pre + "HTTP error while executing post");
			return null;
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.e(tag, pre + "HTTP error in server response");
			return null;
		}
		Log.i(tag, pre + "HTTP operation complete. Processing response.");

		// Convert Response Entity to usable format
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			resp.getEntity().writeTo(bao);
			Log.v(tag, pre + "Http response: " + bao);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(tag, pre + "Unable to write response to byte[]");
			return null;
		}

		// Extract Routes from response
		if (bao.size() == 0) {
			Log.w(tag, pre + "Response was completely empty, "
					+ "are you sure you are using the "
					+ "same version client and server? "
					+ "At the least, there should have "
					+ "been empty XML here");
		}
		
		Route route = routeHandler_.processXML(new InputSource(
				new ByteArrayInputStream(bao.toByteArray())));
 
		return route;
	}
}
