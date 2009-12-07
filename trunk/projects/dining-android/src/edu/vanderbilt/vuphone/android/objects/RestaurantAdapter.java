package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.dining.R.drawable;
import edu.vanderbilt.vuphone.android.dining.R.layout;

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
	
	// non restaurant item codes, must be negative
	public static final int FAVORITE_PARTITION = -1;
	public static final int OPEN_PARTITION = -2;
	public static final int CLOSED_PARTITION = -3;
	public static final int OTHER_PARTITION = -4;
	
		
	private ArrayList<Long> _order;
	private Context _context;
	private boolean displayFav;
	
	// TODO: Implement partition 
	public RestaurantAdapter(Context context) {
		this(context, DEFAULT);
	}
	public RestaurantAdapter(Context context, int sortType) {
		Log.i("test", "RestaurantAdapter constructer");
		_order = Restaurant.getIDs();
		_context = context;
		setSort(sortType);
	}
	
	public int getCount() {
		return Restaurant.getIDs().size();
	}
	
	public Object getItem(int i) {
		if (getItemId(i)>0)
			return Restaurant.get(getItemId(i));
		else return (int)getItemId(i);
	}
	
	public long getItemId(int i) {
		return _order.get(i);
	}
	
	public View getView(int i, View convertView, ViewGroup parent) {
		// current is either Restaurant or int
		Object current = getItem(i);
		if (current instanceof Restaurant) {
			
			ViewWrapper wrapper;
			// checks if convertView not initialized, or initialized to partition
			if (convertView == null || convertView.getTag()==null) {
				LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.main_list_item, parent, false);
				wrapper = new ViewWrapper(convertView);
				convertView.setTag(wrapper);
			} else {
				wrapper = (ViewWrapper)convertView.getTag();
			}
			
			Restaurant r = (Restaurant)current;
			 if (displayFav) {
				 wrapper.getFavoriteView().setImageResource(r.favorite()?
					R.drawable.dining:		// favorite icon 
					R.drawable.icon);		// nonfavorite icon
				 // TODO add proper favorite/nonfavorite icons
			 } else {
				 wrapper.getFavoriteView().setVisibility(ImageView.GONE);
			 }
			 wrapper.getNameView().setText(r.getName());
			 wrapper.getSpecialView().setText(getSpecialText(r));

	/*		if (!r.isOpen()) {
				((ImageView)parent.findViewById(R.mainListItem.favoriteIcon))
				((TextView)parent.findViewById(R.mainListItem.name))
				((TextView)parent.findViewById(R.mainListItem.specialText))
			}*/
			 return convertView;
		} else {
			TextView partition = new TextView(_context);
			partition.setGravity(Gravity.CENTER);
			partition.setFocusable(false);
			switch ((Integer)current) {
			case FAVORITE_PARTITION:
				partition.setText("Favorites");
				break;
			case OPEN_PARTITION:
				partition.setText("Open");
				break;
			case CLOSED_PARTITION:
				partition.setText("Closed");
				break;				
			case OTHER_PARTITION:
				partition.setText("Other");
			}
			return partition;
			
		}
		
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
	public void setSort(int sortType) {
		// first remove partitions if this is a later sort
		if (_order != null)
			for (int i = 0; i<_order.size(); i++)
				if (_order.get(i)<0)
					_order.remove(i);
		sort(_order, ALPHABETICAL);
		displayFav = true;
		switch (sortType) {
		case FAVORITE_OPEN_CLOSED:
		{
			displayFav = false;
			sort(_order, FAVORITE);
			int nonFav = firstNonFavorite();
			int closed;
			if (nonFav != -1) {
				// if some are non favorites
				sort(_order.subList(nonFav, _order.size()), OPEN_CLOSED);
				closed = firstClosed(nonFav);
			} else {
				closed = -1;
			}
			// add partitions bottom to top, so indices remain valid
			
			if (closed != -1 && nonFav!=-1) 
				// if some restaurants are closed which are not favorites
				_order.add(closed, (long)CLOSED_PARTITION);
			if (nonFav != -1) 
				// if some are non favorites
				_order.add(nonFav, (long)OPEN_PARTITION);
			if (nonFav != 0) 
				// if there are some favorites
				_order.add(0, (long)FAVORITE_PARTITION);

			break;
		}
		case FAVORITE:
		{
			displayFav = false;
			sort(_order, FAVORITE);
			int nonFav = firstNonFavorite();
			if (nonFav != -1) 
				// if some are non favorites
				_order.add(firstNonFavorite(), (long)OTHER_PARTITION);
			if (nonFav != 0) 
				// if there are some favorites
				_order.add(0, (long)FAVORITE_PARTITION);
			break;
		}
		case OPEN_CLOSED:
		{
			sort(_order, OPEN_CLOSED);
			int closed = firstClosed();
			if (closed != -1)
				// if some are closed
				_order.add(closed, (long)CLOSED_PARTITION);
			if (closed != 0) 
				// if some are open
				_order.add(0, (long)OPEN_PARTITION);
			break;
		}
		case NEAR_FAR:
		default:
		}
	}
	
	
	// returns the first instance of a non favorite element in the sort, -1 if all are favorites
	// useful in in lists sorted by favorites
	private int firstNonFavorite() {
		for (int i = 0; i<_order.size(); i++)
			if (!Restaurant.favorite(_order.get(i)))
				return i;
		return -1;
	}
	private int firstClosed(int start) {
		for (int i = start; i<_order.size(); i++)
			if (!Restaurant.getHours(_order.get(i)).isOpen())
				return i;
		return -1;			
	}
	private int firstClosed() {
		return firstClosed(0);
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
	// compare method for merge, 'less or equal'
	private boolean compare(long first, long second, int sortType) {
		switch (sortType) {
		case FAVORITE:
			return Restaurant.favorite(first);
		case ALPHABETICAL:
			return Restaurant.getName(first).compareToIgnoreCase(Restaurant.getName(second))<=0; 
		case OPEN_CLOSED:
			return Restaurant.getHours(first).isOpen();
		case NEAR_FAR:
			return true;
		default:
			return true;
		}
	}
	
}