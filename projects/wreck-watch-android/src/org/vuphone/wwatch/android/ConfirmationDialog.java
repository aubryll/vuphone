package org.vuphone.wwatch.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

//TODO - Makes this vibrate or something so that the user knows something's up
class ConfirmationDialog extends ProgressDialog implements 
	DialogInterface.OnClickListener, DialogInterface.OnDismissListener{
	
	static final int MAX_TIME = 10;
	private int time_ = 0;
	private Timer timer_ = null;
	private final ServiceUI activity_;
	
	public void onClick(DialogInterface dialog, int button) {
		switch (button){
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
	
	public ConfirmationDialog(ServiceUI context) {
		super(context);
		activity_ = context;
		timer_ = new Timer();

		super.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		super.setMax(MAX_TIME);
		super.setTitle("Alert");
		super.setMessage("WreckWatch has detected a crash. Did an accident occur?");
		super.setCancelable(false);
		
		super.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", this);
		super.setButton(DialogInterface.BUTTON_NEGATIVE, "No", this);
		
		super.setOnDismissListener(this);
	}
	
	public void startCountdown() {
		timer_.cancel();
		timer_ = new Timer();
		time_ = 0;
		
		timer_.scheduleAtFixedRate(new TimerTask() {
			public void run() {
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
		Intent intent = new Intent(activity_, org.vuphone.wwatch.android.WreckWatchService.class);
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