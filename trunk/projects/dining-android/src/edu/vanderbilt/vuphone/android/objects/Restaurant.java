package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;

public class Restaurant {
		
	private String _name;
	private RestaurantHours _hours;
	private Menu _menu;
	private String _description;
	private String _type;    // eg 'Cafe,' 'Mediterranian,' 'Coffee Shop'
	private int _icon;
	private int _latitude;
	private int _longitude;
	private boolean _favorite;
	private boolean _onTheCard;
	private boolean _offCampus; // taste of nashville
	private String _phoneNumber;
	private String _url;

	public Restaurant() {
		this(null);
	}
	public Restaurant(String name) {
		this(name, null, false);
	}
	public Restaurant(String name, RestaurantHours hours, boolean favorite) {
		this(name, hours, favorite, 0, 0, null, null, null, 0x0, true, false, null, null);
	}
	public Restaurant(String name, RestaurantHours hours, boolean favorite, int latitude, int longitude, String type, Menu menu,
			String description, int iconId, boolean onTheCard, boolean offCampus, String phoneNumber, String url) {
		setAttributes(name, hours, favorite, latitude, longitude, type, menu, description, iconId, onTheCard, offCampus, phoneNumber, url);
	}

	public boolean 			isOpen() 			{return _hours.isOpen();}
	public int 				minutesToOpen() 	{return _hours.minutesToOpen();}
	public int 				minutesToClose() 	{return _hours.minutesToClose();}
	public Time 			getNextOpenTime()	{return _hours.getNextOpenTime();}
	public Time 			getNextCloseTime()	{return _hours.getNextCloseTime();}
	
	// these methods may return null
	public String 			getName() 			{return _name;}
	public int 				getLat() 			{return _latitude;}
	public int 				getLon() 			{return _longitude;}
	public RestaurantHours 	getHours() 			{return _hours;}
	public String 			getDescription()	{return _description;}
	public String 			getType()			{return _type;}
	public Menu 			getMenu()			{return _menu;}
	public boolean 			favorite() 			{return _favorite;}
	public boolean			onTheCard()			{return _onTheCard;}
	public boolean			offCampus()			{return _offCampus;}
	public boolean			tasteOfNashville()	{return _onTheCard && _offCampus;}
	public String			getPhoneNumber()	{return _phoneNumber;}
	public String			getUrl()			{return _url;}
	public int				getIcon()			{return _icon;}

	public void setAttributes(String name, RestaurantHours hours, boolean favorite, int latitude, int longitude, String type, Menu menu,
			String description, int iconId, boolean onTheCard, boolean offCampus, String phoneNumber, String url) {
		setName(name);
		setHours(hours);
		setFavorite(favorite);
		setLocation(latitude, longitude);
		setDescription(description);
		setType(type);
		setMenu(menu);
		setIcon(iconId);
		setOnTheCard(onTheCard);
		setOffCampus(offCampus);
		setPhoneNumber(phoneNumber);
		setUrl(url);
	}
	public void setName(String name)			{_name = name;}
	public void setHours(RestaurantHours hrs)	{_hours = hrs;}
	public void setLatitude(int latitude)		{_latitude = latitude;}
	public void setLongidute(int longitude)		{_longitude = longitude;}
	public void setLocation(int lat, int lon)	{_latitude = lat; _longitude = lon;}
	public void setFavorite(boolean fav) 		{_favorite = fav;}
	public void setType(String type)			{_type = type;}
	public void setMenu(Menu menu) 				{_menu = menu;}
	public void setDescription(String desc)		{_description = desc;}
	public void	setOnTheCard(boolean card)		{_onTheCard = card;}
	public void	setOffCampus(boolean off)		{_offCampus = off;}
	public void	setPhoneNumber(String number)	{_phoneNumber = number;}
	public void setUrl(String url)				{_url = url;}
	public void	setIcon(int iconID)				{_icon = iconID;}

	public boolean create() 					{return DBWrapper.create(this);}
	
	public String toString() {
		StringBuilder out = new StringBuilder();
		if (favorite())
			out.append("* ");
		out.append(getName()).append("  (").append(getLat()).append(",").append(getLon()).
			append(")\n").append(getDescription()).append("\n").append(getHours().toString());
		return out.toString();
	}
	
	// static methods for database access
	public static ArrayList<Long> getIDs() 				{return DBWrapper.getIDs();}
	public static Restaurant get(long rowID) 			{return DBWrapper.get(rowID);}
	public static String getName(long rowID) 			{return DBWrapper.getName(rowID);}
	public static int getLat(long rowID) 				{return DBWrapper.getLat(rowID);}
	public static int getLon(long rowID) 				{return DBWrapper.getLon(rowID);}
	public static RestaurantHours getHours(long rowID) 	{return DBWrapper.getHours(rowID);}
	public static String getType(long rowID)			{return DBWrapper.getType(rowID);}	
	public static boolean favorite(long rowID) 			{return DBWrapper.favorite(rowID);}
	public static boolean onTheCard(long rowID)			{return DBWrapper.onTheCard(rowID);}
	public static boolean offCampus(long rowID)			{return DBWrapper.offCampus(rowID);}
	public static boolean create(Restaurant r) 			{return DBWrapper.create(r);}
}

