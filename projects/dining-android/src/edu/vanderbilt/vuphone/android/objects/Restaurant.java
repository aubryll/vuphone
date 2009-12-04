package edu.vanderbilt.vuphone.android.objects;

public class Restaurant {
	private String _name;
	private int _latitude;
	private int _longitude;
	private boolean _favorite;
	private RestaurantHours _hours;

	public Restaurant() {
		setAttributes("", null, 0, 0, false);
	}
	public Restaurant(String name) {
		setAttributes(name, null, 0, 0, false);
	}
	public Restaurant(String name, RestaurantHours hours, int latitude, int longitude, boolean favorite) {
		setAttributes(name, hours, latitude, longitude, favorite);
	}

	public boolean isOpen() 					{return _hours.isOpen();}
	public int minutesToOpen() 					{return _hours.minutesToOpen();}
	public int minutesToClose() 				{return _hours.minutesToClose();}
	
	
	public String getName() 					{return _name;}
	public int getLat() 						{return _latitude;}
	public int getLon() 						{return _longitude;}
	public RestaurantHours getHours() 			{return _hours;}
	public boolean favorite() 					{return _favorite;}
	

	public void setAttributes(String name, RestaurantHours hours, int latitude, int longitude, boolean favorite) {
		setName(name);
		setHours(hours);
		setLocation(latitude, longitude);
		setFavorite(favorite);
	}
	public void setName(String name)			{_name = name;}
	public void setLocation(int lat, int lon)	{_latitude = lat; _longitude = lon;}
	public void setHours(RestaurantHours hrs)	{_hours = hrs;}
	public void setFavorite(boolean fav) 		{_favorite = fav;}
}
