package org.vuphone.wwatch.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The entry point into the application. This activity is responsible for
 * starting the service with any necessary configuration data and displaying any
 * visual alerts required by the service. Uses some nasty hacks to get
 * everything to work.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class TestingUI extends Activity {
	final static int LAUNCH = 0;
	final static int CONFIRM = 1;
	private int mode_ = 0;

	// All the EditText UI elements we will be interacting with
	private EditText speedScaleEdit_ = null;
	private EditText accelScaleEdit_ = null;

	// All the textView UI elements we will be interacting with
	private static TextView scaleSpeed_ = null;
	private static TextView realSpeed_ = null;
	private static TextView realAccel_ = null;
	private static TextView scaleAccel_ = null;
	private static TextView lastGps_ = null;
	private static TextView numWaypoints_ = null;

	private static TestingUI instance_ = null;

	/**
	 * OnCLickListener for the start services button
	 */
	private OnClickListener updateListener = new OnClickListener() {
		public void onClick(View v) {
			double dialation = 1.0;
			Intent gpsIntent = new Intent(TestingUI.this, GPService.class);
			try {
				dialation = Double.parseDouble(speedScaleEdit_.getText()
						.toString());
			} catch (Exception e) {
			}
			gpsIntent.putExtra("TimeDialation", dialation);

			startService(gpsIntent);

			// Update Accelerometer service
			Intent accelIntent = new Intent(TestingUI.this,
					DecelerationService.class);
			float accelScale = (float) 1.0;
			try {
				accelScale = Float.parseFloat(accelScaleEdit_.getText()
						.toString());
			} catch (Exception e) {
			}
			accelIntent.putExtra("AccelerationScaleFactor", accelScale);
			startService(accelIntent);

		}
	};

	/**
	 * OnCLickListener for the stop services button
	 */
	private OnClickListener stopListener = new OnClickListener() {
		public void onClick(View v) {
			Activity a = TestingUI.this.getParent();
			Tabs t = (Tabs) a;
			t.stopServices();

			Toast
					.makeText(
							TestingUI.this,
							"You must now re-launch the app to get updates from the sensors.",
							Toast.LENGTH_LONG).show();
			Button button = (Button) findViewById(R.id.stop_button);
			button.setEnabled(false);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance_ = this;

		Intent startIntent = super.getIntent();
		Bundle data = startIntent.getExtras();
		if (data == null)
			mode_ = TestingUI.LAUNCH;
		else
			mode_ = data.getInt("ActivityMode");

		switch (mode_) {
		case TestingUI.LAUNCH:
			// Started by the launcher
			setContentView(R.layout.main);

			// Save these guys for later
			scaleSpeed_ = (TextView) findViewById(R.id.scaled_speed);
			realSpeed_ = (TextView) findViewById(R.id.real_speed);
			realAccel_ = (TextView) findViewById(R.id.real_accel);
			scaleAccel_ = (TextView) findViewById(R.id.scale_accel);
			lastGps_ = (TextView) findViewById(R.id.last_gps);
			numWaypoints_ = (TextView) findViewById(R.id.num_gps);

			// Assign click listeners
			Button button = (Button) findViewById(R.id.update_button);
			button.setOnClickListener(updateListener);
			button = (Button) findViewById(R.id.stop_button);
			button.setOnClickListener(stopListener);

			speedScaleEdit_ = (EditText) super.findViewById(R.id.speed_scale);
			accelScaleEdit_ = (EditText) super.findViewById(R.id.accel_scale);

			break;

		case TestingUI.CONFIRM:
			// Put up a confirm dialog and return an intent
			// dialog = new ConfirmationDialog(this);
			// dialog.show();

			break;
		}
	}

	/**
	 * Comparable to a destructor, this is called when an activity is no longer
	 * needed
	 */
	protected void onDestroy() {
		super.onDestroy();
		Log.v(VUphone.tag, "Testing onDestroy reached");
		instance_ = null;
	}

	/**
	 * Used to interact with the main interface (IRegister) of the services we
	 * are interested in.
	 */
	public static ServiceConnection gpsConnection_ = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.v(VUphone.tag,
					"Testing onConnected activated, adding to GPS callbacks");
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
			Log.v(VUphone.tag, "Testing - GPS was disconnected");
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.

		}
	};

	/**
	 * Used to interact with the main interface (IRegister) of the services we
	 * are interested in.
	 */
	public static ServiceConnection accelConnection_ = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.v(VUphone.tag,
					"Testing onConnected activated, adding to Accel callbacks");
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
			Log.v(VUphone.tag, "Testing - Accel was disconnected");
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.

		}
	};

	/**
	 * Contains the implementations of the methods defined in the
	 * ISettingsViewCallback interface, which is defined to allow the GPS and
	 * accelerometer services to pass updates to this activity
	 */
	private static ISettingsViewCallback callback_ = new ISettingsViewCallback.Stub() {
		private int m_ = 1;
		private int numGPS = 0;

		public void accelerometerChanged(float x, float y, float z)
				throws RemoteException {

			if (instance_ == null)
				return;

			String realAccel = "X: " + Math.round(x * 10.0) / 10.0 + ", Y:"
					+ Math.round(y * 10.0) / 10.0 + ", Z:"
					+ Math.round(z * 10.0) / 10.0;
			Log.v(VUphone.tag, "Setting real Acceleration to " + realAccel);
			realAccel_.setText(realAccel);

			scaleAccel_.setText("X: " + Math.round(x * m_ * 10.0) / 10.0
					+ ", Y:" + Math.round(y * m_ * 10.0) / 10.0 + ", Z:"
					+ Math.round(z * m_ * 10.0) / 10.0);
		}

		public void showConfirmDialog() {
			if (instance_ == null)
				return;

			//dialog = new ConfirmationDialog(instance_);
			//dialog.show();
		}

		public void gpsChanged(double lat, double lng) throws RemoteException {
			if (instance_ == null)
				return;

			Double slat = new Double(lat);
			Double slng = new Double(lng);

			String gps = "Lat: " + slat.toString().substring(0, 6) + ", Lng: "
					+ slng.toString().substring(0, 6);
			lastGps_.setText(gps);
			Log.v(VUphone.tag, "Setting GPS to " + gps);
			numGPS++;
			numWaypoints_.setText("GPS: " + numGPS);

		}

		public void setAccelerometerMultiplier(int multip)
				throws RemoteException {
			if (instance_ == null)
				return;

			Log.v(VUphone.tag, "Setting accel multiplier to " + multip);
			m_ = multip;
		}

		public void setRealSpeed(double speed) throws RemoteException {
			if (instance_ == null)
				return;

			speed = Math.round(speed * 100.0) / 100.0;
			realSpeed_.setText("Real: " + speed);
			Log.v(VUphone.tag, "Set real speed to " + speed);
		}

		public void setScaleSpeed(double speed) throws RemoteException {
			if (instance_ == null)
				return;

			speed = Math.round(speed * 100.0) / 100.0;
			scaleSpeed_.setText("Scale: " + speed);
			Log.v(VUphone.tag, "Set scale speed to " + speed);
		}

	};

}
