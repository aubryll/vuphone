/**
 * 
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Summary;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.vandyupon.notification.eventpost.EventPost;

/**
 * @author Hamilton Turner
 * 
 */
public class RequestICal {
	// Some book-keeping variables
	private static int missing = 0;
	private static int unable_to_code = 0;
	private static int other = 0;

	public static void main(String[] argv) throws Exception {
		RequestICal.doIt();
	}

	public static void doIt() throws InterruptedException {

		FileInputStream fin = null;
		try {
			fin = new FileInputStream("vu-calendar.ics");
		} catch (FileNotFoundException e) {
			System.err
					.println("Could not read sample input file vu-calendar.ics");
			e.printStackTrace();
			return;
		}

		// Read in the ICAL file
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
		}

		// Get all of the events from the calendar
		ComponentList events = calendar.getComponents(Component.VEVENT);

		// Used in the loop to store a single event
		Component c = null;
		for (int i = 0; i < events.size(); ++i) {
			EventPost ep = new EventPost();

			c = (Component) events.get(i);

			// get location
			org.vuphone.vandyupon.datastructs.Location location = getLocation(c);
			// Wait 1/2 second between requests because the geocoding service doesn't allow more than 2 requests/second
			Thread.sleep(500);
			if (location == null)
				continue;
			ep.setLocation(location);

			// get name
			Summary name = (Summary) c.getProperty(Property.SUMMARY);
			if (name == null)
				continue;
			ep.setName(name.getValue());

			// get user
			ep.setUser("vandy calendar datamine");

			// get end time
			DtEnd end = (DtEnd) c.getProperty(Property.DTEND);
			if (end == null) {
				++missing;
				continue;
			} else
				ep.setEndTime(end.getDate().getTime());

			// get start time
			DtStart start = (DtStart) c.getProperty(Property.DTSTART);
			if (start == null) {
				++missing;
				continue;
			} else
				ep.setEndTime(start.getDate().getTime());

			boolean postWorked = doEventPost(ep.getName(), ep.getStartTime(),
					ep.getEndTime(), ep.getLocation().getLat(), ep
							.getLocation().getLon(), "no desc for now");

			if (postWorked == false)
				++other;
			
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
	 * @param context
	 *            application context used to fetch the android ID
	 * @return true if the server was contacted, and returned 200 OK, false
	 *         otherwise
	 */
	public static boolean doEventPost(String name, long start, long end,
			double lat, double lon, String desc) {

		lastError_ = "";

		String startTime = Long.toString(start);
		String endTime = Long.toString(end);
		String latitude = Double.toString((double) lat);
		String longitude = Double.toString((double) lon);
		String androidID = "vandy cal datamine";

		try {
			name = URLEncoder.encode(name, "UTF-8");
			startTime = URLEncoder.encode(startTime, "UTF-8");
			endTime = URLEncoder.encode(endTime, "UTF-8");
			latitude = URLEncoder.encode(latitude, "UTF-8");
			longitude = URLEncoder.encode(longitude, "UTF-8");
			androidID = URLEncoder.encode(androidID, "UTF-8");
			desc = URLEncoder.encode(desc, "UTF-8");
		} catch (UnsupportedEncodingException use) {
			use.printStackTrace();
		}

		// Prepare to make the post
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost("http://localhost:8080" + PATH);
		c.getParams().setParameter("http.socket.timeout", new Integer(1000));
		c.getParams()
				.setParameter("http.connection.timeout", new Integer(1000));
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Content-Encoding", "UTF-8");

		// Create the parameter string
		StringBuffer params = new StringBuffer();
		params.append("type=eventpost");
		params.append("&locationlat=");
		params.append(latitude);
		params.append("&locationlon=");
		params.append(longitude);
		params.append("&eventname=");
		params.append(name);
		params.append("&starttime=");
		params.append(startTime);
		params.append("&endtime=");
		params.append(endTime);
		params.append("&userid=");
		params.append(androidID);
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
			++missing;
			return null;
		}

		// Remove the extra Room info and what not, and add ", Nashville, TN"
		String startStr = location.getValue();
		if (startStr.indexOf(",") != -1)
			startStr = startStr.substring(0, startStr.indexOf(","));
		if (startStr.indexOf("-") != -1)
			startStr = startStr.substring(0, startStr.indexOf("-"));
//		startStr += ", Nashville, TN";

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
