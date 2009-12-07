package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;

public class Restaurant {
		
	private String _name;
	private int _latitude;
	private int _longitude;
	private String _description;
	private boolean _favorite;
	private RestaurantHours _hours;

	public Restaurant() {
		setAttributes("", new RestaurantHours(), 0, 0, false, "");
	}
	public Restaurant(String name) {
		setAttributes(name, new RestaurantHours(), 0, 0, false, "");
	}
	public Restaurant(String name, RestaurantHours hours, int latitude, int longitude, boolean favorite) {
		setAttributes(name, hours, latitude, longitude, favorite, "");
	}
	public Restaurant(String name, RestaurantHours hours, int latitude, int longitude, boolean favorite, String description) {
		setAttributes(name, hours, latitude, longitude, favorite, description);
	}

	public boolean 			isOpen() 			{return _hours.isOpen();}
	public int 				minutesToOpen() 	{return _hours.minutesToOpen();}
	public int 				minutesToClose() 	{return _hours.minutesToClose();}
	public Time 			getNextOpenTime()	{return _hours.getNextOpenTime();}
	public Time 			getNextCloseTime()	{return _hours.getNextCloseTime();}
	
	
	public String 			getName() 			{return _name;}
	public int 				getLat() 			{return _latitude;}
	public int 				getLon() 			{return _longitude;}
	public RestaurantHours 	getHours() 			{return _hours;}
	public boolean 			favorite() 			{return _favorite;}
	public String 			getDescription()	{return _description;}

	public void setAttributes(String name, RestaurantHours hours, 
			int latitude, int longitude, boolean favorite, String description) {
		setName(name);
		setHours(hours);
		setLocation(latitude, longitude);
		setFavorite(favorite);
		setDescription(description);
	}
	public void setName(String name)			{_name = name;}
	public void setLocation(int lat, int lon)	{_latitude = lat; _longitude = lon;}
	public void setHours(RestaurantHours hrs)	{_hours = hrs;}
	public void setFavorite(boolean fav) 		{_favorite = fav;}
	public void setDescription(String desc)		{_description = desc;}
	
	
	public boolean create() 					{return DBWrapper.create(this);}
	
	// static methods for database access
	public static ArrayList<Long> getIDs() 				{return DBWrapper.getIDs();}
	public static Restaurant get(long rowID) 			{return DBWrapper.get(rowID);}
	public static String getName(long rowID) 			{return DBWrapper.getName(rowID);}
	public static int getLat(long rowID) 				{return DBWrapper.getLat(rowID);}
	public static int getLon(long rowID) 				{return DBWrapper.getLon(rowID);}
	public static RestaurantHours getHours(long rowID) 	{return DBWrapper.getHours(rowID);}
	public static boolean favorite(long rowID) 			{return DBWrapper.favorite(rowID);}
	public static boolean create(Restaurant r) 			{return DBWrapper.create(r);}
}

