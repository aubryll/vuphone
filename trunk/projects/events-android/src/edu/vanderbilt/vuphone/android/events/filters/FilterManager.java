/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.filters;

import java.util.ArrayList;

import edu.vanderbilt.vuphone.android.events.Constants;

import android.util.Log;

/**
 * @author Hamilton Turner
 * 
 */
public class FilterManager {
	private static final String tag = Constants.tag;
	private static final String pre = "FilterManager: ";

	private static PositionFilter positionFilter_ = null;
	private static TimeFilter timeFilter_ = null;

	private static ArrayList<PositionFilterListener> positionFilterObservers_ = new ArrayList<PositionFilterListener>();
	private static ArrayList<TimeFilterListener> timeFilterObservers_ = new ArrayList<TimeFilterListener>();

	private FilterManager() {
	}

	public static void updateFilter(Filter f) {
		if (f instanceof PositionFilter) {
			positionFilter_ = (PositionFilter) f;
			for (PositionFilterListener n : positionFilterObservers_)
				n.filterUpdated(positionFilter_);
		} else if (f instanceof TimeFilter) {
			timeFilter_ = (TimeFilter) f;
			for (TimeFilterListener n : timeFilterObservers_)
				n.filterUpdated(timeFilter_);
		}
	}

	// TODO - do this another way.
	public static void registerFilterListener(FilterListener listener) {
		if (listener instanceof PositionFilterListener)
			positionFilterObservers_.add((PositionFilterListener) listener);
		
		if (listener instanceof TimeFilterListener)
			timeFilterObservers_.add((TimeFilterListener) listener);
		
		else
			Log
					.e(
							tag,
							pre
									+ "Tried to register a FilterListener that not one of the known types. "
									+ "This likely means someone created one of their own. Use the pre-defined FilterListeners instead.");

	}

	public static void unregisterFilterListener(FilterListener listener) {
		if (listener instanceof PositionFilterListener)
			positionFilterObservers_.remove((PositionFilterListener) listener);
		else if (listener instanceof TimeFilterListener)
			timeFilterObservers_.remove((TimeFilterListener) listener);
		else
			Log
					.w(
							tag,
							pre
									+ "Tried to unregister a FilterListener that not one of the known types. ");
	}
	
	public static TimeFilter getCurrentTimeFilter() {
		return timeFilter_;
	}

}
