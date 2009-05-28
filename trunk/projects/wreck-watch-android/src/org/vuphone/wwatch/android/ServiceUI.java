package org.vuphone.wwatch.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The entry point into the application. This activity is responsible for
 * starting the service with any necessary configuration data and displaying
 * any visual alerts required by the service. Uses some nasty hacks to get
 * everything to work.
 * @author Krzysztof Zienkiewicz
 *
 */
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
    		dialog = new ConfirmationDialog(this);
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
