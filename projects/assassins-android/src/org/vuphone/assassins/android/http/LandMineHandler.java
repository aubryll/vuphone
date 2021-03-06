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

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.vuphone.assassins.android.VUphone;
import org.vuphone.assassins.android.landmine.LandMine;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
 * This class handles parsing the xml returned from the server when
 * a list of currently active land mines is requested.
 * 
 * @author Scott Campbell
 */
public class LandMineHandler extends DefaultHandler {
	
	private String pre = "LandMineHandler: ";
	
	private ArrayList<LandMine> mines_;
	
	private boolean inLandMines = false;
	private boolean inLandMine = false;
	private boolean inLat = false;
	private boolean inLon = false;
	private boolean inRadius = false;

	private double currentLatitude_;
	private double currentLongitude_;
	private float currentRadius_;

	@Override
	public void startElement(String uri, final String localname, String qName,
			Attributes atts) {
		if (localname.trim().equalsIgnoreCase("LandMines")) {
			inLandMines = true;
		} else if (localname.trim().equalsIgnoreCase("LandMine")) {
			inLandMine = true;
		} else if (localname.trim().equalsIgnoreCase("Lat")) {
			inLat = true;
		} else if (localname.trim().equalsIgnoreCase("Lon")) {
			inLon = true;
		} else if (localname.trim().equalsIgnoreCase("Radius")) {
			inRadius = true;
		}
	}

	@Override
	public void endElement(String uri, String localname, String qName)
			throws SAXException {
		if (localname.trim().equalsIgnoreCase("LandMines")) {
			inLandMines = false;
			throw new SAXException("Done processing");
		} else if (localname.trim().equalsIgnoreCase("LandMine")) {
			inLandMine = false;
			final LandMine lm = new LandMine(currentLatitude_, 
					currentLongitude_, currentRadius_);
			mines_.add(lm);
		} else if (localname.trim().equalsIgnoreCase("Lat")) {
			inLat = false;
		} else if (localname.trim().equalsIgnoreCase("Lon")) {
			inLon = false;
		} else if (localname.trim().equalsIgnoreCase("Radius")) {
			inRadius = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		// We use a StringBuilder here to ensure we only get the first length
		// characters of ch, because sometimes ch contains extra garbage.
		
		// Note: StringBuilders are not synchronized.  If this needs to be
		// synchronized, we should use the slower StringBuffer instead.
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(ch[i + start]);
		}
		String str = sb.toString();
		if (inLat == true) {
			try {
				currentLatitude_ = Double.parseDouble(str);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				Log.e(VUphone.tag, pre + "Unable to parse XML as Double");
				Log.e(VUphone.tag, pre + str);
			}
		} else if (inLon == true) {
			try {
				currentLongitude_ = Double.parseDouble(str);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				Log.e(VUphone.tag, pre + "Unable to parse XML as Double");
				Log.e(VUphone.tag, pre + str);
			}
		} else if (inRadius == true) {
			try {
				currentRadius_ = Float.parseFloat(str);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				Log.e(VUphone.tag, pre + "Unable to parse XML as Float");
				Log.e(VUphone.tag, pre + str);
			}
		}
	}

	public ArrayList<LandMine> processXML(InputSource src) {
		XMLReader xr = null;
		mines_ = new ArrayList<LandMine>();
		try {
			xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		} catch (SAXException e) {
			Log.e(VUphone.tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			Log.e(VUphone.tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		} catch (FactoryConfigurationError e) {
			Log.e(VUphone.tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		}

		xr.setContentHandler(this);
		try {
			xr.parse(src);
		} catch (IOException e) {
			Log.e(VUphone.tag, pre + "IOException parsing LandMineXML: "
					+ e.getMessage());
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			if (e.getMessage().equalsIgnoreCase("Done processing")) {
				Log.i(VUphone.tag, pre + "Finished processing LandMineXML");
			} else {
				Log.e(VUphone.tag, pre + "SAXException parsing LandMineXML: "
						+ e.getMessage());
				e.printStackTrace();
			}
		}

		return mines_;
	}

}
