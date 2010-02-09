package edu.vanderbilt.vuphone.android.athletics.data;

// this is our EventDataObject that holds each block parsed by the XML file.
// by using an ArrayList of DataObjects we can easily access the data from the
// XML file without using SQL (mostly because I don't know how).
public class EventDataObject implements Comparable<EventDataObject> {
	private String type; // scrimmage, season, playoff, etc
	private String title; // "@Florida" or "Auburn"
	private int date; // date for start of event YYYYMMDD
	private int time; // time for start of event HHMM in 24 hour time
	private String description; // description if needed
	private String sport; // football, basketball, baseball, etc
	private int ourScore; // our score
	private int theirScore; // opponent score
	
	public EventDataObject(String type2, String title2, int date2, int time2,
			String description2, String sport2, int ourscore2,
			int theirscore2) { // this is the constructor for the parser
	setType(type2);
	setTitle(title2);
	setDate(date2);
	setTime(time2);
	setDescription(description2);
	setSport(sport2);
	setOurScore(ourscore2);
	setTheirScore(theirscore2);
		
	}
	// TODO: implement sorting by date using custom hashCode() and compareTo(DataObject n)
	@Override // this allows us to sort EventDataObjects by date and time
	public int compareTo(EventDataObject arg0) {
		if (this == arg0) {
			return 0;
		} else if (this.date < arg0.date) {
			return -1;
		} else if (this.date > arg0.date) {
			return 1;
		} else if (this.date == arg0.date) {
			if (this.time < arg0.time) {
				return -1;
			} else if (this.time > arg0.time) {
				return 1;
			} else if (this.time == arg0.time) {
				return 0;
			}
		}
		return 0;
	}
	
	
	// Begin automatically generated getters and setters
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
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
