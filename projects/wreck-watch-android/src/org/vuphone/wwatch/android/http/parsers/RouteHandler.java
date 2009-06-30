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
package org.vuphone.wwatch.android.http.parsers;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.mapview.Route;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class RouteHandler extends DefaultHandler {

	private static final String tag = VUphone.tag;
	private static final String pre = "RouteHandler: ";

	private boolean inPoints = false;
	private boolean inPoint = false;
	private boolean inLat = false;
	private boolean inLon = false;
	private boolean inNotes = false;
	private boolean inRoutes = false;
	private boolean inRoute = false;
	private boolean inTime = false;
	private boolean inId = false;
	private boolean inPerson = false;

	private double currentLatitude_;
	private double currentLongitude_;
	private long currentTime_;
	private int currentId_;
	private String currentPerson_;

	private Route curRoute_;

	@Override
	public void startElement(String uri, final String localname, String qName,
			Attributes atts) {
		if (localname.trim().equalsIgnoreCase("Routes")) {
			inRoutes = true;
		} else if (localname.trim().equalsIgnoreCase("Route")) {
			inRoute = true;
			curRoute_ = new Route();
		} else if (localname.trim().equalsIgnoreCase("Points")) {
			inPoints = true;
		} else if (localname.trim().equalsIgnoreCase("Latitude")) {
			inLat = true;
		} else if (localname.trim().equalsIgnoreCase("Longitude")) {
			inLon = true;
		} else if (localname.trim().equalsIgnoreCase("Point")) {
			inPoint = true;
		} else if (localname.trim().equalsIgnoreCase("Time")) {
			inTime = true;
		} else if (localname.trim().equalsIgnoreCase("id")) {
			inId = true;
		} else if (localname.trim().equalsIgnoreCase("Person")){
			inPerson = true;
		}
	}

	@Override
	public void endElement(String uri, String localname, String qName)
			throws SAXException {
		if (localname.trim().equalsIgnoreCase("Points")) {
			inPoints = false;

		} else if (localname.trim().equalsIgnoreCase("Latitude")) {
			inLat = false;
		} else if (localname.trim().equalsIgnoreCase("Longitude")) {
			inLon = false;
		} else if (localname.trim().equalsIgnoreCase("Point")) {
			GeoPoint point = new GeoPoint((int) (currentLatitude_ * 1E6),
					(int) (currentLongitude_ * 1E6));
			curRoute_.addWaypoint(new Waypoint(point, currentTime_));
			inPoint = false;
		} else if (localname.trim().equalsIgnoreCase("Route")) {
			inRoute = false;
			if (curRoute_.getSize() > 0)
				for (Waypoint w : curRoute_.getRoute()) {
					w.setAccidentId(curRoute_.getAccidentId());
				}
		} else if (localname.trim().equalsIgnoreCase("Routes")) {
			inRoutes = false;
			throw new SAXException("Done processing");
		} else if (localname.trim().equalsIgnoreCase("Time")) {
			inTime = false;
		} else if (localname.trim().equalsIgnoreCase("id")) {
			curRoute_.setAccidentId(currentId_);
			inId = false;
		} else if (localname.trim().equalsIgnoreCase("Person")){
			inPerson = false;
		} 
	}

	@Override
	public void characters(char ch[], int start, int length) {
		if (inLat == true) {
			currentLatitude_ = Double.parseDouble(new String(ch));
		} else if (inLon == true) {
			currentLongitude_ = Double.parseDouble(new String(ch));
		} else if (inTime == true) {
			currentTime_ = Long.parseLong(new String(ch));
		} else if (inId == true) {
			String str = "";
			for (int i = 0; i < length; i++) {
				str += ch[i+start];
			}
			currentId_ = Integer.parseInt(str);
		} else if (inPerson == true){
			currentPerson_ = new String(ch);
		} 
	}

	public Route processXML(InputSource src) {
		XMLReader xr = null;
		curRoute_ = new Route();
		try {
			xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		} catch (SAXException e) {
			Log.e(tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			Log.e(tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		} catch (FactoryConfigurationError e) {
			Log.e(tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}

		xr.setContentHandler(this);
		try {
			xr.parse(src);
		} catch (IOException e) {
			Log.e(tag, pre + "IOException parsing AccidentXML: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			if (e.getMessage().equalsIgnoreCase("Done processing")) {
				Log
						.i(tag, pre
								+ "Finished processing AccidentXML");
			} else {
				Log
						.e(tag, pre
								+ "SAXException parsing AccidentXML: "
								+ e.getMessage()); 
				e.printStackTrace();
			}
		}

		return curRoute_;
	}

}
