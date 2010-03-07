package edu.vanderbilt.vuphone.android.athletics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

}
