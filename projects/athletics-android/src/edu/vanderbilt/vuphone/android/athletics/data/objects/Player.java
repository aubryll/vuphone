package edu.vanderbilt.vuphone.android.athletics.data.objects;

import java.util.Map;

public class Player {

	// attributes of a Player
	private String name;
	private String position;
	private String year; // this is for "Freshman", ..., "Senior"
	private Map<String, String> stats; // this way we can have a variable number of stats
	// build 2 constructors, one for players with stats, one for players without stats
	
	// constructor for players with stats
	public Player(String nameIN, String positionIN, String yearIN,
			Map<String, String> statsIN) {
		setName(nameIN);
		setPosition(positionIN);
		setYear(yearIN);
		setStats(statsIN);
	}
	
	// we will override hashCode so we have a way to identify players with unique IDs if needed
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}
	
	// equals is also overridden so we can see if players are equal if needed
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	// begin auto-generated getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Map<String, String> getStats() {
		return stats;
	}

	public void setStats(Map<String, String> stats) {
		this.stats = stats;
	}
	
	
}
