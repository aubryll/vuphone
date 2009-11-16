/**
 * 
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;

/**
 * @author Hamilton Turner
 *
 */
public class RequestICal {

	public static void doIt() {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream("vu-calendar.ics");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CalendarBuilder builder = new CalendarBuilder();

		Calendar calendar = null;
		
			try {
				calendar = builder.build(fin);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		java.util.Calendar today = java.util.Calendar.getInstance();
		today.set(java.util.Calendar.HOUR_OF_DAY, 0);
		today.clear(java.util.Calendar.MINUTE);
		today.clear(java.util.Calendar.SECOND);

		// create a period starting now with a duration of one (1) day..
		Period period = new Period(new DateTime(today.getTime()), new Dur(1, 0, 0, 0));
		Filter filter = new Filter(new PeriodRule(period));

		List eventsToday = (List) filter.filter(calendar.getComponents(Component.VEVENT));
		
		System.out.print(eventsToday.toArray());
		for (int i=0; i<eventsToday.size(); i++) {
			
		}
	}
}
