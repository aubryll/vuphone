/**
 * 
 */
package org.vuphone.vandyupon.android.eventloader;

import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.vuphone.vandyupon.android.Constants;
import org.vuphone.vandyupon.android.eventstore.DBAdapter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
 * Handles parsing the XML that is returned from the server, and sending
 * callbacks whenever an event is ready to be processed. XML Should be in the
 * following format:
 * 
 * <EventRequestResponse> <Event> <Name>Test</Name> <Loc> <Lat>36.1437</Lat>
 * <Lon>-86.8046</Lon> </Loc> <Owner>true</Owner> <Start>1248290654565</Start>
 * <End>1248291254565</End> <EventId>1</EventId>
 * <LastUpdate>1248291254565</LastUpdate> </Event> </EventRequestResponse>
 * 
 * @author Hamilton Turner
 * 
 */
public class EventHandler extends DefaultHandler {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventHandler: ";

	/** Used to keep track of which XML element we are in */
	private boolean inName = false;
	private boolean inLat = false;
	private boolean inLon = false;
	private boolean inOwner = false;
	private boolean inStart = false;
	private boolean inEnd = false;
	private boolean inEventId = false;
	private boolean inLastUpdate = false;

	/** Holds the data for the current event */
	private double latitude_;
	private double longitude_;
	private boolean owner_;
	private String name_;
	private long startTime_;
	private long endTime_;
	private long serverId_;
	private long lastUpdate_;

	/** Passed to the Loader so it can insert events into the DB */
	private DBAdapter database_;

	public EventHandler(DBAdapter openDatabase) {
		database_ = openDatabase;
	}

	/** Called every time a new element is entered */
	@Override
	public void startElement(String uri, String localname, String qName,
			Attributes atts) {

		if (localname.trim().equalsIgnoreCase("Name"))
			inName = true;
		else if (localname.trim().equalsIgnoreCase("Lat"))
			inLat = true;
		else if (localname.trim().equalsIgnoreCase("Lon"))
			inLon = true;
		else if (localname.trim().equalsIgnoreCase("Owner"))
			inOwner = true;
		else if (localname.trim().equalsIgnoreCase("Start"))
			inStart = true;
		else if (localname.trim().equalsIgnoreCase("End"))
			inEnd = true;
		else if (localname.trim().equalsIgnoreCase("EventId"))
			inEventId = true;
		else if (localname.trim().equalsIgnoreCase("LastUpdate"))
			inLastUpdate = true;

	}

	/** Called every time an element is exited */
	@Override
	public void endElement(String uri, String localname, String qName)
			throws SAXException {

		if (localname.trim().equalsIgnoreCase("EventRequestResponse"))
			throw new SAXException("Done processing");
		else if (localname.trim().equalsIgnoreCase("Event")) {
			// Fire callback here to store current event
			EventLoader.handleEvent(database_, name_, latitude_, longitude_,
					owner_, startTime_, endTime_, lastUpdate_, serverId_);
		} else if (localname.trim().equalsIgnoreCase("Name"))
			inName = false;
		else if (localname.trim().equalsIgnoreCase("Lat"))
			inLat = false;
		else if (localname.trim().equalsIgnoreCase("Lon"))
			inLon = false;
		else if (localname.trim().equalsIgnoreCase("Owner"))
			inOwner = false;
		else if (localname.trim().equalsIgnoreCase("Start"))
			inStart = false;
		else if (localname.trim().equalsIgnoreCase("End"))
			inEnd = false;
		else if (localname.trim().equalsIgnoreCase("EventId"))
			inEventId = false;
		else if (localname.trim().equalsIgnoreCase("LastUpdate"))
			inLastUpdate = false;
	}

	/** Called every time an element is about to be exited */
	@Override
	public void characters(char ch[], int start, int length) {
		String str = new String(ch, start, length);

		if (inLat)
			latitude_ = Double.parseDouble(str);
		else if (inLon)
			longitude_ = Double.parseDouble(str);
		else if (inOwner)
			owner_ = Boolean.parseBoolean(str);
		else if (inName)
			name_ = new String(str);
		else if (inStart)
			startTime_ = Long.parseLong(str);
		else if (inEnd)
			endTime_ = Long.parseLong(str);
		else if (inEventId)
			serverId_ = Long.parseLong(str);
		else if (inLastUpdate)
			lastUpdate_ = Long.parseLong(str);

	}

	/**
	 * Given a source of XML, this parses it and calls the static method
	 * EventHandler.handleEvent() when an event is ready
	 */
	public void processXML(InputSource src) {

		// Create the reader
		XMLReader xr = null;
		try {
			xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		} catch (SAXException e) {
			Log.e(tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return;
		} catch (ParserConfigurationException e) {
			Log.e(tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return;
		} catch (FactoryConfigurationError e) {
			Log.e(tag, pre + "Error getting XMLReader instance: "
					+ e.getMessage());
			e.printStackTrace();
			return;
		}

		// Parse the data
		xr.setContentHandler(this);
		try {
			xr.parse(src);
		} catch (IOException e) {
			Log.e(tag, pre + "IOException parsing XML: " + e.getMessage());
			e.printStackTrace();
			return;
		} catch (SAXException e) {
			if (e.getMessage().equalsIgnoreCase("Done processing")) {
				Log.i(tag, pre + "Finished processing XML");
			} else {
				Log.e(tag, pre + "SAXException parsing XML: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
