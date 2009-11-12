package edu.vanderbilt.vuphone.android.dining;

import java.util.Calendar;
import java.util.List;

public class Restaurant {
	private String _name;
	private List<CalendarRange> _open;
	private int _latitude;
	private int _longitude;
	private boolean _favorite;
	
	public Restaurant(String name) {
		_name = name;
	}
	public Restaurant(String name, Calendar start, Calendar stop) {
		_name = name;
		setOpenTime(start, stop);
	}
	
	
	// returns whether now is contained in the ranges of _open
	public boolean isOpen() {
		for (int i = 0; i < _open.size(); i++)
			if (_open.get(i).isInRangeInc(Calendar.getInstance()))
				return true;
		return false;	
	}
	
	public void setOpenTime(Calendar start, Calendar stop) {
		_open.add(new CalendarRange(start, stop));
	}
	
	public void setOpenTime(CalendarRange range) {
		_open.add(range);
	}
	
	
}
