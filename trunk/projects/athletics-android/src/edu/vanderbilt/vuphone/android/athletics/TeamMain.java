package edu.vanderbilt.vuphone.android.athletics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TeamMain extends Activity {

	@Override
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_main);
		((Button) findViewById(R.id.Button01))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(TeamMain.this,
								AthleticsSchedule.class);
						startActivity(i);

					}

				});
		((Button) findViewById(R.id.Button02))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(TeamMain.this,
								AthleticsRoster.class);
						startActivity(i);

					}

				});
		((Button) findViewById(R.id.Button03))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(TeamMain.this,
								AthleticsNews.class);
						startActivity(i);

					}

				});
		((Button) findViewById(R.id.Button04))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(TeamMain.this,
								AthleticsNews.class);
						startActivity(i);

					}

				});

	}

}
