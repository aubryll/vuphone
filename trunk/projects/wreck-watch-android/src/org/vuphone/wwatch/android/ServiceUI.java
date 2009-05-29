package org.vuphone.wwatch.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The entry point into the application. This activity is responsible for
 * starting the service with any necessary configuration data and displaying any
 * visual alerts required by the service. Uses some nasty hacks to get
 * everything to work.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class ServiceUI extends Activity {

	final static int LAUNCH = 0;
	final static int CONFIRM = 1;
	private int mode_ = 0;

	private EditText edit_ = null;
	private EditText accelScaleEdit_ = null;

	private TextView scaleSpeed_ = null;
	private TextView realSpeed_ = null;
	private TextView realAccel_ = null;
	private TextView scaleAccel_ = null;
	private TextView lastGps_ = null;
	private TextView numWaypoints_ = null;

	private ConfirmationDialog dialog = null;

	private OnClickListener startListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(ServiceUI.this, WreckWatchService.class);
			double dialation = 1.0;
			float accelScale = (float)1.0;
			
			if (!edit_.getText().toString().equals("")){
				try {
					dialation = Double.parseDouble(edit_.getText().toString());
				} catch (Exception e) {
				}
			}

			if (!accelScaleEdit_.getText().toString().equals("")){
				try {
					accelScale = Float.parseFloat(accelScaleEdit_.getText()
							.toString());
				} catch (Exception e) {
				}
			}
			

			intent.putExtra("TimeDialation", dialation);
			intent.putExtra("AccelerationScaleFactor", accelScale);
			
			startService(intent);
			bindService(intent, connection_, BIND_AUTO_CREATE);
		}
	};

	private OnClickListener stopListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(ServiceUI.this, WreckWatchService.class);
			stopService(intent);
			unbindService(connection_);
			
			intent = new Intent(ServiceUI.this, DecelerationCheckService.class);
			stopService(intent);

		}
	};

	private OnClickListener testListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent2 = new Intent(ServiceUI.this,
					org.vuphone.wwatch.android.ServiceUI.class);

			intent2.putExtra("ActivityMode", ServiceUI.CONFIRM);
			startActivity(intent2);

		}
	};

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
			// Started by the launcher
			setContentView(R.layout.main);

			// Save these guys for later
			scaleSpeed_ = (TextView) findViewById(R.id.scale_speed);
			realSpeed_ = (TextView) findViewById(R.id.real_speed);
			realAccel_ = (TextView) findViewById(R.id.real_accel);
			scaleAccel_ = (TextView) findViewById(R.id.scale_accel);
			lastGps_ = (TextView) findViewById(R.id.last_gps);
			numWaypoints_ = (TextView) findViewById(R.id.num_gps);
			
			// Assign click listeners
			Button button = (Button) findViewById(R.id.start_button);
			button.setOnClickListener(startListener);
			button = (Button) findViewById(R.id.stop_button);
			button.setOnClickListener(stopListener);
			button = (Button) findViewById(R.id.test_button);
			button.setOnClickListener(testListener);

			edit_ = (EditText) super.findViewById(R.id.dialation_edit);
			accelScaleEdit_ = (EditText) super.findViewById(R.id.accel_scale);

			break;

		case ServiceUI.CONFIRM:
			// Put up a confirm dialog and return an intent
			dialog = new ConfirmationDialog(this);
			dialog.show();
			break;
		}

		// //Testing for Poster

		// HTTPPoster.doAccidentPost(Calendar.getInstance().getTimeInMillis(),
		// 500.23, -41.2, null);
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

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection connection_ = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been
			// established, giving us the service object we can use to
			// interact with the service. We are communicating with our
			// service through an IDL interface, so get a client-side
			// representation of that from the raw service object.
			IRegister mService = IRegister.Stub.asInterface(service);

			// We want to monitor the service for as long as we are
			// connected to it.
			try {
				mService.registerCallback(callback_);
			} catch (RemoteException e) {
				// In this case the service has crashed before we could even
				// do anything with it; we can count on soon being
				// disconnected (and then reconnected if it can be restarted)
				// so there is no need to do anything here.
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
		}
	};

	private ISettingsViewCallback callback_ = new ISettingsViewCallback.Stub() {
		private int m_ = 0;
		private int numGPS = 0;

		public void accelerometerChanged(float x, float y, float z)
				throws RemoteException {
			realAccel_.setText("X: " + x + ", Y:" + y + ", Z:" + z);
			if (m_ != 0)
				scaleAccel_.setTag("X: " + (x * m_) + ", Y:" + (y * m_)
						+ ", Z:" + (z * m_));
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
			realSpeed_.setText("Real: " + speed);
		}

		public void setScaleSpeed(int speed) throws RemoteException {
			scaleSpeed_.setText("Scale: " + speed);
		}

	};

}
