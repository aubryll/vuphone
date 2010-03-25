package edu.vanderbilt.vuphone.android.athletics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import edu.vanderbilt.vuphone.android.athletics.storage.XmlToDatabaseHelper;

/*
 * The Main class has two buttons: Sports and History
 * When Sports is clicked, the user will be taken to
 * a list of Vanderbilt Sports.
 * When History is clicked, the user will be taken to
 * a list of different parts of Vanderbilt Athletic History.
 * 
 * @author Grayson Sharpe
 */

public class Main extends Activity {
	private ProgressBar mProgress;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); // Sets the layout

		ConnectivityManager connect = (ConnectivityManager) getSystemService(Main.CONNECTIVITY_SERVICE);

		XmlToDatabaseHelper loader = new XmlToDatabaseHelper(this);

		// TODO Fix isUpdated so that database doesn't have to be loaded every
		// launch
		if (connect.getNetworkInfo(0).isConnected()
				|| connect.getNetworkInfo(1).isConnected()) {
			// if (loader.isUpdated(this))
			// {
			loader.updateDatabase();
			// }
		} else {
			Context context = getApplicationContext();
			CharSequence text = "You aren't connected to the Internet. Please connect to Internet for updated data.";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2, toast
					.getYOffset() / 2);
			toast.show();
		}

		((Button) findViewById(R.main.sports_button))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(Main.this, SportsMain.class);
						startActivity(i); // Launches SportsMain Intent
					}
				});
		((Button) findViewById(R.main.history_button))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(Main.this, HistoryMain.class);
						startActivity(i); // Launches HistoryMain Intent
					}
				});
	}

	// -------------------- MENU FUNCTIONS

	private static final int MENU_ITEM_ABOUT = 0;

	/*
	 * Adds a menu button to see About Page
	 */
	/** Creates list of actions for user when the menu button is clicked */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_ITEM_ABOUT, Menu.NONE, "About").setIcon(
				getResources().getDrawable(
						android.R.drawable.ic_menu_info_details));

		return true;
	}

	/** Handles what happens when each menu item is clicked */
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_ITEM_ABOUT:
			startActivity(new Intent(this, About.class));
			break;
		}
		return true;
	}
}
