package org.vuphone.wwatch.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;

/**
 * A class responsible for asking the user if he's OK or taking default action
 * if the user does not reply.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
class ConfirmationDialog extends ProgressDialog implements
		DialogInterface.OnClickListener, DialogInterface.OnDismissListener {

	private static final int DEFAULT_MAX_TIME = 10;
	
	private int maxTime_ = 1;
	private int time_ = 0;
	private Timer timer_ = null;
	private Vibrator vibrator_ = null;

	private final TestingUI activity_;
	
	private TimerTask countdownTask_ = null;

	public void onClick(DialogInterface dialog, int button) {
		switch (button) {
		case DialogInterface.BUTTON_POSITIVE:
			this.fireAccidentIntent(true);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			this.fireAccidentIntent(false);
			break;
		}
	}

	public void onDismiss(DialogInterface dialog) {
		activity_.finish();
	}

	public ConfirmationDialog(TestingUI context) {
		super(context);
		activity_ = context;
		timer_ = new Timer();

		vibrator_ = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);

		maxTime_ = context.getSharedPreferences(VUphone.PREFERENCES_FILE,
				Context.MODE_PRIVATE).getInt(VUphone.TIMEOUT_TAG,
				ConfirmationDialog.DEFAULT_MAX_TIME);

		super.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		super.setMax(maxTime_);
		super.setTitle("Alert");
		super.setMessage("WreckWatch has detected a crash. Did an accident occur?");
		super.setCancelable(false);

		super.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", this);
		super.setButton(DialogInterface.BUTTON_NEGATIVE, "No", this);

		super.setOnDismissListener(this);
		
		countdownTask_ = new TimerTask() {
			public void run() {
				// The countdown should be turned off once the user click
				// a button
				/*
				if (timer_ == null) {
					this.cancel();
					ConfirmationDialog.this.vibrator_.cancel();
					return;
				}*/
				ConfirmationDialog.this
						.setProgress(ConfirmationDialog.this.time_);
				ConfirmationDialog.this.time_++;
				if (time_ > maxTime_) {
					timer_.cancel();
					this.cancel();
					ConfirmationDialog.this.timeout();
				}
			}
		};
	}

	public void show() {
		super.show();
		this.startCountdown();
		vibrator_.vibrate(maxTime_ * 1000);
	}

	private void startCountdown() {
		timer_.cancel();
		timer_ = new Timer();
		time_ = 0;

		timer_.scheduleAtFixedRate(countdownTask_, 0, 1000);
	}

	/**
	 * Fires an intent to the service class reporting that an accident occurred.
	 * Also dismisses this dialog and exits the activity.
	 * 
	 * @param accidentOccurred
	 *            Did an accident occur?
	 */
	private void fireAccidentIntent(boolean accidentOccurred) {
		// Stop the timer so that it doesn't fire again
		countdownTask_.cancel();
		vibrator_.cancel();
		timer_.cancel();

		Intent intent = new Intent(activity_,
				org.vuphone.wwatch.android.GPService.class);
		intent.putExtra("DidAccidentOccur", accidentOccurred);
		activity_.startService(intent);
		super.dismiss();
	}

	/**
	 * Called when the user does not respond. Fires an accident intent
	 */
	private void timeout() {
		this.fireAccidentIntent(true);
	}
}