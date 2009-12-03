package edu.vanderbilt.vuphone.android.dining;

public class Range {
	private Time start_;
	private Time end_;

	// Needed for XStream!
	public Range() {};
	
	public Range(Time start, Time end) {
		if (end.getHour() < start.getHour())
			throw new RuntimeException("oops");
		if ((end.getHour() == start.getHour())
				&& (end.getMinute() <= start.getMinute()))
			throw new RuntimeException("oops");

		start_ = start;
		end_ = end;
	}

	public Time getStart() {
		return start_;
	}

	public Time getEnd() {
		return end_;
	}

	public boolean inRange(Time t) {

		if (t.getHour() > end_.getHour() || t.getHour() < start_.getHour())
			return false;
		else if ((t.getMinute() > end_.getMinute())
				|| (t.getMinute() < start_.getMinute()))
			return false;
		else
			return true;
	}
}
