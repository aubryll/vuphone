/**
 * @author Hamilton Turner
 * @author Aaron Thompson
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.BufferedReader;
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

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;

import org.vuphone.vandyupon.notification.eventpost.EventPost;

public class RequestVUICal {

	// To form the URL, concat the prefix + number + suffix
	// Ex: http://calendar.vanderbilt.edu/calendar/ics/set/200/vu-calendar.ics
	private static final String REQUEST_URL_PREFIX = "http://calendar.vanderbilt.edu/calendar/ics/set/";
	private static final int NUMBER_OF_EVENTS_REQUESTED = 1000;
	private static final String REQUEST_URL_SUFFIX = "/vu-calendar.ics?xtags=commencement";

	public static void main(String[] argv) {
		// Get calendar events
		ComponentList events = getCalendarEvents();

		// check for error
		if (events == null)
			return;

		// loop over events
		int size = events.size();
		for (int i = 0; i < size; i++) {
			Component event = (Component) events.get(i);
			EventPost post = EventPostBuilder.build(event);
			if (post != null)
				EventPostPoster.doPost(post);
			System.out.println("Event " + i + " of " + size);
		}
	}

	private static ComponentList getCalendarEvents() {
		// First, read the file in, removing all instances of "US-Central:" and
		// "LAST-MODIFIED;"
		BufferedReader reader = null;
		URL url = null;
		try {
			StringBuffer urlString = new StringBuffer(REQUEST_URL_PREFIX);
			urlString.append(NUMBER_OF_EVENTS_REQUESTED);
			urlString.append(REQUEST_URL_SUFFIX);

			url = new URL(urlString.toString());
			reader = new BufferedReader(new InputStreamReader(url.openStream(),
					"UTF-8"));

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
			return null;
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
			fin = new InputStreamReader(new FileInputStream(
					"vu-calendar-temp.ics"), "UTF-8");
		} catch (FileNotFoundException e) {
			System.err
					.println("Could not read preprocessed input file vu-calendar-temp.ics");
			e.printStackTrace();
			return null;
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
			return null;
		} catch (ParserException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Get all of the events from the calendar
		ComponentList events = calendar.getComponents(Component.VEVENT);
		return events;
	}

}
