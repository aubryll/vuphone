package edu.vanderbilt.vuphone.android.dining;

public class Range {
	private Time start_;
	private Time end_;

	// Needed for XStream!
	public Range() {
		start_=new Time();
		end_=new Time();
	};
	
	public Range(Time start, Time end) {
		if (end.before(start))
			throw new RuntimeException("Range end time before start time");

		start_ = new Time(start);
		end_ = new Time(end);
	}

	public Time getStart() {
		return start_;
	}

	public Time getEnd() {
		return end_;
	}

	public boolean inRange(Time t) {
		return t.before(end_) && start_.before(t);
	}
	
	// returns a negative number if start_ is after t
	public int minutesUntilStart(Time t) {
		return start_.totalMinutes() - t.totalMinutes();
	}
	
	// returns a negative number if end_ is after t
	public int minutesUntilEnd(Time t) {
		return start_.totalMinutes() - t.totalMinutes();
	}
}
