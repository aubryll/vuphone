package edu.vanderbilt.vuphone.android.athletics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		((Button) findViewById(R.main.sports_button))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(Main.this, SportsMain.class);
						startActivity(i);
					}
				});
		((Button) findViewById(R.main.history_button))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(Main.this, HistoryMain.class);
						startActivity(i);
					}
				});
	}


	// -------------------- MENU FUNCTIONS

	private static final int MENU_ITEM_ABOUT = 0;

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
