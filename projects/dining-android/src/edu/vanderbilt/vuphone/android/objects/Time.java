package edu.vanderbilt.vuphone.android.objects;

import java.util.Calendar;
import java.util.GregorianCalendar;


// So I (Austin) happened to have a more full featured Time class implementation 
// with a virtually identical interface on hand from a CS 101 assignment. It has 
// some good helper methods, as well as functional toString methods with option for
// 12 and 24 hour time. 


public class Time {

	private static final Exception NumberFormatException = null;
	// hour in 24 based time (0-23)
	private int hour;
	// minute in range 0-59
	private int minute;
	
	// string output setting
	private boolean display24;
	
	
	
	
	// constructors based on setTime methods defined below, and sets display
	// mode to 12 hour format, default constructor sets to the current time
	public Time() 				{Calendar now = new GregorianCalendar();
								 setTime(now);		display24=false;}
	public Time(int hr,int min) {setTime(hr,min);	display24=false;}
	public Time(int hr) 		{setTime(hr);		display24=false;}
	public Time(String time)	{setTime(time);		display24=false;}
	public Time(Time time)		{setTime(time);		display24=false;}
	public Time(Calendar c) 	{setTime(c);		display24=false;}
	
	
	
	
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
	
	// sets display mode to 24 hour time
	public void setMilitaryTime() 	{display24=true;}
	
	// sets display mode to 12 hour time
	public void setCivilianTime() 	{display24=false;}
	
	// changes time to having elapsed 1 hour
	public void incrementHour() {
		hour = (hour+1)%24;
	}
	// changes time to having elapsed 1 minute
	public void incrementMinute() {
		minute = (minute+1)%60;
		if (minute == 0) 
			incrementHour();
	}
	
	
	// returns true if the time contained in 'this' comes before (in the same day) 
	// the time contained in 'time'
	public boolean before(Time time) {
		return totalMinutes() < time.totalMinutes();
	}
	
	// returns the number of minutes elapsed in the day
	public int totalMinutes() {
		return getHour()*60 + getMinute();
	}
	
	// returns a string representation of the contained time in set or default format (12 or 24 hour time);
	public String toString() {		
		if (display24) {
			return ((getHour()<10 ? "0":"") + getHour() + ":" + (getMinute()==0?"00":(getMinute()<10?"0" + getMinute():getMinute())));
		} else {
			int hr = (getHour() + 12 - 1)%12 + 1; 	// turns hour to 12 hour format (the + 12 simply 
													// makes the modulus operator behave as it is 
													// mathematically defined)
			return (hr + ":" + (getMinute()==0?"00":(getMinute()<10?"0" + getMinute():getMinute())) + " " + (getHour()>=11 ? "pm" : "am"));
		}
	}
	
}
