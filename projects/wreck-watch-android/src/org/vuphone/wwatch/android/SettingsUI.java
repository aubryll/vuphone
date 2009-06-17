package org.vuphone.wwatch.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class SettingsUI extends Activity {

	private AlertDialog timeoutExplanation_;
	private AlertDialog batteryExplanation_;
	private AlertDialog locationExplanation_;

	private OnClickListener listener_ = new OnClickListener() {
		public void onClick(View v) {
			if (v.equals(findViewById(R.id.timeout_field)))
				timeoutExplanation_.show();
			else if (v.equals(findViewById(R.id.battery_field)))
				batteryExplanation_.show();
			else if (v.equals(findViewById(R.id.location_field)))
				locationExplanation_.show();
			else if (v.equals(findViewById(R.id.save_button)))
				savePreferences();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		Builder b = new AlertDialog.Builder(this);
		b.setMessage("The number of seconds to wait for"
				+ " a response before assuming a crash has occurred");
		b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				timeoutExplanation_.dismiss();
			}
		});
		timeoutExplanation_ = b.create();

		View field = findViewById(R.id.timeout_field);
		field.setFocusable(true);
		field.setOnClickListener(listener_);

		b = new AlertDialog.Builder(this);
		b.setMessage("The default location to zoom in on. City, State");
		b.setPositiveButton("Ok", null);
		locationExplanation_ = b.create();
		
		field = findViewById(R.id.location_field);
		field.setFocusable(true);
		field.setOnClickListener(listener_);
		
		b = new AlertDialog.Builder(this);
		b.setMessage("Higher battery power = faster wreck detection");
		b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				batteryExplanation_.dismiss();
			}
		});
		batteryExplanation_ = b.create();

		field = findViewById(R.id.battery_field);
		field.setFocusable(true);
		field.setOnClickListener(listener_);

		((Button) super.findViewById(R.id.save_button))
				.setOnClickListener(listener_);

		((EditText) super.findViewById(R.id.timeout)).setText(""
				+ super.getSharedPreferences(VUphone.PREFERENCES_FILE,
						Context.MODE_PRIVATE).getInt(VUphone.TIMEOUT_TAG, 10));

		((EditText) super.findViewById(R.id.location)).setText(""
				+ super.getSharedPreferences(VUphone.PREFERENCES_FILE,
						Context.MODE_PRIVATE).getString(VUphone.LOCATION_TAG, "Nashville, TN"));
		
		((EditText) super.findViewById(R.id.server)).setText(""
				+ super.getSharedPreferences(VUphone.PREFERENCES_FILE,
						Context.MODE_PRIVATE).getString(VUphone.SERVER_TAG, "0.0.0.0:8080"));		

	}

	private void savePreferences() {
		SharedPreferences prefs = super.getSharedPreferences(
				VUphone.PREFERENCES_FILE, Context.MODE_PRIVATE);

		int time = Integer.parseInt(((EditText) super
				.findViewById(R.id.timeout)).getText().toString());
		int level = (int) ((RatingBar) super.findViewById(R.id.battery_level))
				.getRating();		
		String location = ((EditText) super.findViewById(R.id.location)).getText().toString();
		
		String server = ((EditText) super.findViewById(R.id.server)).getText().toString();

		Editor edit = prefs.edit();
		edit.putInt(VUphone.TIMEOUT_TAG, time);
		edit.putInt(VUphone.BATTERY_LEVEL_TAG, level);
		edit.putString(VUphone.LOCATION_TAG, location);
		edit.putString(VUphone.SERVER_TAG, server);
		edit.commit();
	}
}
