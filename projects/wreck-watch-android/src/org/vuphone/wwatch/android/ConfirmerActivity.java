package org.vuphone.wwatch.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * An activity responsible for confirming whether the user was in an accident
 * and taking the appropriate actions. This activity should only be triggered if
 * the system detects that an accident has occurred. It will at that point ask a
 * user to confirm or deny it. Should the user deny this claim, the activity
 * will simply finish. Should he confirm or neglect to take any action within a
 * set period of time, the activity will report an accident and then finish.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class ConfirmerActivity extends Activity {

	private ConfirmationDialog dialog_ = null;
	
	private void fireDialog() {
		if (!dialog_.isShowing())
			dialog_.show();
	}
	
	/**
	 * The "main" method of this Activity. Note, we're assuming that this
	 * Activity is triggered only when an accident is detected. Therefore, we
	 * can go ahead and pull up the dialog, leaving the onStart() method empty.
	 * To avoid unpleasant side effects, the orientation of the screen will be
	 * fixed during this activity's lifecycle.
	 */
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.confirmer);
		
		dialog_ = new ConfirmationDialog(this);
		
		((Button) findViewById(R.id.test_button)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				fireDialog();
			}
		});
	}

	/**
	 * The "destructor"
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
}