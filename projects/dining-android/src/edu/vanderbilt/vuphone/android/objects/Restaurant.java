package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;

import android.content.Context;
import edu.vanderbilt.vuphone.android.storage.DBAdapter;

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
	public Time getNextOpenTime()				{return _hours.getNextOpenTime();}
	public Time getNextCloseTime()				{return _hours.getNextCloseTime();}
	
	
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
	
	
	// THE BELOW STILL NEEDS TO BE IMPLEMENTED, I JUST THREW THIS TOGETHER WITHOUT KNOWING WHAT I WAS DOING
	// IT DOESNT WORK, THROWS NULL POINTER EXCEPTION
	// ALSO, IT DEFEATS THE PURPOSE OF HAVING SEPERATE METHODS FOR EACH COLUMN
	// SO ALL OF THE BELOW REALLY NEEDS TO BE REIMPLEMENTED lol
	// (also i couldnt tell if the Context param was necessary, but it was needed in the implementation just using DBAdapter,
	// so i assumed it was)
	
	public static ArrayList<Long> getIDs(Context context) {
		return (ArrayList<Long>)(new DBAdapter(context)).openReadable().fetchAllRestaurantIDs();
	}
	public static String getName(Context context, long rowID) {
		return get(context, rowID).getName();
	}		
	public static int getLat(Context context, long rowID) {
		return get(context, rowID).getLat();
	}		
	public static int getLon(Context context, long rowID) 	{
		return get(context, rowID).getLon();
	}
	public static RestaurantHours getHours(Context context, long rowID) {
		return get(context, rowID).getHours();
	}
	public static boolean favorite(Context context,long rowID) {
		return get(context, rowID).favorite();
	}
	public static Restaurant get(Context context,long rowID) { return (new DBAdapter(context)).openReadable().fetchRestaurant(rowID);}
}
