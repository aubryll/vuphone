package edu.vanderbilt.vuphone.android.objects;

public class Restaurant {
	private String _name;
	private int _latitude;
	private int _longitude;
	private boolean _favorite;
	private RestaurantHours _hours;

	public Restaurant(String name) {
		_name = name;
	}

	public Restaurant(String name, RestaurantHours hours) {
		_name = name;
		_hours = hours;
	}

	public boolean isOpen() {
		return _hours.isOpen();
	}

}
