/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.eventloader;

/**
 * @author Hamilton Turner
 *
 */
public interface LoadingListener {
	
	public enum LoadState {
		STARTED, ONE_EVENT, FINISHED_WITH_ERROR, FINISHED
	}
	
	public void OnEventLoadStateChanged(LoadState l);
}
