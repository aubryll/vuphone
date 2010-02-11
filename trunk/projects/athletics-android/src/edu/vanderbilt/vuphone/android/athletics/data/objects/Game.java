package edu.vanderbilt.vuphone.android.athletics.data.objects;

import java.util.Date;

public class Game {
	
	// attributes of a Game
	private String sport;
	private String gender;
	private String type;
	private String opponent;
	private String place;
	private Date time;
	private String description;
	private boolean played;
	private int ourScore;
	private int theirScore;
	
	// build two constructors, one for game that hasn't yet been played and one that has
	
	// constructor for already played game
	public Game(String sportIN, String genderIN, String typeIN, String opponentIN, 
			String placeIN, Date timeIN, String descriptionIN, int ourScoreIN,
			int theirScoreIN) {
		setSport(sportIN);
		setGender(genderIN);
		setType(typeIN);
		setOpponent(opponentIN);
		setPlace(placeIN);
		setTime(timeIN);
		setDescription(descriptionIN);
		setPlayed(true); // constructor is called with scores so this must be true
		setOurScore(ourScoreIN);
		setTheirScore(theirScoreIN);
	}
	
	// constructor for already played game
	public Game(String sportIN, String genderIN, String typeIN, String opponentIN, 
			String placeIN, Date timeIN, String descriptionIN) {
		setSport(sportIN);
		setGender(genderIN);
		setType(typeIN);
		setOpponent(opponentIN);
		setPlace(placeIN);
		setTime(timeIN);
		setDescription(descriptionIN);
		setPlayed(false); // constructor is called without scores so this must be false
	}

	// we will override hashCode so we have a way to identify games with unique IDs if needed
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((opponent == null) ? 0 : opponent.hashCode());
		result = prime * result + ourScore;
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result + (played ? 1231 : 1237);
		result = prime * result + ((sport == null) ? 0 : sport.hashCode());
		result = prime * result + theirScore;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	// equals is also overridden so we can see if games are equal if needed
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (opponent == null) {
			if (other.opponent != null)
				return false;
		} else if (!opponent.equals(other.opponent))
			return false;
		if (ourScore != other.ourScore)
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		if (played != other.played)
			return false;
		if (sport == null) {
			if (other.sport != null)
				return false;
		} else if (!sport.equals(other.sport))
			return false;
		if (theirScore != other.theirScore)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	public int getOurScore() {
		return ourScore;
	}

	public void setOurScore(int ourScore) {
		this.ourScore = ourScore;
	}

	public int getTheirScore() {
		return theirScore;
	}

	public void setTheirScore(int theirScore) {
		this.theirScore = theirScore;
	}
}
