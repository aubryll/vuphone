package org.vuphone.wwatch.android;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class GetAcclData extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// UNCOMMENT TO TEST. NEEDS A TEXTVIEW TO PRINT TO
		//final TextView tv = (TextView) findViewById(R.id.text_view);
		final TextView tv = null;
		tv.setText("Working....");
		tv.invalidate();

		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> accel = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
		Sensor mine = accel.get(0);
		sm.registerListener(new SensorEventListener() {

			public void onAccuracyChanged(Sensor arg0, int arg1) {

			}

			public void onSensorChanged(SensorEvent e) {

				setMax(e.values);
				float[] max = getMax();
				tv.setText(e.values[0] + ", " + e.values[1] + ", "
						+ e.values[2] + "\n" + max[0] + ", " + max[1] + ", "
						+ max[2]);
				tv.invalidate();
			}

		}, mine, SensorManager.SENSOR_DELAY_UI);

	}

	private float x = 0;
	private float y = 0;
	private float z = 0;

	private void setMax(float[] values) {
		if (values[0] > x)
			x = values[0];
		if (values[1] > y)
			y = values[1];
		if (values[2] > z)
			z = values[2];
	}

	private float[] getMax() {
		float[] a = new float[3];
		a[0] = x;
		a[1] = y;
		a[2] = z;
		return a;
	}
}