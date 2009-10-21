package vuphone.vanderbilt.edu.dining;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class restaurantDetails extends Activity {
	
	
	Calendar rightNow = Calendar.getInstance();
	


	// A listener for our button
	//Listener is not necessary as of right now
	private OnClickListener listener_ = new OnClickListener() {
		public void onClick(View v) {
			Log.d("First Try", "Button was clicked!");
		}
	};

	@Override
	protected void onCreate(Bundle ice) {
		super.onCreate(ice);

		setContentView(R.layout.main);

		Bundle extras = getIntent().getExtras();
		int restaurant = extras.getInt("restaurant");

		TextView t = (TextView) findViewById(R.id.my_textview); //Outputs Name of Restaurant on Details page
		t.setText(Main.RESTAURANTS[restaurant]);	
		
		/*
		 * Currently when a user clicks on a Restaurant name, he is presented with the name of the restaurant
		 * and the hours for that restaurant on the current day that the system clock says it is.
		 * 
		 * This restaurant details page is merely a mockup because the real restaurantDetails page will
		 * be done by Austin.
		 * 
		 */
		if(rightNow.get(Calendar.DAY_OF_WEEK)==1)
		{
		TextView time = (TextView) findViewById(R.id.time);
		time.setText("It's Sunday. \nOur hours today are " + Main.SUNDAY_START[restaurant] + " - " + Main.SUNDAY_END[restaurant]);
		}
		else if(rightNow.get(Calendar.DAY_OF_WEEK)==2)
		{
			TextView time = (TextView) findViewById(R.id.time);
			time.setText("It's Monday. \nOur hours today are " + Main.MONDAY_START[restaurant] + " - " + Main.MONDAY_END[restaurant]);
		}
		else if(rightNow.get(Calendar.DAY_OF_WEEK)==3)
		{
			TextView time = (TextView) findViewById(R.id.time);
			time.setText("It's Tuesday \nOur hours today are " + Main.TUESDAY_START[restaurant] + " - " + Main.TUESDAY_END[restaurant]);
		}
		else if(rightNow.get(Calendar.DAY_OF_WEEK)==4)
		{
			TextView time = (TextView) findViewById(R.id.time);
			time.setText("It's Wednesday \nOur hours today are " + Main.WEDNESDAY_START[restaurant] + " - " + Main.WEDNESDAY_END[restaurant]);
		}
		else if(rightNow.get(Calendar.DAY_OF_WEEK)==5)
		{
			TextView time = (TextView) findViewById(R.id.time);
			time.setText("It's Thursday \nOur hours today are " + Main.THURSDAY_START[restaurant] + " - " + Main.THURSDAY_END[restaurant]);
		}
		else if(rightNow.get(Calendar.DAY_OF_WEEK)==6)
		{
			TextView time = (TextView) findViewById(R.id.time);
			time.setText("It's Friday \nOur hours today are " + Main.FRIDAY_START[restaurant] + " - " + Main.FRIDAY_END[restaurant]);
		}
		else
		{
			TextView time = (TextView) findViewById(R.id.time);
			time.setText("It's Saturday \nOur hours today are " + Main.SATURDAY_START[restaurant] + " - " + Main.SATURDAY_END[restaurant]);
		}
	}
}