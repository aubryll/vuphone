package org.vuphone.wwatch.android.http.parsers;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class WreckHandler extends DefaultHandler {
	private static final String tag = VUphone.tag;
	private static final String pre = "WreckHandler: ";

	private boolean inWrecks = false;
	private boolean inWreck = false;
	private boolean inLat = false;
	private boolean inLon = false;
	private boolean inTime = false;
	private boolean inId = false;

	private int currentLatitude_;
	private int currentLongitude_;
	private long currentTime_;
	private int currentId_;

	private ArrayList<Waypoint> wrecks_;

	@Override
	public void startElement(String uri, final String localname, String qName,
			Attributes atts) {
		if (localname.trim().equalsIgnoreCase("Wrecks")) {
			inWrecks = true;
		} else if (localname.trim().equalsIgnoreCase("Wreck")) {
			inWreck = true;
		} else if (localname.trim().equalsIgnoreCase("Latitude")) {
			inLat = true;
		} else if (localname.trim().equalsIgnoreCase("Longitude")) {
			inLon = true;
		} else if (localname.trim().equalsIgnoreCase("Time")) {
			inTime = true;
		} else if (localname.trim().equalsIgnoreCase("id")) {
			inId = true;
		}
	}

	@Override
	public void endElement(String uri, String localname, String qName)
			throws SAXException {
		if (localname.trim().equalsIgnoreCase("Wrecks")) {
			inWrecks = false;
			throw new SAXException("Done processing");
		} else if (localname.trim().equalsIgnoreCase("Wreck")) {
			inWreck = false;
			final Waypoint wp = new Waypoint(new GeoPoint(currentLatitude_,
					currentLongitude_), currentTime_);
			wp.setAccidentId(currentId_);
			wp.setSeverity((int) (wp.getTime()%5));
			wrecks_.add(wp);
		} else if (localname.trim().equalsIgnoreCase("Latitude")) {
			inLat = false;
		} else if (localname.trim().equalsIgnoreCase("Longitude")) {
			inLon = false;
		} else if (localname.trim().equalsIgnoreCase("Time")) {
			inTime = false;
		} else if (localname.trim().equalsIgnoreCase("id")) {
			inId = false;
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
				currentLatitude_ = (int) (Double.parseDouble(str) * 1E6);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				Log.e(tag, pre + "Unable to parse XML as Double");
				Log.e(tag, pre + new String(ch));
			}
		} else if (inLon == true) {
			try {
				currentLongitude_ = (int) (Double.parseDouble(str) * 1E6);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				Log.e(tag, pre + "Unable to parse XML as Double");
				Log.e(tag, pre + new String(ch));
			}
		} else if (inTime == true) {
			try {
				currentTime_ = Long.parseLong(str);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				Log.e(tag, pre + "Unable to parse XML as Long");
				Log.e(tag, pre + new String(ch));
			}
		} else if (inId == true) {
			try {
				currentId_ = Integer.parseInt(str);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				Log.e(tag, pre + "Unable to parse XML as Integer");
				Log.e(tag, pre + ch.toString());
			}
		}
	}

	public ArrayList<Waypoint> processXML(InputSource src) {
		XMLReader xr = null;
		wrecks_ = new ArrayList<Waypoint>();
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
				Log.i(tag, pre + "Finished processing AccidentXML");
			} else {
				Log.e(tag, pre + "SAXException parsing AccidentXML: "
						+ e.getMessage());
				e.printStackTrace();
			}
		}

		return wrecks_;
	}
}
