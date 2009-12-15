package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.util.Log;

// TODO sort ranges before saving, and check for overlay

public class RestaurantHours {
	
	// array of 7 arrays (one for each day), each of which contains the ranges that the Restaurant is 
	// open for that day
	private ArrayList<ArrayList<Range>> _openRanges;
	
	// default constructor initializes the array and each of its elements
	public RestaurantHours () {
		_openRanges = new ArrayList<ArrayList<Range>>();
		_openRanges.ensureCapacity(7);
		for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
			_openRanges.add(new ArrayList<Range>());
		}
	}
	public RestaurantHours (ArrayList<ArrayList<Range>> hours) {
		_openRanges = hours;
		for (int i = Calendar.SUNDAY; i<=Calendar.SATURDAY; i++) {
			if (i-1>_openRanges.size())
				// if hours didnt have the correct number of days
				_openRanges.add(new ArrayList<Range>());
			sortAndMerge(i);
		}
	}
	
	// returns an ArrayList of ranges for the indicated day, 
	// as defined by the Calendar class
	public ArrayList<Range> getRanges(int calendarDay) {
		return _openRanges.get(calendarDay-1);
	}
	
	// sets the ArrayList of ranges for a particular day
	public void setRanges(int calendarDay, ArrayList<Range> ranges) {
		_openRanges.set(calendarDay-1, ranges);
		sortAndMerge(calendarDay);
	}
	
	// adds a range to a particular day in sorted order, concatenating with currently
	// existing ranges if necessary
	public void addRange(int calendarDay, Range newRange) {
		ArrayList<Range> ranges = getRanges(calendarDay);
		for (int i = ranges.size(); i>0; i--) {
			if (newRange.after(ranges.get(i-1))) {
				ranges.add(i,newRange);
				return;
			}
			if (ranges.get(i-1).overlap(newRange)) {
				ranges.get(i-1).concat(newRange);
				mergeAll(ranges);
				return;
			}				
		}
		ranges.add(0,newRange);
	}
	
	// returns the number of ranges in the day
	public int getRangeCount(int calendarDay) {
		return getRanges(calendarDay).size();
	}
	
	// returns ArrayList of today's ranges
	public ArrayList<Range> getTodayRanges() {
		Calendar now = new GregorianCalendar();
		return getRanges(now.get(Calendar.DAY_OF_WEEK));
	}
	
	// returns the next range or the current range (from now), null Range if closed for the day
	public Range getCurrentRange() {
		ArrayList<Range> todayRanges = getTodayRanges();
		Time now = new Time();
		for (int i = 0; i<todayRanges.size(); i++) 
			if (now.before(todayRanges.get(i).getEnd()))
				return todayRanges.get(i);
		return new Range();
	}
	
	// returns true if restaurant is open now
	public boolean isOpen() {
		Range current = getCurrentRange();
		if (current.notNull())
			return current.during(new Time());
		return false;
	}
	
	// returns minutes to the next opening time for the restaurant, 0 if already open, -1 if closed for the day
	public int minutesToOpen() {
		Time now = new Time();
		Range current = getCurrentRange();
		if (current.notNull()) {
			int minutes = current.getStart().totalMinutes()-now.totalMinutes();
			return minutes>0?minutes:0;
		}
		return -1;
	}
	
	// returns minutes to the next closing time for the restaurant, -1 if closed for the day
	public int minutesToClose() {
		Time now = new Time();
		Range current = getCurrentRange();
		if (current.notNull())
			return current.getEnd().totalMinutes()-now.totalMinutes();
		return -1;
	}	
	
	// returns the next open time, the current time if already open, and midnight if closed for the day
	public Time getNextOpenTime() {
		Range current = getCurrentRange();
		Time now = new Time();
		if (current.notNull()) {
			if (current.getStart().before(now))
				return now;
			else return current.getStart();		
		}
		return new Time(0,0);
	}
	
	// returns the next closing time, midnight if closed for the day
	public Time getNextCloseTime() {
		Range current = getCurrentRange();
		if (current.notNull()) 
			return current.getEnd();
		return new Time(0,0);
	}
	
	// puts the ranges for a particular day in the correct order, and merges overlapping ranges
	public void sortAndMerge(int calendarDay) {
		ArrayList<Range> ranges = getRanges(calendarDay);
		quickSort(ranges, 0, ranges.size()-1);
		mergeAll(ranges);
	}
	
	// implementation of quicksort
	private void quickSort(ArrayList<Range> toSort, int start, int end) {
		if (end<=start)
			return;
		int first=start;
		int pivotIndex = end--;
		for (;start<end;) {
			if (!toSort.get(start).after(toSort.get(pivotIndex)))
				// the comparison step, sorts in !after order, lol
				start++;
			else
				swap(toSort, start, end--);
		}
		if (toSort.get(start).before(toSort.get(pivotIndex))) 
				swap(toSort, ++start, pivotIndex);
		else swap(toSort, start, pivotIndex);
		
		quickSort(toSort, first, start-1);
		quickSort(toSort, start+1, pivotIndex);
		
	}
	
	// swaps elements in an ArrayList<Range> array
	private void swap(ArrayList<Range> toSwap, int first, int second) {
		Range temp = toSwap.get(second);
		toSwap.set(second, toSwap.get(first));
		toSwap.set(first, temp);
	}
	
	// merges all overlapping Ranges
	private void mergeAll(ArrayList<Range> ranges) {
		for (int i = ranges.size()-2;i>=0;i--) {
			if (ranges.get(i).overlap(ranges.get(i+1))) {
				ranges.get(i).concat(ranges.remove(i+1));
			}
		}
	}
	
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
			ArrayList<Range> ranges = getRanges(i);
			switch (i) {
			case Calendar.SUNDAY:
				out.append("S\t");
				break;
			case Calendar.MONDAY:
				out.append("M\t");
				break;
			case Calendar.TUESDAY:
				out.append("T\t");
				break;
			case Calendar.WEDNESDAY:
				out.append("W\t");
				break;
			case Calendar.THURSDAY:
				out.append("T\t");
				break;
			case Calendar.FRIDAY:
				out.append("F\t");
				break;
			case Calendar.SATURDAY:
				out.append("S\t");
				break;
			}
			for (int j = 0; j < ranges.size(); j++) {
				out.append(ranges.get(j).toString()).append(" ");
			}
			out.append("\n");
		}
		return out.toString();
	}
	
	// flattens a days ranges into at most 64 bits (21*3 + 1)
	public long flatten(int calendarDay) {
		ArrayList<Range> day = getRanges(calendarDay);
		long out;
		switch (day.size()) {
		case 0: case 1: case 2:
			out = 0; // rightmost mode bit set to 0 
						//	(can be encoded and decoded with only bit shifts, but is less data efficient)
			for (int i = 0; i < day.size(); i++) {
				out += (long)day.get(i).getStart().getHour() << 22*i + 1;
				out += (long)day.get(i).getStart().getMinute() << 22*i + 6;
				out += (long)day.get(i).getEnd().getHour() << 22*i + 12;
				out += (long)day.get(i).getStart().getMinute() << 22*i + 17;
			}
			return out;
		case 3:
			out = 1; // rightmost mode bit set to 1
						//	(uses multiplication and division to encode and decode, 
						//  but is more data efficient, and can fit 3 ranges in 63 bits, plus the mode bit)
			for (int i = 0; i < day.size(); i++) {
				out += (long)day.get(i).getStart().getHour() +
					24*((long)day.get(i).getStart().getMinute() +
					60*((long)day.get(i).getEnd().getHour() +
					24*((long)day.get(i).getEnd().getMinute()))) << 21*i + 1;
			}
			return out;
		default:
			throw new RuntimeException("too many ranges to flatten (must be <=3)");
		}
	}
	
	public static ArrayList<Range> inflate(long flattenedDay) {
		ArrayList<Range> out = new ArrayList<Range>();
		boolean mode = (flattenedDay & 1) == 1;
		flattenedDay = flattenedDay >> 1;
		if (mode) {
			out.ensureCapacity(3);
			for (int i = 0; i < 3; i++) {
				Range currentR = new Range();
				long current = flattenedDay & 0x1fffff;
				int hr, min;
				hr = (int)current % 24;
				current /= 24;
				min = (int)current % 60;
				current /= 60;
				currentR.setStart(new Time(hr, min));
				hr = (int) current % 24;
				current /= 24;
				min = (int)current % 60;
				Time end = new Time(hr, min);
				if (!currentR.setEnd(end))
					throw new RuntimeException("invalid hours data in database, " + end.toString() + " before " + currentR.getStart().toString());
				out.add(currentR);
				flattenedDay = flattenedDay >> 21; 
			}
		} else {
			while (flattenedDay != 0) {
				Range currentR = new Range();
				int hr, min;
				hr = ((int)flattenedDay & 0x1f);
				flattenedDay = flattenedDay >> 5;
				min = ((int)flattenedDay & 0x3f);
				flattenedDay = flattenedDay >> 6;
				currentR.setStart(new Time(hr, min));
				hr = ((int)flattenedDay & 0x1f);
				flattenedDay = flattenedDay >> 5;
				min = ((int)flattenedDay & 0x3f);
				flattenedDay = flattenedDay >> 6;
				Time end = new Time(hr, min);
				if (!currentR.setEnd(end))
					throw new RuntimeException("invalid hours data in database, " + end.toString() + " before " + currentR.getStart().toString());
				out.add(currentR);
			}
		}
		return out;
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
