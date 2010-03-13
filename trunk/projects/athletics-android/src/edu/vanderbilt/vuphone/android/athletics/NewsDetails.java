package edu.vanderbilt.vuphone.android.athletics;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * Activity is launched when user clicks on a 
 * news article in the list on the previous page.
 * 
 * May scrap entire activity and launch browser view
 * onClick in previous activity
 * 
 * @author Grayson Sharpe
 */
public class NewsDetails extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_details);

		String date = getIntent().getExtras().getString("news_date");
		((TextView) findViewById(R.news.detailsDate)).setText(date);

		String title = getIntent().getExtras().getString("news_title");
		((TextView) findViewById(R.news.detailsTitle)).setText(title);

		((Button) findViewById(R.news.detailsButton))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse("http://vucommodores.cstv.com/")));
					}
				});
	}
}
