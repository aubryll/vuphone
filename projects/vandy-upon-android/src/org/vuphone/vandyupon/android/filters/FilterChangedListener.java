/**
 * 
 */
package org.vuphone.vandyupon.android.filters;

/**
 * Filters are designed to be immutable, but some are mutable by nature (such as
 * the PositionFilter - it can elect to change itself if it is attempting to
 * stay centered on the current location). This interface allows classes to be
 * notified when a filter changes.
 * 
 * @author Hamilton Turner
 * 
 */
public interface FilterChangedListener {

	/**
	 * A filter changed, all used filter values should be re-queried
	 */
	public void filterChanged();
}
