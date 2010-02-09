package edu.vanderbilt.vuphone.android.athletics.data;
// this is not an android app - this is just a java console app
// but it shows an example of how to use the data engines as written so far

// there is a part labeled "START" and "END" in the main method,
// you need this in your Activity to retrieve the data
import java.util.Iterator;
import java.util.List;

public class EventExample {

	// this is an example on the many ways you can use the XML data
	// this might be one of those things where you learn how to use what it gives you
	// and not necessarily how it works since it is a little sloppy
	public static void main(String[] args) {
				
		// START: the following code is necessary to have access to object data in your class
		// create a new instance of XMLParser
		XMLParser EventParser = new XMLParser();
		// create a List of DataObjects
		List<EventDataObject> EventData;
		// get the List of objects from XMLParser
		EventData = EventParser.sendDataObject("event"); // warning exists because it uses a raw list
		// END
		
		// use getNumberOfFootballGames example
		System.out.println("Number of football games: ");
		System.out.println(EventDataEngine.getNumberOfGames(EventData, "football"));
		// use getFootballOpponents to print a list
		System.out.println("Opponents:");
		List<String> opponents = EventDataEngine.getOpponents(EventData, "football");
		Iterator<String> it = opponents.iterator();
		while(it.hasNext()) {
			String opponent = it.next();
			System.out.println(opponent);
		}
		// print the opponents in order by date
		System.out.println("Opponents (in order):");
		List<String> opponents2 = EventDataEngine.getOpponentsInOrder(EventData, "football");
		Iterator<String> it2 = opponents2.iterator();
		while(it2.hasNext()) {
			String opponent = it2.next();
			System.out.println(opponent);
		}
		// print future opponents in order by date
		// print the opponents in order by date
		System.out.println("Future Opponents (in order):");
		List<String> opponents3 = EventDataEngine.getFutureOpponentsInOrder(EventData, "football");
		Iterator<String> it3 = opponents3.iterator();
		while(it3.hasNext()) {
			String opponent = it3.next();
			System.out.println(opponent);
		}
	}
	
}
