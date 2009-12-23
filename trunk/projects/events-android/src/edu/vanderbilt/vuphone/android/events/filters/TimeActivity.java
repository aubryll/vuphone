/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.filters;

import android.app.Activity;
import android.os.Bundle;
import edu.vanderbilt.vuphone.android.events.R;

/**
 * @author Hamilton Turner
 *
 */
public class TimeActivity extends Activity {
	
	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		setContentView(R.layout.time_filter);
	}

}
