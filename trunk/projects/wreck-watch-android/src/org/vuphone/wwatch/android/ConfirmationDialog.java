package org.vuphone.wwatch.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

/**
 * A class responsible for asking the user if he's OK or taking default action
 * if the user does not reply. Note, GPService started with a specific intent is
 * still responsible for pushing the information to the server.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
class ConfirmationDialog extends ProgressDialog implements
		DialogInterface.OnClickListener {

	private static final int DEFAULT_MAX_TIME = 10;

	private final int maxTime_;
	private final Timer timer_ = new Timer();
	private final Vibrator vibrator_;

	private final ConfirmerActivity activity_;
	private CountdownTimerTask countdownTask_ = new CountdownTimerTask();

	/**
	 * A private inner class responsible for counting down the dialog. For
	 * thread safety, this class is responsible for keeping track of its current
	 * time
	 * 
	 * @author Krzysztof Zienkiewicz
	 * 
	 */
	private class CountdownTimerTask extends TimerTask {

		/**
		 * Current time. Starts at -1 to correctly adjust for the first call to
		 * run
		 */
		private int time_ = -1;

		/**
		 * A no-op constructor
		 */
		public CountdownTimerTask() {
			super();
			Log.v(VUphone.tag, "TimerTask ctor");
		}

		/**
		 * Stops the countdown and removes it from the timer's queue. Note, this
		 * task cannot be scheduled again.
		 */
		public void reset() {
			cancel();
			timer_.purge();
			time_ = -1;
		}

		/**
		 * Increaments the counter. If this timeouts, this task is canceled,
		 * removing it from the timer's queue and purges the timer. report(true)
		 * is then called.
		 */
		@Override
		public void run() {
			++time_;
			setProgress(time_);

			if (time_ > maxTime_) {
				Log.v(VUphone.tag, "TimerTask.run() entering timeout");
				report(true);
			}
		}
	}

	public ConfirmationDialog(final ConfirmerActivity con) {
		super(con);
		activity_ = con;
		vibrator_ = (Vibrator) con.getSystemService(Context.VIBRATOR_SERVICE);
		maxTime_ = con.getSharedPreferences(VUphone.PREFERENCES_FILE,
				Context.MODE_PRIVATE).getInt(VUphone.TIMEOUT_TAG,
				ConfirmationDialog.DEFAULT_MAX_TIME);

		setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		setMax(maxTime_);
		setTitle("Alert");
		setMessage("WreckWatch has detected a crash. Did an accident occur?");
		setCancelable(false);

		setButton(DialogInterface.BUTTON_POSITIVE, "Yes", this);
		setButton(DialogInterface.BUTTON_NEGATIVE, "No", this);

	}

	public void onClick(DialogInterface dialog, int button) {
		switch (button) {
		case DialogInterface.BUTTON_POSITIVE:
			report(true);
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			report(false);
			break;
		}
	}

	/**
	 * Hides the dialog, stops the vibrator and timer, and reports the accident,
	 * if necessary.
	 * 
	 * @param occurred
	 *            True if we want to report an accident
	 */
	private void report(boolean occurred) {
		vibrator_.cancel();
		countdownTask_.reset();
		// For some reason, if we enter here because of a timeout, calling
		// Toasts silently hangs. TODO - figure this out
		cancel();
		setProgress(0);
		
		Intent intent = new Intent(activity_,
				org.vuphone.wwatch.android.GPService.class);
		intent.putExtra("DidAccidentOccur", occurred);
		activity_.startService(intent);
	}

	/**
	 * Bring up the dialog and start the countdown.
	 */
	@Override
	public void show() {
		super.show();
		startCountdown();
	}

	/**
	 * Resets the TimerTask and adds it to the Timer queue. Turns on the
	 * vibrator
	 */
	private void startCountdown() {
		countdownTask_.reset();
		countdownTask_ = new CountdownTimerTask();

		timer_.scheduleAtFixedRate(countdownTask_, 0, 1000);
		vibrator_.vibrate(maxTime_ * 1000);
	}
}