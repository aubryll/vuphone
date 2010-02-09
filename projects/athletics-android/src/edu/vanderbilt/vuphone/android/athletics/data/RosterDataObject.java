package edu.vanderbilt.vuphone.android.athletics.data;
import java.util.List;


public class RosterDataObject {

	private String sport; // football, basketball, etc
	private List<List<String>> playerdata; // a list of lists of players
	// this data type can be conceptually difficult to imagine.  imagine:
	// String player0name = player.get(0).get(0);
	// String player0stat1 = player.get(0).get(1);
	// String player0stat2 = player.get(0).get(2);
	// this lets us store stats as a virtual multidimensional array (since java doesn't support them)
	// the biggest downside to this is that all programmers need to understand the layout of the array
	// since they are unlabeled.
	// to create this multidimensional array we will first create our player then create stats
	// player.get(0).add("John");
	// player.get(0).add("Point Guard");
	// player.get(1).add("Bill");
	// player.get(1).add("Center");
	
	public RosterDataObject(String sport2, List<List<String>> playerdata2) { // constructor
		setSport(sport2);
		setPlayerData(playerdata2);
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public List<List<String>> getPlayerData() {
		return playerdata;
	}

	public void setPlayerData(List<List<String>> playerdata) {
		this.playerdata = playerdata;
	}
}
