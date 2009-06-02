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
 * A class responsible for asking the user if he's OK or taking default
 * action if the user does not reply.
 * @author Krzysztof Zienkiewicz
 *
 */
class ConfirmationDialog extends ProgressDialog implements 
	DialogInterface.OnClickListener, DialogInterface.OnDismissListener{
	
	static final int MAX_TIME = 10;
	private int time_ = 0;
	private Timer timer_ = null;
	private Vibrator vibrator_ = null;
	
	private final TestingUI activity_;
	
	public void onClick(DialogInterface dialog, int button) {
		switch (button){
		case DialogInterface.BUTTON_POSITIVE:
			this.fireAccidentIntent(true);
			vibrator_.cancel();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			this.fireAccidentIntent(false);
			vibrator_.cancel();
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
		
		vibrator_ = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


		super.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		super.setMax(MAX_TIME);
		super.setTitle("Alert");
		super.setMessage("WreckWatch has detected a crash. Did an accident occur?");
		super.setCancelable(false);
		
		super.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", this);
		super.setButton(DialogInterface.BUTTON_NEGATIVE, "No", this);
		
		super.setOnDismissListener(this);
	}
	
	public void show() {
		super.show();
		this.startCountdown();
		vibrator_.vibrate(MAX_TIME * 1000);
	}
	
	private void startCountdown() {
		if (timer_ != null)
			timer_.cancel();
		timer_ = new Timer();
		time_ = 0;
		
		timer_.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				// The countdown should be turned off once the user click
				// a button
				if (timer_ == null) {
					this.cancel();
					ConfirmationDialog.this.vibrator_.cancel();
					return;
				}
				ConfirmationDialog.this.setProgress(ConfirmationDialog.this.time_);
				ConfirmationDialog.this.time_++;
				if (time_ > MAX_TIME) {
					timer_.cancel();
					ConfirmationDialog.this.timeout();
				}
			}
		}, 0, 1000);
	}
	
	/**
	 * Fires an intent to the service class reporting that an accident
	 * occurred. Also dismisses this dialog and exits the activity.
	 * 
	 * @param accidentOccurred	Did an accident occur?
	 */
	private void fireAccidentIntent(boolean accidentOccurred) {
		// Stop the timer so that it doesn't fire again
		timer_.cancel();
		timer_ = null;	// Bad practice but its the easiest way to turn off
						// the current countdown
		
		Intent intent = new Intent(activity_, org.vuphone.wwatch.android.GPService.class);
		intent.putExtra("DidAccidentOccur", accidentOccurred);
		activity_.startService(intent);
		super.dismiss();
	}
	
	/**
	 * Called when the user does not respond. Fires an accident intent
	 */
	private void timeout() {
		Log.v("VUPHONE", "Timeout");
		this.fireAccidentIntent(true);		
	}
}