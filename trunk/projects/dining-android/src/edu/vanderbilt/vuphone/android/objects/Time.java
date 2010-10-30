package edu.vanderbilt.vuphone.android.objects;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.vanderbilt.vuphone.android.dining.Main;


/**
 * Stores a time in hours and minutes
 * @author austin
 *
 */
public class Time {
	
	public static Time beginning = new Time(0,0);
	public static Time end = new Time(23,59);

	// hour in 24 based time (0-23)
	private int hour;
	// minute in range 0-59
	private int minute;
	
	
	
	
	// constructors based on setTime methods defined below, and sets display
	// mode to 12 hour format, default constructor sets to the current time
	public Time() 				{Calendar now = new GregorianCalendar();
								 setTime(now);}
	public Time(int hr,int min) {setTime(hr,min);}
	public Time(int hr) 		{setTime(hr);}
	public Time(String time)	{setTime(time);}
	public Time(Time time)		{setTime(time);}
	public Time(Calendar c) 	{setTime(c);}
	
	
	
	
	// sets hour to hr and minute to min checking for correctness
	public void setTime(int hr, int min) {
		setHour(hr);
		setMinute(min);
	}
	// sets hour to hr and minute to 0 checking for correctness
	public void setTime(int hr) {
		setTime(hr,0);
	}
	// takes strings of format HH:MM, H:MM, HH, H. Sets hour and 
	// minute accordingly checking for correctness
	public void setTime(String time) {
		int colonI = time.indexOf(':');
		try {
			if (colonI==-1) {
				// parseInt throws the necessary exception if time has length 0, 
				// and if time has length > 2, setHour prints the necessary error
				setTime(Integer.parseInt(time),0);
			} else { 
				String MM = time.substring(colonI+1,time.length());
				if (MM.length()!=2)
					throw new NumberFormatException();
				
				setTime(Integer.parseInt(time.substring(0, colonI)),
						Integer.parseInt(MM));
			}
		} 
		catch (NumberFormatException e) {
			throw new NumberFormatException("ERR: Invalid time format");
		}
	}
	// sets the time contained in the referenced Time object
	public void setTime(Time time) {
		setTime(time.getHour(),time.getMinute());
	}
	public void setTime(Calendar c) {
		setTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
	}
	
	
	
	// sets hour to hr, displaying an error and halting if outside the range 0-23
	public void setHour(int hr) {
		if (hr<0 || hr>23) {
			throw new RuntimeException("Invalid hour set; must be in range 0-23");
		}
		hour = hr;
	}
	// sets minute to min, displaying an error and halting if outside the range 0-59
	public void setMinute(int min) {
		if (min<0 || min>59) {
			throw new RuntimeException("Invalid minute set; must be in range (0-59)");
		}
		
		minute = min;
	}
	
	
	// returns hour in 24 hour time
	public int getHour() 			{return hour;}
	// returns minute
	public int getMinute() 			{return minute;}
	
	
	// returns true if the time contained in 'this' comes before (in the same day) 
	// the time contained in 'time'
	public boolean before(Time t) {
		return totalMinutes() < t.totalMinutes();
	}
	
	public boolean equals(Time t) {
		return (getHour() == t.getHour() && getMinute() == t.getMinute());
	}
	
	public boolean after(Time t) {
		return totalMinutes() > t.totalMinutes();
	}
	
	public boolean before(Range r) {
		return before(r.getStart());
	}
	
	public boolean during(Range r) {
		return r.inRange(this);
	}
	
	public boolean after(Range r) {
		return after(r.getEnd()) && !r.overnight();
	}
	
	// returns the number of minutes elapsed in the day
	public int totalMinutes() {
		return getHour()*60 + getMinute();
	}
	
	// returns a string representation of the contained time in set or default format (12 or 24 hour time);
	public String toString() {		
		return toString(Main.display24);
	}
		
	public String toString(boolean display24) {
		if (display24) {
			return ((getHour()<10 ? "0":"") + getHour() + ":" + (getMinute()==0?"00":(getMinute()<10?"0" + getMinute():getMinute())));
		} else {
			int hr = (getHour() + 12 - 1)%12 + 1; 	// turns hour to 12 hour format (the + 12 simply 
													// makes the modulus operator behave as it is 
													// mathematically defined)
			return (hr + ":" + (getMinute()==0?"00":(getMinute()<10?"0" + getMinute():getMinute())) + " " + (getHour()>=12 ? "pm" : "am"));
		}
	}
	
}
