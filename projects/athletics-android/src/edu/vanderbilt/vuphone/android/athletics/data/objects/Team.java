package edu.vanderbilt.vuphone.android.athletics.data.objects;

import java.util.ArrayList;

public class Team {

	// attributes of a Team
	private String sport;
	private String school;
	private String name;
	private String conference;
	private int wins;
	private int losses;
	private int conferenceWins;
	private int conferenceLosses;
	private ArrayList<Player> players;
	
	// build constructor
	public Team(String sportIN, String schoolIN, String nameIN, String conferenceIN, int winsIN, int lossesIN,
			int conferenceWinsIN, int conferenceLossesIN, ArrayList<Player> playersIN) {
		setSport(sportIN);
		setSchool(schoolIN);
		setName(nameIN);
		setConference(conferenceIN);
		setWins(winsIN);
		setLosses(lossesIN);
		setConferenceWins(conferenceWinsIN);
		setConferenceLosses(conferenceLossesIN);
		setPlayers(playersIN);
	}
	
	// we will override hashCode so we have a way to identify teams with unique IDs if needed
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((conference == null) ? 0 : conference.hashCode());
		result = prime * result + conferenceLosses;
		result = prime * result + conferenceWins;
		result = prime * result + losses;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((school == null) ? 0 : school.hashCode());
		result = prime * result + ((sport == null) ? 0 : sport.hashCode());
		result = prime * result + wins;
		return result;
	}

	// equals is also overridden so we can see if teams are equal if needed
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (conference == null) {
			if (other.conference != null)
				return false;
		} else if (!conference.equals(other.conference))
			return false;
		if (conferenceLosses != other.conferenceLosses)
			return false;
		if (conferenceWins != other.conferenceWins)
			return false;
		if (losses != other.losses)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (school == null) {
			if (other.school != null)
				return false;
		} else if (!school.equals(other.school))
			return false;
		if (sport == null) {
			if (other.sport != null)
				return false;
		} else if (!sport.equals(other.sport))
			return false;
		if (wins != other.wins)
			return false;
		return true;
	}



	// begin auto-generated getters and setters
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConference() {
		return conference;
	}
	public void setConference(String conference) {
		this.conference = conference;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public int getConferenceWins() {
		return conferenceWins;
	}
	public void setConferenceWins(int conferenceWins) {
		this.conferenceWins = conferenceWins;
	}
	public int getConferenceLosses() {
		return conferenceLosses;
	}
	public void setConferenceLosses(int conferenceLosses) {
		this.conferenceLosses = conferenceLosses;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
}
