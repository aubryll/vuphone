package org.vuphone.wwatch.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SettingsUI extends Activity {

	private AlertDialog timeoutExplanation_;
	private AlertDialog batteryExplanation_;

	private OnClickListener listener_ = new OnClickListener() {
		public void onClick(View v) {
			if (v.equals(findViewById(R.id.timeout_field)))
				timeoutExplanation_.show();
			else if (v.equals(findViewById(R.id.battery_field)))
				batteryExplanation_.show();
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
		
		
	}

}
