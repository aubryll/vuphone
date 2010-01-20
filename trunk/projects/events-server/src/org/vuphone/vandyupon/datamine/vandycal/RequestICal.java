/**
 * @author Hamilton Turner
 * @author Aaron Thompson
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Summary;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.eventpost.EventPost;

public class RequestICal {
	// Some book-keeping variables
	private static int missing = 0;
	private static int unable_to_code = 0;
	private static int other = 0;	
	
	public static void main(String[] argv) throws Exception {
		RequestICal.doIt();
	}

	public static void doIt() throws InterruptedException {
		// First, read the file in, removing all instances of "US-Central:" and "LAST-MODIFIED;"
		BufferedReader reader = null;
		URL url = null;
		try {
			url = new URL("http://calendar.vanderbilt.edu/calendar/ics/set/1000/vu-calendar.ics");
			reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

			FileOutputStream fout = new FileOutputStream("vu-calendar.ics");
			OutputStreamWriter out = new OutputStreamWriter(fout, "UTF-8");
			String s;
			while ((s = reader.readLine()) != null) {
				out.write(s + "\n");
			}
			out.close();
		} catch (MalformedURLException e1) {
			System.err.println("ICS calendar URL malformed");
			e1.printStackTrace();
			return;
		} catch (IOException e1) {
			System.err.println("Couldn't load ICS calendar");
			e1.printStackTrace();
		}

		// Write it back out to a temp file
		try {
			reader = new BufferedReader(new FileReader("vu-calendar.ics"));
			
			FileOutputStream fout = new FileOutputStream("vu-calendar-temp.ics");
			OutputStreamWriter out = new OutputStreamWriter(fout, "UTF-8");

			String s;
			while ((s = reader.readLine()) != null) {
				s = s.replaceFirst(";TZID=US-Central", "") + "\n";
				out.write(s);
			}
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Read in the iCal file from the preprocessed temp file
		InputStreamReader fin = null;
		try {
			fin = new InputStreamReader(new FileInputStream("vu-calendar-temp.ics"), "UTF-8");
		} catch (FileNotFoundException e) {
			System.err.println("Could not read preprocessed input file vu-calendar-temp.ics");
			e.printStackTrace();
			return;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Now build the calendar
		CalendarBuilder builder = new CalendarBuilder();
		Calendar calendar = null;
		try {
			calendar = builder.build(fin);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (ParserException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Get all of the events from the calendar
		ComponentList events = calendar.getComponents(Component.VEVENT);

		// Used in the loop to store a single event
		Component c = null;
		for (int i = 0; i < events.size(); i++) {
			EventPost ep = new EventPost();

			c = (Component)events.get(i);

			Property locName = c.getProperty(Property.LOCATION);
			if (locName != null) {
				// Strip backslashes before colons that occur in the ics file
				ep.setLocationName(locName.getValue().replaceAll("\\\\", ""));
			} else {
				ep.setLocationName(null);
			}

			// get geolocation
			org.vuphone.vandyupon.datastructs.Location location = getLocation(c);
			ep.setLocation(location);

			// get name
			Summary name = (Summary)c.getProperty(Property.SUMMARY);
			if (name == null) {
				System.out.println("Skipping one w/o a name");
				continue;
			}
			// Strip backslashes before colons that occur in the ics file
			ep.setName(name.getValue().replaceAll("\\\\", ""));

			// get description
			Description desc = (Description)c.getProperty(Property.DESCRIPTION);
			if (desc != null) {
				// Strip backslashes before colons that occur in the ics file
				ep.setDescription(desc.getValue().replaceAll("\\\\", ""));
			}

			// get user
			ep.setUser("vandy calendar datamine");

			// get start time
			DtStart start = (DtStart)c.getProperty(Property.DTSTART);
			if (start == null) {
				missing++;
				continue;
			} else {
				ep.setStartTime(start.getDate().getTime() / 1000);
			}

			// get end time
			DtEnd end = (DtEnd) c.getProperty(Property.DTEND);
			if (end == null) {
				ep.setEndTime(ep.getStartTime());
			} else {
				ep.setEndTime(end.getDate().getTime() / 1000);
			}

			// get UID
			ep.setSourceUid(c.getProperty(Property.UID).getValue());

			boolean postWorked = doEventPost(ep.getName(), ep.getStartTime(),
					ep.getEndTime(), ep.getLocationName(), ep.getLocation(), ep
							.getDescription(), ep.getSourceUid());

			if (postWorked == false)
				other++;

		}

		System.out.println("Missing " + missing);
		System.out.println("Unable " + unable_to_code);
		System.out.println("Other Fails " + other);
		System.out.println("Total Successful: "
				+ (events.size() - missing - unable_to_code - other));

	}

	private static final String PATH = "/vandyupon/events/";

	private static String lastError_ = "";

	/**
	 * Attempts to contact the server and post an event.
	 * 
	 * @param name
	 *            event name
	 * @param start
	 *            event start calendar
	 * @param end
	 *            event end calendar
	 * @param location
	 *            event location
	 * @param desc
	 *            event description
	 * @param sourceUid
	 * @param context
	 *            application context used to fetch the android ID
	 * @return true if the server was contacted, and returned 200 OK, false
	 *         otherwise
	 */
	public static boolean doEventPost(String name, long start, long end,
			String locName, Location loc, String desc, String sourceUid) {

		lastError_ = "";

		String startTime = Long.toString(start);
		String endTime = Long.toString(end);
		String latitude = null, longitude = null;
		if (loc != null) {
			latitude = Double.toString((double) loc.getLat());
			longitude = Double.toString((double) loc.getLon());
		}
		String androidID = "vandy cal datamine";

		
		try {
			name = URLEncoder.encode(name, "UTF-8");
			startTime = URLEncoder.encode(startTime, "UTF-8");
			endTime = URLEncoder.encode(endTime, "UTF-8");
			if (locName != null)
				locName = URLEncoder.encode(locName, "UTF-8");
			if (loc != null) {
				latitude = URLEncoder.encode(latitude, "UTF-8");
				longitude = URLEncoder.encode(longitude, "UTF-8");
			}
			androidID = URLEncoder.encode(androidID, "UTF-8");

			if (desc != null)
				desc = URLEncoder.encode(desc, "UTF-8");
			else
				desc = "";
		
			// Fix bizarre URL encoding issues
			desc = desc.replaceAll("%26%2339%3B", "%27");
			desc = desc.replaceAll("%E2%80%99", "%27");
			desc = desc.replaceAll("%C2%A0", "%A0");

			byte[] bytes = URLDecoder.decode(desc, "UTF-8").getBytes();
			int i = 0;
			for (byte b: bytes) {
				if ( (int)b < 0 || (int)b > 127) {
					System.err.println("Changing byte " + (int)b + " to 32");
					bytes[i] = 32;
				}
				i++;
			}
			desc = URLEncoder.encode(new String(bytes, "UTF-8"), "UTF-8");

			sourceUid = URLEncoder.encode(sourceUid, "UTF-8");
		} catch (UnsupportedEncodingException use) {
			use.printStackTrace();
		}

		// Prepare to make the post
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost("http://localhost:8082" + PATH);
		c.getParams().setParameter("http.socket.timeout", new Integer(1000));
		c.getParams().setParameter("http.connection.timeout", new Integer(1000));
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Content-Encoding", "UTF-8");

		// Create the parameter string
		StringBuffer params = new StringBuffer();
		params.append("type=eventpost");

		if (locName != null) {
			params.append("&locationname=");
			params.append(locName);
		}
		if (latitude != null) {
			params.append("&locationlat=");
			params.append(latitude);
		}
		if (longitude != null) {
			params.append("&locationlon=");
			params.append(longitude);
		}
		params.append("&eventname=");
		params.append(name);
		params.append("&starttime=");
		params.append(startTime);
		params.append("&endtime=");
		params.append(endTime);
		params.append("&userid=");
		params.append(androidID);
		params.append("&sourceuid=");
		params.append(sourceUid);
		params.append("&resp=xml");
		params.append("&desc=");
		params.append(desc);

		// Add the parameters to the post
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));
		

		// Execute post

		HttpResponse resp = null;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			resp = c.execute(post);
			resp.getEntity().writeTo(bao);

		} catch (ClientProtocolException e) {
			e.printStackTrace();

			lastError_ = "General HTTP Error";
		} catch (IOException e) {
			e.printStackTrace();

			lastError_ = "Unable to access server response. Is Internet available?";
		} catch (Exception e) {
			e.printStackTrace();

			lastError_ = "Unknown problem";
		}

		if (resp == null)
			return false;
		else if (resp.getStatusLine().getStatusCode() != 200) {
			if (lastError_.equals("") == false)
				lastError_ += ". ";
			lastError_ += "Server returned "
					+ resp.getStatusLine().getStatusCode() + " status";
			return false;
		} else if (bao.size() == 0) {
			if (lastError_.equals("") == false)
				lastError_ += ". ";
			lastError_ += "Server did not acknowledge";
			return false;
		}

		return true;
	}

	private static org.vuphone.vandyupon.datastructs.Location getLocation(Component c) {
		Property location = c.getProperty(Property.LOCATION);

		org.vuphone.vandyupon.datastructs.Location geoLocation = null;

		// Check to make sure there was a location
		if (location == null) {
			missing++;
			return null;
		}

		// Remove the extra Room info and what not
		String startStr = location.getValue();
		if (startStr.indexOf(",") != -1)
			startStr = startStr.substring(0, startStr.indexOf(","));
		if (startStr.indexOf("-") != -1)
			startStr = startStr.substring(0, startStr.indexOf("-"));

		// Get a Lat / Lon from the string
		try {
			System.out.println("Coding: " + startStr);
			geoLocation = Geocoder.getLocation(startStr);
			if ((geoLocation.getLat() == 36.1419303)
					&& (geoLocation.getLon() == -86.8044586))
				System.out.println("Got: Vandy");
			else if ((geoLocation.getLat() == 36.1658899)
					&& (geoLocation.getLon() == -86.7844432))
				System.out.println("Got: Nash");
			else
				System.out.println("Got: " + geoLocation.getLat() + ", "
						+ geoLocation.getLon());

		} catch (IOException e) {
			++unable_to_code;
			return null;
		}

		if ((geoLocation.getLat() == 0) || (geoLocation.getLon() == 0)) {
			++unable_to_code;
			return null;
		}

		return geoLocation;
	}

	public static String getLastError() {
		return lastError_;
	}
	
}
