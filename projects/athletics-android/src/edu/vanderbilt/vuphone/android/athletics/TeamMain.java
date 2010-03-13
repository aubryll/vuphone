package edu.vanderbilt.vuphone.android.athletics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 * @author Moses Morjain
 */
public class TeamMain extends Activity {

	@Override
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_main);

		/*
		 * String title is assigned the value of the item (name of 
		 * the sport) that the user clicked on in the 
		 * previous activity.
		 * String title cannot go before onCreate is called because of 
		 * a problem with pending Intent.
		 */
		String title = getIntent().getExtras().getString("sports_title");
		setTitle("Vanderbilt " + title);

		((Button) findViewById(R.id.Button01))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(TeamMain.this,
								AthleticsSchedule.class).putExtra(
								"sports_title", getIntent().getExtras()
										.getString("sports_title"));
						startActivity(i);

					}

				});
		((Button) findViewById(R.id.Button02))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(TeamMain.this,
								AthleticsRoster.class).putExtra("sports_title",
								getIntent().getExtras().getString(
										"sports_title"));
						startActivity(i);

					}

				});
		((Button) findViewById(R.id.Button03))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(TeamMain.this,
								AthleticsNews.class).putExtra("sports_title",
								getIntent().getExtras().getString(
										"sports_title"));
						startActivity(i);

					}

				});
		((Button) findViewById(R.id.Button04))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(TeamMain.this,
								AthleticsNews.class).putExtra("sports_title",
								getIntent().getExtras().getString(
										"sports_title"));
						startActivity(i);

					}

				});

	}

}
