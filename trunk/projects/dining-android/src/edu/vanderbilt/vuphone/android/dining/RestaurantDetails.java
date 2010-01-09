package edu.vanderbilt.vuphone.android.dining;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.maps.MapActivity;

import edu.vanderbilt.vuphone.android.map.OneLocation;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;
import edu.vanderbilt.vuphone.android.storage.Restaurant;

public class RestaurantDetails extends MapActivity implements ViewSwitcher.ViewFactory,
View.OnClickListener{

	public static final String RESTAURANT_ID = "0";

	private long restaurantID;
	private Calendar rightNow;
	private Restaurant restaurant;
	private long restaurantLogo;
	private TextSwitcher mSwitcher;

    private int mCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_details);
		
		rightNow = new GregorianCalendar();
		restaurantID = getIntent().getExtras().getLong(RESTAURANT_ID);
		restaurant = Restaurant.get(restaurantID);
		setTitle(restaurant.getName());
		
		restaurantLogo = Restaurant.getIcon(restaurantID);

		SpannableString title = new SpannableString(restaurant.getName());
		title.setSpan(new RelativeSizeSpan((float) 2.2), 0, title.length(), 0);
		((ImageView)findViewById(R.restaurantDetailsPage.restaurantLogo)).setImageResource((int) restaurantLogo);

		mSwitcher = (TextSwitcher) findViewById(R.restaurantDetailsPage.switcher);
        mSwitcher.setFactory(this);

        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);

        ImageButton nextButton = (ImageButton) findViewById(R.restaurantDetailsPage.next);
        nextButton.setOnClickListener(this);

        updateCounter();
        
        ((OneLocation) findViewById(R.restaurantDetailsPage.map)).setRestaurant(restaurantID);
	}

	// TODO place holder functions below
	CharSequence DaySchedule()
	{
		RestaurantHours hours = Restaurant.getHours(restaurantID);
		SpannableStringBuilder out = new SpannableStringBuilder();
		if(mCounter>Calendar.SATURDAY)
		{
			mCounter=Calendar.SUNDAY;
		}
		switch (mCounter) {
			case Calendar.SUNDAY:
				out.append((CharSequence) hours.getSundayRanges().toString());
				break;
			case Calendar.MONDAY:
				out.append((CharSequence) hours.getMondayRanges().toString());
				break;
			case Calendar.TUESDAY:
				out.append((CharSequence) hours.getTuesdayRanges().toString());
				break;
			case Calendar.WEDNESDAY:
				out.append((CharSequence) hours.getWednesdayRanges().toString());
				break;
			case Calendar.THURSDAY:
				out.append((CharSequence) hours.getThursdayRanges().toString());
				break;
			case Calendar.FRIDAY:
				out.append((CharSequence) hours.getFridayRanges().toString());
				break;
			case Calendar.SATURDAY:
				out.append((CharSequence) hours.getSaturdayRanges().toString());
				break;
			}
		return out;
		}
	
	
	CharSequence WeeklySchedule() {
		SpannableStringBuilder out = new SpannableStringBuilder();
		out.append(restaurant.getHours().toString());

		/*
		 * 
		 * RestaurantHours hours = restaurant.getHours();
		 * 
		 * 
		 * 
		 * for (int i = 0; i < hours.getMondayRangeCount(); i++) {
		 * ArrayList<Range> ranges = hours.getMondayRanges(); Range r =
		 * ranges.get(i); out.append(Integer.toString(r.getStart().getHour()));
		 * out.append(":");
		 * out.append(Integer.toString(r.getStart().getMinute()));
		 * out.append(" TO ");
		 * out.append(Integer.toString(r.getEnd().getHour())); out.append(":");
		 * out.append(Integer.toString(r.getEnd().getMinute()));
		 * out.append("\n"); }
		 * 
		 * for (int i = 0; i < hours.getTuesdayRangeCount(); i++) {
		 * ArrayList<Range> ranges = hours.getMondayRanges(); Range r =
		 * ranges.get(i); out.append(Integer.toString(r.getStart().getHour()));
		 * out.append(":");
		 * out.append(Integer.toString(r.getStart().getMinute()));
		 * out.append(" TO ");
		 * out.append(Integer.toString(r.getEnd().getHour())); out.append(":");
		 * out.append(Integer.toString(r.getEnd().getMinute()));
		 * out.append("\n"); }
		 */
		// out.append("S\t" + Main.SUNDAY_START[restaurant] + " - "
		// + Main.SUNDAY_END[restaurant] + '\n' + "M\t"
		// + Main.MONDAY_START[restaurant] + " - "
		// + Main.MONDAY_END[restaurant] + '\n' + "T\t"
		// + Main.TUESDAY_START[restaurant] + " - "
		// + Main.TUESDAY_END[restaurant] + '\n' + "W\t"
		// + Main.WEDNESDAY_START[restaurant] + " - "
		// + Main.WEDNESDAY_END[restaurant] + '\n' + "T\t"
		// + Main.THURSDAY_START[restaurant] + " - "
		// + Main.THURSDAY_END[restaurant] + '\n' + "F\t"
		// + Main.FRIDAY_START[restaurant] + " - "
		// + Main.FRIDAY_END[restaurant] + '\n' + "S\t"
		// + Main.SATURDAY_START[restaurant] + " - "
		// + Main.SATURDAY_END[restaurant] + '\n');

		// int i = 0;
		// for (int day = 1; day <= 7; day++) {
		//
		// int next = out.toString().indexOf('\n', i);
		// next = (next != -1 ? next : out.length());
		//
		// if (day == rightNow.get(Calendar.DAY_OF_WEEK))
		// // trying to find way to do bold but had to settle for this
		// out.setSpan(new RelativeSizeSpan((float) 1.6), i, next, 0);
		// i = next + 1;
		// }

		return out;
	}

    public void onClick(View v) {
        mCounter++;
        updateCounter();
    }

    private void updateCounter() {
        mSwitcher.setText(DaySchedule());
    }

    public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(10);
        return t;
    }

	/* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}