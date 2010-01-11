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
		STARTED, FINISHED
	}
	
	public void OnEventLoadStateChanged(LoadState l);
}
