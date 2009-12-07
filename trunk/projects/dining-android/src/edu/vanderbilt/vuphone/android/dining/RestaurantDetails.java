package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.vanderbilt.vuphone.android.map.OneLocation;
import edu.vanderbilt.vuphone.android.objects.Range;
import edu.vanderbilt.vuphone.android.objects.Restaurant;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;
import edu.vanderbilt.vuphone.android.storage.DBAdapter;

public class RestaurantDetails extends Activity {
	
	public static final String RESTAURANT_ID = "0";

	private long restaurantID;
	private Calendar rightNow;
	private Restaurant restaurant;

	private OnClickListener mapListener = new OnClickListener() {
		public void onClick(View v) {
			if (Main.DEBUG)
				Log.i("Dining", "Map button for restaurant "
						+ restaurant.getName() + " clicked.");
			Intent startMapView = new Intent(RestaurantDetails.this,
					OneLocation.class);
			int longitude = restaurant.getLon();
			int latitude = restaurant.getLat();
			String location = restaurant.getName();
			startMapView.putExtra(OneLocation.EXTRA_LONGITUDES, longitude);
			startMapView.putExtra(OneLocation.EXTRA_LATITUDES, latitude);
			startMapView.putExtra(OneLocation.EXTRA_LOCATIONS, location);
			startActivity(startMapView);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Main.DEBUG)
			Log.i("Dining", "Restaurant details Activity created");

		setContentView(R.layout.restaurant_details);

		rightNow = Calendar.getInstance();
		restaurantID = getIntent().getExtras().getLong(RESTAURANT_ID);
		
		DBAdapter adapter = new DBAdapter(this);
		adapter.openReadable();
		
		restaurant = adapter.fetchRestaurant(restaurantID);

		SpannableString title = new SpannableString(
				restaurant.getName());
		title.setSpan(new RelativeSizeSpan((float) 2.2), 0, title.length(), 0);
		((TextView) findViewById(R.restaurantDetailsPage.restaurantName))
				.setText(title);
		((TextView) findViewById(R.restaurantDetailsPage.restaurantHours))
				.setText(WeeklySchedule());

		((Button) findViewById(R.restaurantDetailsPage.to_map_button))
				.setOnClickListener(mapListener);

	}

	// TODO place holder functions below

	CharSequence WeeklySchedule() {
		SpannableStringBuilder out = new SpannableStringBuilder();
		out.append(restaurant.getHours().toString());

/*
 * 
		RestaurantHours hours = restaurant.getHours();
		
		
		
		for (int i = 0; i < hours.getMondayRangeCount(); i++)
		{
			ArrayList<Range> ranges = hours.getMondayRanges();
			Range r = ranges.get(i);
			out.append(Integer.toString(r.getStart().getHour()));
			out.append(":");
			out.append(Integer.toString(r.getStart().getMinute()));
			out.append(" TO ");
			out.append(Integer.toString(r.getEnd().getHour()));
			out.append(":");
			out.append(Integer.toString(r.getEnd().getMinute()));
			out.append("\n");
		}
		
		for (int i = 0; i < hours.getTuesdayRangeCount(); i++)
		{
			ArrayList<Range> ranges = hours.getMondayRanges();
			Range r = ranges.get(i);
			out.append(Integer.toString(r.getStart().getHour()));
			out.append(":");
			out.append(Integer.toString(r.getStart().getMinute()));
			out.append(" TO ");
			out.append(Integer.toString(r.getEnd().getHour()));
			out.append(":");
			out.append(Integer.toString(r.getEnd().getMinute()));
			out.append("\n");
		}
		*/
//		out.append("S\t" + Main.SUNDAY_START[restaurant] + " - "
//				+ Main.SUNDAY_END[restaurant] + '\n' + "M\t"
//				+ Main.MONDAY_START[restaurant] + " - "
//				+ Main.MONDAY_END[restaurant] + '\n' + "T\t"
//				+ Main.TUESDAY_START[restaurant] + " - "
//				+ Main.TUESDAY_END[restaurant] + '\n' + "W\t"
//				+ Main.WEDNESDAY_START[restaurant] + " - "
//				+ Main.WEDNESDAY_END[restaurant] + '\n' + "T\t"
//				+ Main.THURSDAY_START[restaurant] + " - "
//				+ Main.THURSDAY_END[restaurant] + '\n' + "F\t"
//				+ Main.FRIDAY_START[restaurant] + " - "
//				+ Main.FRIDAY_END[restaurant] + '\n' + "S\t"
//				+ Main.SATURDAY_START[restaurant] + " - "
//				+ Main.SATURDAY_END[restaurant] + '\n');

//		int i = 0;
//		for (int day = 1; day <= 7; day++) {
//
//			int next = out.toString().indexOf('\n', i);
//			next = (next != -1 ? next : out.length());
//
//			if (day == rightNow.get(Calendar.DAY_OF_WEEK))
//				// trying to find way to do bold but had to settle for this
//				out.setSpan(new RelativeSizeSpan((float) 1.6), i, next, 0);
//			i = next + 1;
//		}

		return out;
	}
}