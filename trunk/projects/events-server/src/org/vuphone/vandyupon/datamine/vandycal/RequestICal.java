/**
 * 
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
<<<<<<< .mine
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
=======
import java.util.Iterator;
import java.util.List;
>>>>>>> .r665

import org.vuphone.vandyupon.notification.eventpost.EventPost;
import org.vuphone.vandyupon.notification.eventpost.EventPostHandler;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
<<<<<<< .mine
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.util.CompatibilityHints;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.vandyupon.notification.eventpost.EventPost;
=======
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Period;
>>>>>>> .r665
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Name;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Url;

/**
 * @author Hamilton Turner
 * 
 */
public class RequestICal {
	public static void main(String[] argv) throws Exception {
		RequestICal.doIt();
	}
	
	public static void loadEventsWithin1Week() {
		CompatibilityHints.setHintEnabled(
				CompatibilityHints.KEY_RELAXED_PARSING, true);
		CompatibilityHints.setHintEnabled(
				CompatibilityHints.KEY_RELAXED_UNFOLDING, true);
		CompatibilityHints.setHintEnabled(
				CompatibilityHints.KEY_RELAXED_VALIDATION, true);

		FileInputStream fin = null;
		try {
			fin = new FileInputStream("vu-calendar.ics");
		} catch (FileNotFoundException e) {
			System.err.println("Could not read sample input file vu-calendar.ics");
			e.printStackTrace();
			return;
		}

		CalendarBuilder builder = new CalendarBuilder();

		Calendar calendar = null;
<<<<<<< .mine

		try {
			calendar = builder.build(fin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

=======
		
		try {
			calendar = builder.build(fin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		
>>>>>>> .r665
		java.util.Calendar today = java.util.Calendar.getInstance();
		today.set(java.util.Calendar.HOUR_OF_DAY, 0);
		today.clear(java.util.Calendar.MINUTE);
		today.clear(java.util.Calendar.SECOND);

<<<<<<< .mine
		ComponentList events = calendar.getComponents(Component.VEVENT);

		Component c = null;
		int missing = 0;
		int unable_to_code = 0;
		int other = 0;
		// public EventPost(Location loc, String name, String user, long start,
		// long end,
		// String callback, String responseType){
		for (int i = 0; i < events.size(); ++i) {
			EventPost ep = new EventPost();

			c = (Component) events.get(i);

			Property location = c.getProperty(Property.LOCATION);

			if (location != null) {

				String startStr = location.getValue();
				if (startStr.indexOf(",") != -1)
					startStr.substring(0, startStr.indexOf(","));
				if (startStr.indexOf("-") != -1)
					startStr.substring(0, startStr.indexOf("-"));

				startStr += ", Vanderbilt University, Nashville, TN";
				System.out.println("" + i + ": " + startStr);

				try {
					ep.setLocation(Geocoder.getLocation(startStr));
				} catch (IOException e) {
					++unable_to_code;
					continue;
				}
			} else {
				++missing;
				continue;
			}

			Summary name = (Summary) c.getProperty(Property.SUMMARY);
			ep.setName(name.getValue());

			ep.setUser("vandy calendar datamine");

			DtEnd end = (DtEnd) c.getProperty(Property.DTEND);
			if (end != null) {
				ep.setEndTime(end.getDate().getTime());
			} else {
				++missing;
				continue;
			}

			DtStart start = (DtStart) c.getProperty(Property.DTSTART);
			if (start != null) {
				ep.setEndTime(start.getDate().getTime());
			} else {
				++missing;
				continue;
			}

			if (false == doEventPost(ep.getName(), ep.getStartTime(), ep
					.getEndTime(), ep.getLocation().getLat(), ep.getLocation()
					.getLon(), "no desc for now"))
				++other;

		}

		System.out.println("Missing " + missing);
		System.out.println("Unable " + unable_to_code);
		System.out.println("Other Fails " + other);
		System.out.println("Total Successful: "
				+ (events.size() - missing - unable_to_code - other));

	}

	private static final String tag = "";
	private static final String pre = "EventPoster: ";
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
=======
		// create a period starting now with a duration of seven days
		Period period = new Period(new DateTime(today.getTime()), new Dur(7, 0, 0, 0));
		Filter filter = new Filter(new PeriodRule(period));
>>>>>>> .r665

<<<<<<< .mine
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
=======
		List<VEvent> eventsToday = (List) filter.filter(calendar.getComponents(Component.VEVENT));
		EventPost post = new EventPost();
		
		for (VEvent event : eventsToday) {
			System.out.println(event.getLocation());
			
>>>>>>> .r665
			Uid uid = event.getUid();
			post.setName(event.getName());
			post.setDescription(event.getDescription().getValue());
			Url url = event.getUrl();
			post.setStartTime(event.getStartDate().getDate().getTime());
			post.setEndTime(event.getEndDate().getDate().getTime());
			Location location = event.getLocation();
			Property categories = event.getProperty("CATEGORIES");
			ParameterList pl = categories.getParameters();
			System.out.println(pl);
		}

		else
			return true;
	}

	public static String getLastError() {
		return lastError_;
	}
}
