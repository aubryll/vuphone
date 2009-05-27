package org.vuphone.wwatch.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The entry point into the application. This activity is responsible for
 * starting the service with any necessary configuration data. 
 * @author Krzysztof Zienkiewicz
 *
 */

public class ServiceStarter extends Activity implements View.OnClickListener{

	private EditText edit_ = null;
	private Button start_ = null;
	
	public void onClick(View v) {
		Intent intent = new Intent(this, org.vuphone.wwatch.android.WreckWatchService.class);
		
		double dialation = Double.parseDouble(edit_.getText().toString());
		
		intent.putExtra("TimeDialation", dialation);
		super.startService(intent);
		super.finish();
	}
	
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	super.setContentView(R.layout.main);
    	
    	edit_ = (EditText) super.findViewById(R.id.dialation_edit);
    	start_ = (Button) super.findViewById(R.id.start_button);
    	start_.setOnClickListener(this);    	
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
