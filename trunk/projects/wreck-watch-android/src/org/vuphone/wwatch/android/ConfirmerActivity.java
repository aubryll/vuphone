package org.vuphone.wwatch.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
	 * The "constructor" of this Activity. To avoid unpleasant side effects, the
	 * orientation of the screen will be fixed during this activity's lifecycle.
	 */
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		// setContentView(R.layout.confirmer);

		dialog_ = new ConfirmationDialog(this);

		/*
		((Button) findViewById(R.id.test_button))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						fireDialog();
					}
				});
		*/
	}

	/**
	 * The "destructor"
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * The "main" method. Brings up the dialog if started with the correct intent.
	 */
	@Override
	public void onStart() {
		super.onStart();
		Intent intent = this.getIntent();
		if (intent.getBooleanExtra("ShowDialog", false))
			dialog_.show();
	}
}