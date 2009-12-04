package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

// TODO sort ranges before saving, and check for overlay

public class RestaurantHours {
	
	// array of 7 arrays (one for each day), each of which contains the ranges that the Restaurant is 
	// open for that day
	private ArrayList<ArrayList<Range>> _openRanges;
	
	// default constructor initializes the array and each of its elements
	public RestaurantHours () {
		_openRanges = new ArrayList<ArrayList<Range>>();
		for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
			_openRanges.add(new ArrayList<Range>());
		}
	}
	public RestaurantHours (ArrayList<ArrayList<Range>> hours) {_openRanges = hours;	}
	
	// returns an ArrayList of ranges for the indicated day, 
	// as defined by the Calendar class
	public ArrayList<Range> getRanges(int calendarDay) {
		return _openRanges.get(calendarDay-1);
	}
	
	// sets the ArrayList of ranges for a particular day
	public void setRanges(int calendarDay, ArrayList<Range> ranges) {
		_openRanges.set(calendarDay-1, ranges);
	}
	
	// adds a range to a particular day in sorted order, concatenating with currently
	// existing ranges if necessary
	public void addRange(int calendarDay, Range newRange) {
		ArrayList<Range> ranges = getRanges(calendarDay);
		for (int i = 0; i < ranges.size(); i++) {
			if (newRange.before(ranges.get(i))) {
				ranges.add(i,newRange);
				return;
			}
			else if (ranges.get(i).overlap(newRange)) {
				ranges.get(i).concat(newRange);
				return;
			}
		}
		ranges.add(newRange);
	}
	
	// returns the number of ranges in the day
	public int getRangeCount(int calendarDay) {
		return getRanges(calendarDay).size();
	}
	
	// returns ArrayList of today's ranges
	public ArrayList<Range> getTodayRanges() {
		Calendar now = new GregorianCalendar();
		return getRanges(now.DAY_OF_WEEK);
	}
	
	// returns true if restaurant is open now
	public boolean isOpen() {
		ArrayList<Range> todayRanges = getTodayRanges();
		Time now = new Time();
		for (int i = 0; i<todayRanges.size(); i++)
			if (todayRanges.get(i).inRange(now))
				return true;
		return false;
	}
	
	// returns minutes to the next opening time for the restaurant, and 0 if already open, -1 if closed for the day
	public int minutesToOpen() {
		ArrayList<Range> todayRanges = getTodayRanges();
		Time now = new Time();
		for (int i = 0; i<todayRanges.size(); i++) {
			if (now.before(todayRanges.get(i).getEnd())) {
				 int minutes = todayRanges.get(i).getStart().totalMinutes()-now.totalMinutes();
				 return minutes>0?minutes:0;
			}
		}
		return -1;
	}
	
	// returns minutes to the next closing time for the restaurant, -1 if closed for the day
	public int minutesToClose() {
		ArrayList<Range> todayRanges = getTodayRanges();
		Time now = new Time();
		for (int i = 0; i<todayRanges.size(); i++) {
			if (now.before(todayRanges.get(i).getEnd())) {
				return todayRanges.get(i).getEnd().totalMinutes()-now.totalMinutes();
			}
		}
		return -1;
	}
	
	
	
	// methods to support old interface
	public void setSundayRanges(ArrayList<Range> newSundayRange) {setRanges(Calendar.SUNDAY, newSundayRange);}
	public void setMondayRanges(ArrayList<Range> newMondayRange) {setRanges(Calendar.MONDAY, newMondayRange);}
	public void setTuesdayRanges(ArrayList<Range> newTuesdayRange) {setRanges(Calendar.TUESDAY, newTuesdayRange);}
	public void setWednesdayRanges(ArrayList<Range> newWednesdayRange) {setRanges(Calendar.WEDNESDAY, newWednesdayRange);}
	public void setThursdayRanges(ArrayList<Range> newThursdayRange) {setRanges(Calendar.THURSDAY, newThursdayRange);}
	public void setFridayRanges(ArrayList<Range> newFridayRange) {setRanges(Calendar.FRIDAY, newFridayRange);}
	public void setSaturdayRanges(ArrayList<Range> newSaturdayRange) {setRanges(Calendar.SATURDAY, newSaturdayRange);}
	
	public int getSundayRangeCount() {return getRangeCount(Calendar.SUNDAY);}
	public int getMondayRangeCount() {return getRangeCount(Calendar.MONDAY);}
	public int getTuesdayRangeCount() {return getRangeCount(Calendar.TUESDAY);}
	public int getWednesdayRangeCount() {return getRangeCount(Calendar.WEDNESDAY);}
	public int getThursdayRangeCount() {return getRangeCount(Calendar.THURSDAY);}
	public int getFridayRangeCount() {return getRangeCount(Calendar.FRIDAY);}
	public int getSaturdayRangeCount() {return getRangeCount(Calendar.SATURDAY);}
	
	public ArrayList<Range> getSundayRanges() {return getRanges(Calendar.SUNDAY);}
	public ArrayList<Range> getMondayRanges() {return getRanges(Calendar.MONDAY);}
	public ArrayList<Range> getTuesdayRanges() {return getRanges(Calendar.TUESDAY);}
	public ArrayList<Range> getWednesdayRanges() {return getRanges(Calendar.WEDNESDAY);}
	public ArrayList<Range> getThursdayRanges() {return getRanges(Calendar.THURSDAY);}
	public ArrayList<Range> getFridayRanges() {return getRanges(Calendar.FRIDAY);}
	public ArrayList<Range> getSaturdayRanges() {return getRanges(Calendar.SATURDAY);}
	
	
}
