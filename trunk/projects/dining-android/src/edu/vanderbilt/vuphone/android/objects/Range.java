package edu.vanderbilt.vuphone.android.objects;

import android.util.Log;

public class Range {
	
	// start is allowed to be after end, it indicates a range that is intraday
	private Time _start;
	private Time _end;

	// Needed for XStream!
	public Range() {};
	public Range(Time start, Time end) {setTimes(start, end);}

	public Time getStart() {return _start;}
	public Time getEnd() {return _end;}
	

	// sets start time for range, 
	public void setStart(Time start) {
		_start = start;
	}
	
	// sets end time for range, 
	public void setEnd(Time end) {
		_end = end;
	}
	
	// sets start and end times, and throws an exception if end is before start
	public void setTimes(Time start, Time end) {
		_start = start;
		_end = end;
	}

	// returns whether t is in the current range or not. 
	public boolean inRange(Time t) {
		try {
			int startM = _start.totalMinutes();
			int endM = _end.totalMinutes();
			int tM = t.totalMinutes();
			return (startM <= tM && (tM <= endM || endM <= startM));
		} catch (NullPointerException e) {
			throw new RuntimeException("inRange(Time): Range not properly initialized");
		}
	}
	
	// returns a negative number if start_ is after t
	public int minutesUntilStart(Time t) {
		try {
			return _start.totalMinutes() - t.totalMinutes();
		} catch (NullPointerException e) {
			throw new RuntimeException("minutesUntilStart(Time): Range not properly initialized");
		}
	}
	
	// always returns a positive number, even when t is after the range (will return time until end the next day)
	public int minutesUntilEnd(Time t) {
		try {
			return (_end.totalMinutes() - t.totalMinutes() + 1440 - 1) % 1440 + 1;
		} catch (NullPointerException e) {
			throw new RuntimeException("minutesUntilEnd(Time): Range not properly initialized");
		}
	}
	

	
	// true if this Range is completely before t
	public boolean before(Time t) {
		try {
			return _end.before(t) && !overnight();
		} catch (NullPointerException e) {
			throw new RuntimeException(
					"before(Time): Range not properly initialized");
		}
	}
	// true if this Range is completely before r
	public boolean before(Range r) {
		return before(r.getStart());
	}
	
	// returns true if this range and r overlap
	// might be implemented better, and currently no consideration is made for the bounds
	public boolean overlap(Range r) {
		return !(before(r) || after(r));
	}
	
	// true if this Range is completely after t
	public boolean after(Time t) {
		try {
			return t.before(getStart());
		} catch (NullPointerException e) {
			throw new RuntimeException("after(Time): Range not properly initialized");
		}
	}
	// true if this Range is completely after r
	public boolean after(Range r) {
		return (r.before(getStart()));
	}
	
	public boolean overnight() {
		return !(_start.before(_end));
	}
	
	public boolean over24Hours() {
		return _start.equals(_end);
	}
	
	// r overlaps with this Range, concatenates r
	// note it is possible for no change to occur, (if r is completely contained in this Range)
	// in this case true is still returned
	//
	// RESULTS IN UNDEFINED BEHAVIOR IF overlap(r) IS NOT TRUE
	// 
	public void concat(Range r) {
		if (r.getStart().before(getStart()))
			setStart(r.getStart());
		int start = _start.totalMinutes();
		int duration = (_end.totalMinutes() - start + 1440) % 1440;
		int durationR = (r.getEnd().totalMinutes() - start + 1440) % 1440;
		if (duration < durationR)
			setEnd(r.getEnd());
	}
	
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean display24) {
		return _start.toString(display24) + " - " + _end.toString(display24);
	}
	
	// true if both _start and _end are not null
	public boolean notNull() {
		return !(_start == null || _end == null);
	}
}
