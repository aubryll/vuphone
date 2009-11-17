/**
 * 
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.vuphone.vandyupon.notification.eventpost.EventPost;
import org.vuphone.vandyupon.notification.eventpost.EventPostHandler;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Period;
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

	public static void loadEventsWithin1Week() {
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
		
		
		java.util.Calendar today = java.util.Calendar.getInstance();
		today.set(java.util.Calendar.HOUR_OF_DAY, 0);
		today.clear(java.util.Calendar.MINUTE);
		today.clear(java.util.Calendar.SECOND);

		// create a period starting now with a duration of seven days
		Period period = new Period(new DateTime(today.getTime()), new Dur(7, 0, 0, 0));
		Filter filter = new Filter(new PeriodRule(period));

		List<VEvent> eventsToday = (List) filter.filter(calendar.getComponents(Component.VEVENT));
		EventPost post = new EventPost();
		
		for (VEvent event : eventsToday) {
			System.out.println(event.getLocation());
			
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
	}
}
