package org.vuphone.assassins.android.godfather;

import org.vuphone.assassins.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * This is an activity for the Godfather to use to enter a custom central
 * point for the game.  It is called by checking the 'Other' radio button
 * in ActivityGodfather.  All it does is take in the lat lon pair the user
 * enters and returns that to ActivityGodfather
 * 
 * @author Scott Campbell
 */
public class ActivityEnterCentralPoint extends Activity {

	private Intent intent_;
	private AlertDialog errorMsg_;
	
	private double latitude_;
	private double longitude_;
	
	private Button submitButton_;
	
	/**
	 * The "constructor". Sets up the fields and the layout. This is either
	 * called because somebody requested this activity to start up or because
	 * the activity is being reloaded due to a keyboard flip.
	 */
	@Override
	protected void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.enter_central_point);
		
		submitButton_ = (Button) findViewById(R.id.point_submit);
		submitButton_.setOnClickListener(submit_listener_);
	}
	
	OnClickListener submit_listener_ = new OnClickListener() {

		@Override
		public void onClick(View v) {
			EditText latTxt = (EditText) findViewById(R.id.point_entered_lat);
			EditText lonTxt = (EditText) findViewById(R.id.point_entered_lon);
			latitude_ = Double.parseDouble(latTxt.getText().toString());
			longitude_ = Double.parseDouble(lonTxt.getText().toString());
			if (latitude_ == 0.0 || longitude_ == 0.0) {
				Builder b = new AlertDialog.Builder(ActivityEnterCentralPoint.this);
				b.setMessage("The number of seconds to wait for"
						+ " a response before assuming a crash has occurred");
				b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						errorMsg_.dismiss();
					}
				});
				errorMsg_ = b.create();
				errorMsg_.show();
				return;
			}
			intent_ = new Intent();
			intent_.putExtra("Latitude", latitude_);
			intent_.putExtra("Longitude", longitude_);
			setResult(RESULT_OK, intent_);
			finish();
			
		}
		
	};
}
