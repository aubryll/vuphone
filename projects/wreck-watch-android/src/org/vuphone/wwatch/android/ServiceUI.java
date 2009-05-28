package org.vuphone.wwatch.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The entry point into the application. This activity is responsible for
 * starting the service with any necessary configuration data and displaying
 * any visual alerts required by the service. Uses some nasty hacks to get
 * everything to work.
 * @author Krzysztof Zienkiewicz
 *
 */
// TODO - Makes this vibrate or something so that the user knows something's up
class ConfirmationDialog extends ProgressDialog {
	
	static final String MSG = "Time remaining: ";
	static final int MAX_TIME = 10;
	
	private int time_ = 0;
	private Timer timer_ = null;
	
	private DialogInterface.OnClickListener yesListener_ = null;
	private DialogInterface.OnClickListener noListener_ = null;
	
	public ConfirmationDialog(Context context, DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no) {
		super(context);
		timer_ = new Timer();
		
		yesListener_ = yes;
		noListener_ = no;
		
		super.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		super.setMax(MAX_TIME);
		super.setTitle("Alert");
		super.setMessage("WreckWatch has detected a crash. Did an accident occur?");
		super.setCancelable(false);
		
		super.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", yes);
		super.setButton(DialogInterface.BUTTON_NEGATIVE, "No", no);
		
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
	 * Called when the user does not respond. Dismisses the dialog and calls
	 * executes the noListener_.
	 */
	private void timeout() {
		Log.v("VUPHONE", "Timeout");
		super.dismiss();
		noListener_.onClick(ConfirmationDialog.this, DialogInterface.BUTTON_NEGATIVE);
	}
}

public class ServiceUI extends Activity implements View.OnClickListener{

	final static int LAUNCH 	= 0;
	final static int CONFIRM	= 1;
	private int mode_ = 0;
	
	private EditText edit_ = null;
	private Button start_ = null;
	
	
	private ConfirmationDialog dialog = null;
	
	public void onClick(View v) {
		Intent intent = new Intent(this, org.vuphone.wwatch.android.WreckWatchService.class);
		
		double dialation = Double.parseDouble(edit_.getText().toString());
		
		intent.putExtra("TimeDialation", dialation);
		super.startService(intent);
		super.finish();
	}
	
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	Intent startIntent = super.getIntent();
    	
    	Bundle data = startIntent.getExtras();
    	if (data == null)
    		mode_ = ServiceUI.LAUNCH;
    	else
    		mode_ = data.getInt("ActivityMode");

    	switch (mode_) {
    	case ServiceUI.LAUNCH:
    		// Started by the launcher so config
        	LayoutInflater inflater = LayoutInflater.from(this);
        	View config = inflater.inflate(R.layout.main, null);

        	
        	super.setContentView(config);
        	
        	edit_ = (EditText) super.findViewById(R.id.dialation_edit);
        	start_ = (Button) super.findViewById(R.id.start_button);
        	start_.setOnClickListener(this);
        	break;
        
    	case ServiceUI.CONFIRM:
    		// Put up a confirm dialog and return an intent
    		
    		dialog = new ConfirmationDialog(this, new DialogInterface.OnClickListener() {
    			// There was an accident
    			public void onClick(DialogInterface diag, int button) {
    				ServiceUI.super.finish();
    				Intent intent = new Intent(ServiceUI.this, org.vuphone.wwatch.android.WreckWatchService.class);
    				intent.putExtra("DidAccidentOccur", true);
    				ServiceUI.this.startService(intent);
    			}
    		}, new DialogInterface.OnClickListener() {
    			// There was no accident
    			public void onClick(DialogInterface diag, int button) {
    				ServiceUI.super.finish();
    				Intent intent = new Intent(ServiceUI.this, org.vuphone.wwatch.android.WreckWatchService.class);
    				intent.putExtra("DidAccidentOccur", false);
    				ServiceUI.this.startService(intent);
    			}
    		});
    		
    		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
    			public void onDismiss(DialogInterface diag) {
    				ServiceUI.this.finish();
    			}
    		});
    		
        	dialog.show();
        	
        	dialog.startCountdown();
        	break;
    	}

    	
//    	//Testing for Poster
//    	HTTPPoster p = new HTTPPoster();
//    	p.doAccidentPost(Calendar.getInstance().getTimeInMillis(), 500.23, -41.2, null);
    }

    protected void onStart() {
    	super.onStart();
    }
    
    protected void onRestart() {
    	super.onRestart();
    }
    protected void onResume() {
    	super.onResume();
    }
    protected void onPause() {
    	super.onPause();
    }
    protected void onStop() {
    	super.onStop();
    }
    protected void onDestroy() {
    	super.onDestroy();
    }

}
