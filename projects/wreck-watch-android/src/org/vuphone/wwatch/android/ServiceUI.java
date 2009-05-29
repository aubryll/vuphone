package org.vuphone.wwatch.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
	private EditText accelScaleEdit_ = null;
	private Button start_ = null;
	private Button stop_ = null;
	private Button test_ = null;
	
	private TextView scaleSpeed_ = null;
	private TextView realSpeed_ = null;
	private TextView realAccel_ = null;
	private TextView scaleAccel_ = null;
	private TextView lastGps_ = null;
	private TextView numWaypoints_ = null;
	
	
	
	private ConfirmationDialog dialog = null;
	
	public void onClick(View v) {
		Intent intent = new Intent(this, org.vuphone.wwatch.android.WreckWatchService.class);
		
		if (v.equals(start_)) {
			double dialation = 1.0;
			double accelScale = 1.0;
			
			// Swallow any exception here.
			try{
				dialation = Double.parseDouble(edit_.getText().toString());
				accelScale = Float.parseFloat(accelScaleEdit_.getText().toString());
			}catch(Exception e) {}
			
		
			intent.putExtra("TimeDialation", dialation);
			intent.putExtra("AccelerationScaleFactor", accelScale);
			
			super.startService(intent);
			super.finish();
		}else if (v.equals(stop_)){
			super.stopService(intent);
		}else if (v.equals(test_)){
			Intent intent2 = new Intent(this,
					org.vuphone.wwatch.android.ServiceUI.class);


			intent2.putExtra("ActivityMode", ServiceUI.CONFIRM);
			startActivity(intent2);

		}
	}
	
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	scaleSpeed_ = (TextView) findViewById(R.id.scale_speed);
    	realSpeed_ = (TextView) findViewById(R.id.real_speed);
    	realAccel_ = (TextView) findViewById(R.id.real_accel);
    	scaleAccel_ = (TextView) findViewById(R.id.scale_accel);
    	lastGps_ = (TextView) findViewById(R.id.last_gps);
    	numWaypoints_ = (TextView) findViewById(R.id.num_gps);
    	
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
        	accelScaleEdit_ = (EditText) super.findViewById(R.id.accel_scale);
        	start_ = (Button) super.findViewById(R.id.start_button);
        	start_.setOnClickListener(this);
        	
        	stop_ = (Button) super.findViewById(R.id.stop_button);
        	stop_.setOnClickListener(this);
        	
        	test_ = (Button) super.findViewById(R.id.test_button);
        	test_.setOnClickListener(this);
        	
        	break;
        
    	case ServiceUI.CONFIRM:
    		// Put up a confirm dialog and return an intent
    		dialog = new ConfirmationDialog(this);
        	dialog.show();
        	break;
    	}

    	
//    	//Testing for Poster
    	
//    	HTTPPoster.doAccidentPost(Calendar.getInstance().getTimeInMillis(), 500.23, -41.2, null);
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
    
    private ISettingsViewCallback callback_ = new ISettingsViewCallback.Stub() {
    	private int m_ = 0;
    	private int numGPS = 0;
		public void accelerometerChanged(float x, float y, float z)
				throws RemoteException {
			realAccel_.setText("X: " + x + ", Y:" + y + ", Z:" + z);
			if (m_ != 0)
				scaleAccel_.setTag("X: " + (x*m_) + ", Y:" + (y*m_) + ", Z:" + (z*m_));
		}

		public void addedWaypoint() throws RemoteException {
			numGPS++;
			numWaypoints_.setText("GPS: " + numGPS);
		}

		public void gpsChanged(double lat, double lng) throws RemoteException {
			lastGps_.setText("Lat: " + lat + ", Lng: " + lng);
		}

		public void setAccelerometerMultiplier(int multip)
				throws RemoteException {
			m_ = multip;
		}

		public void setRealSpeed(int speed) throws RemoteException {
			realSpeed_.setTag("Real: " + speed);
		}

		public void setScaleSpeed(int speed) throws RemoteException {
			scaleSpeed_.setTag("Scale: " + speed);
		}
    	
    };

}
