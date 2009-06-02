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
			ImageView iv = (ImageView)v;
			if (iv.equals(findViewById(R.id.timeout_help_icon)))
				timeoutExplanation_.show();
			else if (iv.equals(findViewById(R.id.battery_help_icon)))
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

		ImageView iv = (ImageView) findViewById(R.id.timeout_help_icon);
		iv.setFocusable(true);
		iv.setOnClickListener(listener_);
		
		b = new AlertDialog.Builder(this);
		b.setMessage("Higher battery power = faster wreck detection");
		b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						batteryExplanation_.dismiss();
					}
				});
		batteryExplanation_ = b.create();

		iv = (ImageView) findViewById(R.id.battery_help_icon);
		iv.setFocusable(true);
		iv.setOnClickListener(listener_);
		
		
	}

}
