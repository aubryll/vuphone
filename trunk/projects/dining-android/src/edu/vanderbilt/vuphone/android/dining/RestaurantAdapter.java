package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.vanderbilt.vuphone.android.objects.Restaurant;

public class RestaurantAdapter extends BaseAdapter {

	// alphabetical sort
	public static final int ALPHABETICAL = 0;
	
	// secondary sorts, all include alphabetical
	public static final int FAVORITE = 1;
	public static final int OPEN_CLOSED = 2;
	public static final int NEAR_FAR = 3;
	
	// tertiary sorts, all include alphabetical
	public static final int FAVORITE_OPEN_CLOSED = 5;
	
	public static final int DEFAULT = FAVORITE_OPEN_CLOSED;
	
	// display constants
	
		
	private ArrayList<Long> _order;
	private Context _context;
	private int _sortType;
	
	// TODO: Implement partition 
	public RestaurantAdapter(Context context) {
		Log.i("test", "RestaurantAdapter constructer");
		_order = Restaurant.getIDs(_context);
		_context = context;
		setSort(DEFAULT);
	}
	
	public int getCount() {
		return Restaurant.getIDs(_context).size();
	}
	
	public Restaurant getItem(int i) {
		return Restaurant.get(_context, getItemId(i));
	}
	
	public long getItemId(int i) {
		return _order.get(i);
	}
	
	public View getView(int i, View convertView, ViewGroup parent) {
		Restaurant r = getItem(i);
		LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*View out = */inflater.inflate(R.layout.main_list_item, parent);
		((ImageView)parent.findViewById(R.mainListItem.favoriteIcon)).
			setImageResource(r.favorite()?R.drawable.dining:R.drawable.icon);
		((TextView)parent.findViewById(R.mainListItem.name)).setText(r.getName());
		((TextView)parent.findViewById(R.mainListItem.specialText)).setText(getSpecialText(r));
/*		if (!r.isOpen()) {
			((ImageView)parent.findViewById(R.mainListItem.favoriteIcon))
			((TextView)parent.findViewById(R.mainListItem.name))
			((TextView)parent.findViewById(R.mainListItem.specialText))
		}*/
		return parent;
	}
	
	// returns a string that appears under the restaurant name on the front page
	private String getSpecialText(Restaurant r) {
		int toOpen = r.minutesToOpen();
		if (toOpen==0) {
			int min = r.minutesToClose();
			if (min<=60)
				return "closes in " + min + " minutes";
			else return "closes at " + r.getNextCloseTime().toString();
		} else if (toOpen>0) {
			if (toOpen<=60)
				return "opens in " + toOpen + " minutes";
			else return "opens at " + r.getNextOpenTime().toString();
		} else return "closed"; // closed for the day
	}
	
	// sets sort method for list, sorts, and returns the position of the favorites partition
	// -1 if no mergeSort includes no favorites partition
	public int setSort(int mergeSortType) {
		_sortType=mergeSortType;
		sort(_order, ALPHABETICAL);
		switch (_sortType) {
		case FAVORITE_OPEN_CLOSED:
			sort(_order, FAVORITE);
			int nonFav = firstNonFavorite();
			sort(_order.subList(nonFav, _order.size()), OPEN_CLOSED);
			return nonFav;
		case FAVORITE:
			sort(_order, FAVORITE);
			return firstNonFavorite();
		case OPEN_CLOSED:
			sort(_order, OPEN_CLOSED);
			return -1;
		case NEAR_FAR:
			return -1;
		default:
			return -1;
		}
	}
	
	
	// returns the first instance of a non favorite element in the sort, -1 if all are favorites
	// useful in in lists sorted by favorites
	private int firstNonFavorite() {
		for (int i = 0; i<_order.size(); i++)
			if (!Restaurant.favorite(_context, _order.get(i)))
				return i;
		return -1;
	}
	//implementation of merge sort
	private void sort(List<Long> toSort, int sortType) {
		if (toSort.size()<=1)
			return;
		int center = toSort.size()/2;
		ArrayList<Long> left = new ArrayList<Long>();
		left.addAll(toSort.subList(0, center));
		ArrayList<Long> right = new ArrayList<Long>();
		right.addAll(toSort.subList(center, toSort.size()));
		sort(left, sortType);
		sort(right, sortType);
		int li = 0, ri = 0, i=0;
		while (li<left.size() && ri<right.size()) {
			if (compare(left.get(li), right.get(ri), sortType))
				toSort.set(i++, left.get(li++));
			else toSort.set(i++, right.get(ri++));
		}
		if (left.size()>0)
			for (;li < left.size();)
				toSort.set(i++, left.get(li++));
		else 
			for (;ri < right.size();)
				toSort.set(i++, right.get(ri++));
	}
	// compare method for merge
	private boolean compare(long first, long second, int sortType) {
		switch (sortType) {
		case FAVORITE:
			return Restaurant.favorite(_context, first);
		case ALPHABETICAL:
			return Restaurant.getName(_context, first).compareToIgnoreCase(Restaurant.getName(_context, second))<=0; 
		case OPEN_CLOSED:
			return Restaurant.getHours(_context, first).isOpen();
		case NEAR_FAR:
			return true;
		default:
			return true;
		}
	}
	
}