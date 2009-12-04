package edu.vanderbilt.vuphone.android.objects;

public class Range {
	private Time _start;
	private Time _end;

	// Needed for XStream!
	public Range() {};
	public Range(Time start, Time end) {setTimes(start, end);}

	public Time getStart() {return _start;}
	public Time getEnd() {return _end;}
	

	// sets start time for range, 
	// false: failure because end was before the beginning
	// true: success
	public boolean setStart(Time start) {
		if (_end==null || start.before(_end)) {
			_start = start;
			return true;
		}
		return false;	
	}
	
	// sets end time for range, 
	// false: failure because end was before the beginning
	// true: success
	public boolean setEnd(Time end) {
		if (_start == null || _start.before(end)) {
			_end = end;
			return true;
		}
		return false;
	}
	
	// sets start and end times, and throws an exception if end is before start
	public void setTimes(Time start, Time end) {
		_start = null;
		_end = null;
		if (!setStart(start) || !setEnd(end))
			throw new RuntimeException("Invalid Range created, end time before start");
	}

	// returns whether t is in the current range or not. 
	public boolean inRange(Time t) {
		if (notNull())
			return t.before(_end) && _start.before(t);
		else return false;
	}
	
	// returns a negative number if start_ is after t
	public int minutesUntilStart(Time t) {
		if (notNull())
			return _start.totalMinutes() - t.totalMinutes();
		else
			throw new RuntimeException("minutesUntilStart(Time): Range not properly initialized");
	}
	
	// returns a negative number if end_ is after t
	public int minutesUntilEnd(Time t) {
		if (notNull())
			return _start.totalMinutes() - t.totalMinutes();
		else
			throw new RuntimeException("minutesUntilEnd(Time): Range not properly initialized");
	}
	

	
	// true if t is before this Range
	public boolean before(Time t) {
		if (notNull())
			return t.before(getStart());
		else throw new RuntimeException("before(Time): Range not properly initialized");
	}
	// true if r is entirely before this Range
	public boolean before(Range r) {
		return (before(r.getStart()) && before(r.getEnd()));
	}
	
	// true if t is in the current Range
	public boolean during(Time t) {
		if (notNull())
			return getStart().before(t) && t.before(getEnd());
		else throw new RuntimeException("after(Time): Range not properly initialized");
	}	
	// returns true if this range and r overlap
	// might be implemented better, and currently no consideration is made for the bounds
	public boolean overlap(Range r) {
		return !(r.before(this) || r.after(this));
	}
	
	// true if t is after this Range
	public boolean after(Time t) {
		if (notNull())
			return getEnd().before(t);
		else throw new RuntimeException("after(Time): Range not properly initialized");
	}
	// true if this Range is completely after r
	public boolean after(Range r) {
		return (r.getEnd().before(getStart()));
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
		if (getEnd().before(r.getEnd()))
			setEnd(r.getEnd());
	}
	
	// true if both _start and _end are not null
	public boolean notNull() {
		return !(_start == null || _end == null);
	}
}
