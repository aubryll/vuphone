package edu.vanderbilt.vuphone.android.athletics.data.parsers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import edu.vanderbilt.vuphone.android.athletics.data.objects.Game;
import edu.vanderbilt.vuphone.android.athletics.data.objects.Player;
import edu.vanderbilt.vuphone.android.athletics.data.objects.Team;


public class demo {

	public static void main(String[] args) {
		SportXmlParser football = new SportXmlParser("football");
		System.out.println("Number of games: " + football.games.size());
		System.out.println("Games: ");
		Iterator<Game> gameIterator = football.games.iterator();
		while(gameIterator.hasNext()) {
			Game thisGame = gameIterator.next();
			System.out.println(thisGame.getOpponent());
		}
		System.out.println("");
		System.out.println("Number of teams: " + football.teams.size());
		System.out.println("Teams: ");
		// iterate all of the teams
		Iterator<Team> teamIterator = football.teams.iterator();
		ArrayList<Integer> teamHash = new ArrayList<Integer>(); // searchability
		while(teamIterator.hasNext()) {
			Team thisTeam = teamIterator.next();
			System.out.println(thisTeam.getSchool() + " " + thisTeam.getName());
			teamHash.add(thisTeam.hashCode()); // searchability
		}
		// say I want team 0 on the list
		Team teamIWant = null;
		int listItemIWanted = 0;
		Iterator<Team> teamSearchIterator = football.teams.iterator();
		while(teamSearchIterator.hasNext()) {
			Team thisTeam = teamSearchIterator.next();
			if(thisTeam.hashCode() == teamHash.get(listItemIWanted)) {
				teamIWant = thisTeam;
			}
		}
		System.out.println("");
		Iterator<Player> playerIterator = teamIWant.getPlayers().iterator();
		Player myPlayer = playerIterator.next();
		Map<String, String> myPlayerStats = myPlayer.getStats();
		System.out.println("Player and Stats: ");
		System.out.println(myPlayer.getName());
		System.out.println("Completions:\t" + myPlayerStats.get("completions"));
	}

}
