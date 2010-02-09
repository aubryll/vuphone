package edu.vanderbilt.vuphone.android.athletics.data;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// this class is just a set of functions that manipulates the data objects
// parsed by the XMLParser class.  each function requires at least one parameter
// of the EventDataObject to work with the data.
public class EventDataEngine {
	
	// to get a total number of events
	public static int getNumberOfEvents(List<EventDataObject> x) {
		return x.size();
	}
	
	// to get a total number of games for a specific sport
	public static int getNumberOfGames(List<EventDataObject> x, String sport) {
		int counter = 0;
		Iterator<EventDataObject> it = x.iterator();
		while(it.hasNext()) {
			EventDataObject element = it.next();
			if (element.getSport().equalsIgnoreCase(sport)) {
				counter++;
			}
		}
		return counter;
	}
	// get a list of opponents for a sport in no real order
	public static List<String> getOpponents(List<EventDataObject> x, String sport) {
		List<String> opponents = new ArrayList<String>(); // create list of strings
		Iterator<EventDataObject> it = x.iterator();
		while(it.hasNext()) {
			EventDataObject element = it.next();
			if (element.getSport().equalsIgnoreCase(sport)) {
				opponents.add(element.getTitle());
			}
		}
		return opponents;
	}
	// get a list of opponents for a sport in chronological order
	public static List<String> getOpponentsInOrder(List<EventDataObject> x, String sport) {
		List<String> opponents = new ArrayList<String>(); // create list of strings
		Collections.sort(x); // this sorts using the built in comparator (by date and time)
		Iterator<EventDataObject> it = x.iterator();
		while(it.hasNext()) {
			EventDataObject element = it.next();
			if (element.getSport().equalsIgnoreCase(sport)) {
				opponents.add(element.getTitle());
			}
		}
		return opponents;
	}
	// get the list of future opponents in chronological order (includes same-day)
	public static List<String> getFutureOpponentsInOrder(List<EventDataObject> x, String sport) {
		List<String> opponents = new ArrayList<String>(); // create list of strings
		Collections.sort(x); // this sorts using the built in comparator (by date and time)
		Iterator<EventDataObject> it = x.iterator();
		// build a calendar to get the date
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// declare today as todays date in our standardized format
		Integer today = Integer.parseInt(sdf.format(calendar.getTime())); // using integer class for compareTo function
		while(it.hasNext()) {
			EventDataObject element = it.next();
			if (element.getSport().equalsIgnoreCase(sport)) {
				if (element.getDate() >= today) {
					opponents.add(element.getTitle());
				}
			}
		}
		return opponents;
	}
}
