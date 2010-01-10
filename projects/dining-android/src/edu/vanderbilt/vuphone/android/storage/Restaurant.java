package edu.vanderbilt.vuphone.android.storage;

import java.util.ArrayList;

import android.view.Menu;
import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;
import edu.vanderbilt.vuphone.android.objects.RestaurantMenu;
import edu.vanderbilt.vuphone.android.objects.Time;

public class Restaurant {
	
		
	private String _name;
	private RestaurantHours _hours;
	private RestaurantMenu _menu;
	private String _description;
	private String _type;    // eg 'Cafe,' 'Mediterranian,' 'Coffee Shop'
	private int _icon;
	private int _latitude;
	private int _longitude;
	private boolean _favorite;
	private boolean _mealMoneyAccepted;
	private boolean _mealPlanAccepted;
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
		this(name, hours, favorite, 0, 0, null, null, null, R.drawable.dining_icon, true, true, false, null, null);
	}
	public Restaurant(String name, RestaurantHours hours, boolean favorite, int latitude, int longitude, String type, RestaurantMenu menu,
			String description, int iconId, boolean mealMoneyAccepted, boolean mealPlanAccepted, boolean offCampus, String phoneNumber, String url) {
		setAttributes(name, hours, favorite, latitude, longitude, type, menu, description, iconId, 
				mealMoneyAccepted, mealPlanAccepted, offCampus, phoneNumber, url);
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
	public RestaurantMenu 	getMenu()			{return _menu;}
	public boolean 			favorite() 			{return _favorite;}
	public boolean			mealPlanAccepted()	{return _mealPlanAccepted;}
	public boolean			mealMoneyAccepted()	{return _mealMoneyAccepted;}
	public boolean			onTheCard()			{return _mealMoneyAccepted || _mealPlanAccepted;}
	public boolean			offCampus()			{return _offCampus;}
	public boolean			tasteOfNashville()	{return _mealMoneyAccepted && _offCampus;}
	public String			getPhoneNumber()	{return _phoneNumber;}
	public String			getUrl()			{return _url;}
	public int				getIcon()			{return _icon;}

	
	// I made all the write methods protected so that Restaurants cannot be modified outside the objects package
	// not a completely ideal solution, but unauthorized writes to the restaurant cache must be prevented
	// to alter a Restaurant, use the static methods update() or setX() and then commit(). 
	protected void setAttributes(String name, RestaurantHours hours, boolean favorite, int latitude, int longitude, String type, RestaurantMenu menu,
			String description, int iconId, boolean mealMoneyAccepted, boolean mealPlanAccepted, boolean offCampus, String phoneNumber, String url) {
		setName(name);
		setHours(hours);
		setFavorite(favorite);
		setLocation(latitude, longitude);
		setDescription(description);
		setType(type);
		setMenu(menu);
		setIcon(iconId);
		setMoneyAccepted(mealMoneyAccepted);
		setPlanAccepted(mealPlanAccepted);
		setOffCampus(offCampus);
		setPhoneNumber(phoneNumber);
		setUrl(url);
	}
	protected void setName(String name)				{_name = name;}
	protected void setHours(RestaurantHours hrs)	{_hours = hrs;}
	protected void setLatitude(int latitude)		{_latitude = latitude;}
	protected void setLongidute(int longitude)		{_longitude = longitude;}
	protected void setLocation(int lat, int lon)	{_latitude = lat; _longitude = lon;}
	protected void setFavorite(boolean fav) 		{_favorite = fav;}
	protected void setType(String type)				{_type = type;}
	protected void setMenu(RestaurantMenu menu) 	{_menu = menu;}
	protected void setDescription(String desc)		{_description = desc;}
	protected void setMoneyAccepted(boolean mealMon){_mealMoneyAccepted = mealMon;}
	protected void setPlanAccepted(boolean mealPlan){_mealPlanAccepted = mealPlan;}
	protected void setOffCampus(boolean off)		{_offCampus = off;}
	protected void setPhoneNumber(String number)	{_phoneNumber = number;}
	protected void setUrl(String url)				{_url = url;}
	protected void setIcon(int iconID)				{_icon = iconID;}

	public long create() 							{return DbWrapper.create(this);}
	
	public String toString() {
		StringBuilder out = new StringBuilder();
		if (favorite())
			out.append("*");
		if (offCampus())
			out.append("O");
		if (mealPlanAccepted())
			out.append("P");
		if (mealMoneyAccepted())
			out.append("M");
		out.append(" ");
		out.append(getName()).append("  (").append(getLat()).append(",")
				.append(getLon()).append(")\n").append(getType()).append("\n")
				.append(getDescription()).append("\n").append(getPhoneNumber())
				.append("\n").append(getUrl()).append("\n").append(
						getHours().toString()).append("\n icon hex string: ")
				.append(Integer.toHexString(getIcon()));
		return out.toString();
	}
	
	public boolean equals(Object in) {
		if (in instanceof Restaurant) {
			Restaurant inR = (Restaurant)in;
			if (getName() == null) {
				if (inR.getName() != null)
					return false;
			} else if (!getName().equals(inR.getName()))
				return false;
			
//			if (getHours() == null) {
//				if (inR.getHours() != null)
//					return false;
//			} else if (!getHours().equals(inR.getHours()))
//				return false;
			
			if (getDescription() == null) {
				if (inR.getDescription() != null)
					return false;
			} else if (!getDescription().equals(inR.getDescription()))
				return false;
			
			if (getType() == null) {
				if (inR.getType() != null)
					return false;
			} else if (!getType().equals(inR.getType()))
				return false;
			
//			if (getMenu() == null) {
//				if (inR.getMenu() != null)
//					return false;
//			} else if (!getMenu().equals(inR.getMenu()))
//				return false;
			
			if (getPhoneNumber() == null) {
				if (inR.getPhoneNumber() != null)
					return false;
			} else if (!getPhoneNumber().equals(inR.getPhoneNumber()))
				return false;
			
			if (getUrl() == null) {
				if (inR.getUrl() != null)
					return false;
			} else if (!getUrl().equals(inR.getUrl()))
				return false;
			
			return (getLat() == inR.getLat() &&
					getLon() == inR.getLon() &&
					favorite() == inR.favorite() &&
					mealPlanAccepted() == inR.mealPlanAccepted() &&
					mealMoneyAccepted() == inR.mealMoneyAccepted() &&
					offCampus() == inR.offCampus() &&
					getIcon() == inR.getIcon());
		}
		return false;
	}
	
	// static methods for database access
	public static ArrayList<Long> getIDs() 					{return DbWrapper.getIDs();}
	public static ArrayList<Long> copyIDs()					{return DbWrapper.copyIDs();}
	public static int getI(long rowID)						{return DbWrapper.getI(rowID);}
	public static Restaurant get(long rowID) 				{return DbWrapper.get(rowID);}
	public static String getName(long rowID) 				{return DbWrapper.getName(rowID);}
	public static int getLat(long rowID) 					{return DbWrapper.getLat(rowID);}
	public static int getLon(long rowID) 					{return DbWrapper.getLon(rowID);}
	public static RestaurantHours getHours(long rowID) 		{return DbWrapper.getHours(rowID);}
	public static String getType(long rowID)				{return DbWrapper.getType(rowID);}	
	public static int getIcon (Long rowID)					{return DbWrapper.getIcon(rowID);}
	public static boolean favorite(long rowID) 				{return DbWrapper.favorite(rowID);}
	public static boolean mealPlanAccepted(long rowID)		{return DbWrapper.mealPlanAccepted(rowID);}
	public static boolean mealMoneyAccepted(long rowID) 	{return DbWrapper.mealMoneyAccepted(rowID);}
	public static boolean onTheCard(long rowID)				{return DbWrapper.onTheCard(rowID);}
	public static boolean tasteOfNashville(long rowID)		{return DbWrapper.tasteOfNashville(rowID);}
	public static boolean offCampus(long rowID)				{return DbWrapper.offCampus(rowID);}
	
	public static boolean setFavorite(long rowID, boolean favorite) {return DbWrapper.setFavorite(rowID, favorite);}
	
	public static boolean commit() 							{return DbWrapper.commit();}
	public static void revert()								{DbWrapper.revert();}
	
	public static long create(Restaurant r) 				{return DbWrapper.create(r);}
	public static boolean update(long rowID, Restaurant r)	{return DbWrapper.update(rowID, r);}
	public static boolean delete(long rowID)				{return DbWrapper.delete(rowID);}
	public static boolean deleteAll()						{return DbWrapper.deleteAll();}
	
	// closes the underlying database. Use if no reads or writes are soon to be made (is called often
	// in the underlying database modifying function call, so often unnecessary)
	public static void close()								{DbWrapper.close();}
}

