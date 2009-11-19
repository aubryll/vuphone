package edu.vanderbilt.vuphone.android.dining;

public class Time {
	private int hour_;
	private int minute_;

	public Time(int hour, int minute) {
		if (hour > 23)
			throw new RuntimeException("hour must be < 24");
		if (minute > 59)
			throw new RuntimeException("Minute must be < 60");

		hour_ = hour;
		minute_ = minute;
	}

	public int getHour() {
		return hour_;
	}

	public int getMinute() {
		return minute_;
	}
}