package edu.vanderbilt.vuphone.android.athletics.data;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class RosterDataEngine {

	public static List<String> getPlayersAndPositions(List<RosterDataObject> x, String sport) {
		List<String> data = new ArrayList<String>(); // create list of strings
		
		Iterator<RosterDataObject> it = x.iterator();
		while(it.hasNext()) {
			RosterDataObject element = it.next();
			if (element.getSport().equalsIgnoreCase(sport)) {
				// create our 2 dimensional array
				List<List<String>> players;
				players = element.getPlayerData();
				// now we output the first item of each element in the first array by iterating it
				Iterator<List<String>> it2 = players.iterator();
				while(it2.hasNext()) {
					List<String> individualplayer = it2.next();
					data.add(individualplayer.get(0) + "\t" + individualplayer.get(1));
				}
			}
		}
		return data;
	}
}
