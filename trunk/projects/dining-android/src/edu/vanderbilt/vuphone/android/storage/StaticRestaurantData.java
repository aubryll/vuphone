package edu.vanderbilt.vuphone.android.storage;

import java.util.Calendar;
import java.util.Random;

import edu.vanderbilt.vuphone.android.dining.R;
import edu.vanderbilt.vuphone.android.objects.Range;
import edu.vanderbilt.vuphone.android.objects.RestaurantHours;
import edu.vanderbilt.vuphone.android.objects.Time;

public class StaticRestaurantData {

	int maxMenuItems = 10;

	String[] letters = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "l",
			"m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
			"z" };
	Random r = new Random();

	public void createAllRestaurants() {
		/*
		 * On Campus Restaurants
		 */
		randData();
		theCommonsFoodGalleryData();
		commonGroundsData();
		chefJamesData();
		centerSmoothieData();
		pubData();
		ctWestData();
		quiznosTowersData();
		quiznosMorganData();
		roTikiData();
		starbucksData();
		grinsData();
		suziesCafeEngData();
		suziesCafeBlairData();
		suziesCafeDivinityData();
		nectarData();
		mcTyeireData();
		varsityBranscombData();
		varsityTowersData();
		varsityMorganData();
		varsitySarrattData();
		hemingwayMarketData();
		/*
		 * Taste of Nashville Restaurants
		 */
		bestWokData();
		breadAndCoData();
		brueggersBagelData();
		cabanaData();
		cafeCocoData();
		cheeseburgerCharleysData();
		chilisData();
		medCuisineData();
		michaelangelosPizzaData();
		noshvilleData();
		obiesPizzaData();
		papaJohnsData();
		qdobaData();
		rolyPolyData();
		romaPizzaData();
		samsSportsBarData();
		satayThaiData();
		schlotzskysData();
		smoothieKingTwentyFirstData();
		smoothieKingEllistonData();
		sunsetGrillData();
		tgiFridaysData();
		wendysTwentyFirstData();
		wendysWestEndData();
		yogurtOasisData();
	}

	/*
	 * On Campus Restaurants
	 */
	private void randData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(10, 0);
		Time stop = new Time(14, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));
		start = new Time(11, 0);
		stop = new Time(14, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));
		start = new Time(11, 0);
		stop = new Time(14, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));
		start = new Time(16, 30);
		stop = new Time(19, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));
		start = new Time(11, 0);
		stop = new Time(14, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));
		start = new Time(11, 0);
		stop = new Time(14, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));
		start = new Time(16, 30);
		stop = new Time(19, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));
		start = new Time(11, 0);
		stop = new Time(14, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(14, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "Rand Dining Center is the focal point of the campus community. Its central location, great food, and comfortable atmosphere provide students, faculty, and staff an opportunity to gather, socialize, and interact. Rand retail offers a varity of foods such as baked goods, made-for-you or build-your own salads, and regional favorites like Mediterranean and Asian foods.";

		Restaurant rand = new Restaurant("Rand Dining Center", rh, true,
				(int) (36.146405 * 1E6), (int) (-86.803178 * 1E6), "Cafeteria",
				null, description, R.drawable.rand, true, true, false, null,
				"http://www.vanderbilt.edu/dining/rest_rand.php");
		rand.create();
	}

	private void theCommonsFoodGalleryData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(11, 0);
		Time stop = new Time(20, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(20, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(20, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(20, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(20, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "The Commons Center is the dining facility and community square of The Commons. The state-of-the-art dining facility includes a salad bar with sizzle station, Chef�s Table, specialty pizza oven, deli, wok, grill, and vegan/vegetarian food.";

		Restaurant commonsFood = new Restaurant("The Commons Food Gallery", rh,
				true, (int) (36.141951 * 1E6), (int) (-86.797127 * 1E6),
				"Cafeteria", null, description, R.drawable.commons, true, true,
				false, null,
				"http://www.vanderbilt.edu/dining/rest_commons.php");
		commonsFood.create();
	}

	private void commonGroundsData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(7, 0);
		Time stop = new Time(7, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "Common Grounds 24-hour coffee shop in The Commons Center offers grab-n-go meals, beverages and sundries. Particular emphasis on using biodegradable/compostable products and only serves 100% organic/fair trade coffee.";

		Restaurant commonGrounds = new Restaurant("The Common Grounds", rh,
				false, (int) (36.141771 * 1E6), (int) (-86.79694 * 1E6),
				"Munchie Mart", null, description, R.drawable.commongrounds,
				true, true, false, null,
				"http://www.vanderbilt.edu/dining/commongrounds.php");
		commonGrounds.create();

	}

	private void chefJamesData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(16, 0);
		Time stop = new Time(19, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(19, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(19, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(19, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(19, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(15, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(16, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "Chef James Bistro serves hot gourmet entrees and sides, soups, coffee, gourmet grab-n-go salads, sandwiches, entrees and sides.";

		Restaurant chefJames = new Restaurant("Chef James Bistro", rh, false,
				(int) (36.146321 * 1E6), (int) (-86.803037 * 1E6), "Cafe",
				null, description, R.drawable.chefjamesbistro, true, true,
				false, null,
				"http://www.vanderbilt.edu/dining/line_chefjamesbistro.php");
		chefJames.create();
	}

	private void centerSmoothieData() {
		RestaurantHours rh = new RestaurantHours();

		// Center Smoothie is closed on Saturday and Sunday
		Time start = new Time(7, 0);
		Time stop = new Time(21, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(15, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "Gourmet smoothie shop offering grab-n-go meal plan options.";

		Restaurant centerSmoothie = new Restaurant("Center Smoothie", rh,
				false, (int) (36.146545 * 1E6), (int) (-86.803471 * 1E6),
				"Smoothie", null, description, R.drawable.centersmoothie, true,
				true, false, null,
				"http://www.vanderbilt.edu/dining/line_centersmoothie.php");
		centerSmoothie.create();
	}

	private void pubData() {
		RestaurantHours rh = new RestaurantHours();
		// The Pub is closed on Saturday and Sunday

		Time start = new Time(11, 0);
		Time stop = new Time(21, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "Casual dining restaurant serving al-a-carte lunch (burgers, paninis, salads, sandwiches, chicken fingers) and themed dinners (Mexican, BBQ, Italian, etc). The Pub is a perfect place to watch a game on its state-of-the-art TV/sound system.";

		Restaurant thePub = new Restaurant("The Pub at Overcup Oak", rh, false,
				(int) (36.146626 * 1E6), (int) (-86.803736 * 1E6),
				"Sports Bar", null, description, R.drawable.pub, true, true,
				false, null, "http://www.vanderbilt.edu/dining/line_pub.php");
		thePub.create();
	}

	private void ctWestData() {
		RestaurantHours rh = new RestaurantHours();
		// C.T. West is closed on Friday and Saturday
		
		Time start = new Time(11, 0);
		Time stop = new Time(21, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		String description = "Cowboy-themed casual BBQ restaurant in Carmichael West serving BBQ, burgers, chili, and much more!";

		Restaurant ctWest = new Restaurant("C.T. West", rh, false,
				(int) (36.147418 * 1E6), (int) (-86.806839 * 1E6), "BBQ", null,
				description, R.drawable.ctwest, true, true, false, null,
				"http://www.vanderbilt.edu/dining/line_ctwest.php");
		ctWest.create();
	}

	private void quiznosTowersData() {
		RestaurantHours rh = new RestaurantHours();
		// TODO Implement a way to show that a restaurant is open beyond the
		// current day, on Friday and Saturday, Quiznos is open til 4:00 AM and
		// Sunday through Thursday
		// is open til 12:30 AM the next day.

		Time start = new Time(11, 0);
		Time stop = new Time(0, 30);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(4, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(4, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "Located in Carmichael Towers and Morgan Hall, Quiznos delivers uncompromising quality for those looking for a tasty, fresher alternative to traditional fast food restaurants. Serving sandwiches, soup, and salads.";

		Restaurant quiznosTowers = new Restaurant("Quiznos Sub - Towers", rh,
				false, (int) (36.147461 * 1E6), (int) (-86.806705 * 1E6),
				"Sandwich", null, description, R.drawable.quiznos, true, true,
				false, null,
				"http://www.vanderbilt.edu/dining/line_quiznos_towers.php");
		quiznosTowers.create();
	}

	private void quiznosMorganData() {
		RestaurantHours rh = new RestaurantHours();
		// TODO Check Morgan Quiznos hours after break because hours are
		// currently not clear
		Time start = new Time(11, 0);
		Time stop = new Time(22, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "Located in Carmichael Towers and Morgan Hall, Quiznos delivers uncompromising quality for those looking for a tasty, fresher alternative to traditional fast food restaurants. Serving sandwiches, soup, and salads.";

		Restaurant quiznosMorgan = new Restaurant("Quiznos Sub - Morgan", rh,
				false, (int) (36.140584 * 1E6), (int) (-86.806286 * 1E6),
				"Sandwich", null, description, R.drawable.quiznos, true, true,
				false, null,
				"http://www.vanderbilt.edu/dining/line_quiznos_morgan.php");
		quiznosMorgan.create();
	}

	private void roTikiData() {
		RestaurantHours rh = new RestaurantHours();

		// Need to show that restaurant is closed all day on Saturday and Sunday
		Time start = new Time(11, 0);
		Time stop = new Time(22, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(15, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "Island-themed quick-service restaurant serving chef-crafted wraps, paninis, pizza, sandwiches, and salads.";

		Restaurant roTiki = new Restaurant("Ro*Tiki", rh, false,
				(int) (36.144847 * 1E6), (int) (-86.805728 * 1E6),
				"Cafe/Munchie Mart", null, description, R.drawable.rotiki,
				true, true, false, null,
				"http://www.vanderbilt.edu/dining/line_rotiki.php");
		roTiki.create();
	}

	private void starbucksData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(12, 0);
		Time stop = new Time(23, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(15, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "Starbucks takes the Commodore Card (Meal Money and Commodore Cash) and serves pastries and beverages Sunday through Friday.";

		Restaurant starbucks = new Restaurant("Starbucks", rh, false,
				(int) (36.144368 * 1E6), (int) (-86.805529 * 1E6), "Coffee",
				null, description, R.drawable.starbucks, true, true, false,
				null, "http://www.vanderbilt.edu/dining/line_starbucks.php");
		starbucks.create();
	}

	private void grinsData() {
		RestaurantHours rh = new RestaurantHours();
		// Restaurant is closed all day on Saturday and Sunday

		Time start = new Time(11, 0);
		Time stop = new Time(21, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(15, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "Grins Vegetarian Cafe in the Schulman Center offers wraps, salads, paninis, and baked goods with a Kosher-certified kitchen.";

		Restaurant grins = new Restaurant("Grins - Vegetarian Cafe", rh, false,
				(int) (36.144785 * 1E6), (int) (-86.806399 * 1E6),
				"Vegetarian", null, description, R.drawable.grins, true, true,
				false, null, "http://www.vanderbilt.edu/dining/line_grins.php");
		grins.create();
	}

	private void suziesCafeEngData() {
		RestaurantHours rh = new RestaurantHours();
		// Restaurant is closed all day on Saturday and Sunday

		Time start = new Time(7, 30);
		Time stop = new Time(14, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "All three locations offer an assortment of delicious sandwiches, bagels, muffins, wraps, coffee and many other treats. Suzie's takes cash, Commodore Card and Meal Plan at Blair and Featheringill and takes cash, Meal Plan, Commodore Card, and Visa and Mastercard at the Divinity School.";

		Restaurant suziesCafeEng = new Restaurant("Suzie's Cafe - Engineering",
				rh, false, (int) (36.144801 * 1E6), (int) (-86.802836 * 1E6),
				"Cafe", null, description, R.drawable.suzies, true, true,
				false, null,
				"http://www.vanderbilt.edu/dining/line_suziescafe.php");
		suziesCafeEng.create();
	}

	private void suziesCafeBlairData() {
		RestaurantHours rh = new RestaurantHours();
		
		// Restaurant is closed all day on Saturday and Sunday
		Time start = new Time(7, 30);
		Time stop = new Time(14, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "All three locations offer an assortment of delicious sandwiches, bagels, muffins, wraps, coffee and many other treats. Suzie's takes cash, Commodore Card and Meal Plan at Blair and Featheringill and takes cash, Meal Plan, Commodore Card, and Visa and Mastercard at the Divinity School.";

		Restaurant suziesCafeBlair = new Restaurant("Suzie's Cafe - Blair", rh,
				false, (int) (36.13863 * 1E6), (int) (-86.805874 * 1E6),
				"Cafe", null, description, R.drawable.suzies, true, true,
				false, null,
				"http://www.vanderbilt.edu/dining/line_suziescafe.php");
		suziesCafeBlair.create();
	}

	private void suziesCafeDivinityData() {
		RestaurantHours rh = new RestaurantHours();
		
		// Restaurant is closed all day on Saturday and Sunday
		Time start = new Time(8, 30);
		Time stop = new Time(14, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(8, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(8, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(8, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(8, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "All three locations offer an assortment of delicious sandwiches, bagels, muffins, wraps, coffee and many other treats. Suzie's takes cash, Commodore Card and Meal Plan at Blair and Featheringill and takes cash, Meal Plan, Commodore Card, and Visa and Mastercard at the Divinity School.";

		Restaurant suziesCafeDiv = new Restaurant("Suzie's Cafe - Divinity",
				rh, false, (int) (36.14654 * 1E6), (int) (-86.800662 * 1E6),
				"Cafe", null, description, R.drawable.suzies, true, true,
				false, null,
				"http://www.vanderbilt.edu/dining/line_suziescafe.php");
		suziesCafeDiv.create();
	}

	private void nectarData() {
		RestaurantHours rh = new RestaurantHours();
		
		// Restaurant is closed all day on Saturday and Sunday
		Time start = new Time(10, 0);
		Time stop = new Time(19, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "This campus natural food store offers alternative natural, organic and gourmet products including grab & go, beverages, snacks, frozen meals, specialty food and whole body care. Located on the first floor of Barnard.";

		Restaurant nectar = new Restaurant("Nectar", rh, false,
				(int) (36.148782 * 1E6), (int) (-86.803473 * 1E6),
				"Organic Munchie Mart", null, description, R.drawable.nectar,
				true, true, false, null,
				"http://www.vanderbilt.edu/dining/nectar.php");
		nectar.create();
	}

	private void mcTyeireData() {
		RestaurantHours rh = new RestaurantHours();
		// TODO Add icon and description for restaurant
		// Restaurant is closed all day on Saturday and Sunday

		Time start = new Time(7, 0);
		Time stop = new Time(10, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));
		start = new Time(18, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));
		start = new Time(18, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));
		start = new Time(18, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));
		start = new Time(18, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(10, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "";

		Restaurant mcTyeire = new Restaurant("McTyeire", rh, false,
				(int) (36.143796 * 1E6), (int) (-86.803227 * 1E6), "onCampus",
				null, description, R.drawable.dining, true, true, false, null,
				null);
		mcTyeire.create();
	}

	private void varsityBranscombData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(7, 0);
		Time stop = new Time(7, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "Open 24 hours! Featuring grab-n-go snacks, microwave meals, salads, fresh fruits, sandwiches, plus Bongo Java coffees, Muffin Tin baked goods, tons of groceries, and more! Plus, Varsity Marketplace is home to Ro*Tiki, our island-themed quick-service restaurant offering chef-crafted soups, salads, wraps, sandwiches, and pizza. And Varsity Marketplace is a VU Meal Plan location.";

		Restaurant varsityBranscomb = new Restaurant(
				"Varsity Market - Branscomb", rh, false,
				(int) (36.144929 * 1E6), (int) (-86.805791 * 1E6),
				"Munchie Mart", null, description, R.drawable.marketplace,
				true, true, false, null,
				"http://www.vanderbilt.edu/dining/varsitymarketplace.php");
		varsityBranscomb.create();
	}

	private void varsityTowersData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(7, 0);
		Time stop = new Time(7, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "Located in the basement of Carmichael Towers East, Varsity Towers features grocery items, a variety of bottled beverages, dairy products, sandwiches, salads, fresh fruits, and more. And Varsity Towers is a VU Meal Plan location open 24/7.";

		Restaurant varsityTowers = new Restaurant("Varsity Market - Towers",
				rh, false, (int) (36.147903 * 1E6), (int) (-86.805857 * 1E6),
				"Munchie Mart", null, description, R.drawable.towers, true,
				true, false, null,
				"http://www.vanderbilt.edu/dining/varsitytowers.php");
		varsityTowers.create();
	}

	private void varsityMorganData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(10, 0);
		Time stop = new Time(1, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "Bringing convenience to Area VI, Varsity Morgan offers grocery items, beverages, dairy products, and sandwiches, salads, and fresh fruits. VU Meal Plans may be used at Varsity Morgan.";

		Restaurant varsityMorgan = new Restaurant("Varsity Market - Morgan",
				rh, false, (int) (36.140609 * 1E6), (int) (-86.806328 * 1E6),
				"Munchie Mart", null, description, R.drawable.morgan, true,
				true, false, null,
				"http://www.vanderbilt.edu/dining/varsitymorgan.php");
		varsityMorgan.create();
	}

	private void varsitySarrattData() {
		RestaurantHours rh = new RestaurantHours();
		// Restaurant is closed all day on Saturday and Sunday

		Time start = new Time(8, 0);
		Time stop = new Time(16, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(16, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(16, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(16, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(16, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		String description = "This mini-market in Sarratt Student Center features grab-n-go items, snacks, beverages, sandwiches, salads, fresh fruits, plus a whole lot more. And Jr. Varsity Market is a VU Meal Plan location.";

		Restaurant varsitySarratt = new Restaurant(
				"Jr. Varsity Market - Sarratt", rh, false,
				(int) (36.146587 * 1E6), (int) (-86.803428 * 1E6),
				"Munchie Mart", null, description, R.drawable.jr, true, true,
				false, null,
				"http://www.vanderbilt.edu/dining/jrvarsitymarket.php");
		varsitySarratt.create();
	}

	private void hemingwayMarketData() {
		RestaurantHours rh = new RestaurantHours();
		// TODO Implement a way to show that a restaurant is open beyond the
		// current day
		// Strange situation because restaurant opens at 10:00 AM on Sunday then
		// is open
		// 24 hours a day until it closes on on Saturday at 11:00 PM
		Time start = new Time(10, 0);
		Time stop = new Time(10, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(7, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		String description = "A Market serving Kissam Quad, Hemingway Market is located inside Hemingway Hall.";

		Restaurant hemingwayMarket = new Restaurant("Hemingway Market", rh,
				false, (int) (36.149829 * 1E6), (int) (-86.80124 * 1E6),
				"Munchie Mart", null, description, R.drawable.hemingway, true,
				true, false, null,
				"http://www.vanderbilt.edu/dining/hemingwaymarket.php");
		hemingwayMarket.create();
	}

	/*
	 * Taste of Nashville Restaurants
	 */
	private void bestWokData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(12, 0);
		Time stop = new Time(22, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(10, 45);
		stop = new Time(22, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 45);
		stop = new Time(22, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 45);
		stop = new Time(22, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 45);
		stop = new Time(22, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(10, 45);
		stop = new Time(23, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		Restaurant bestWok = new Restaurant("Best Wok", rh, false,
				(int) (36.158751 * 1E6), (int) (-86.818717 * 1E6), "Chinese",
				null, null, R.drawable.tasteofnashville, true, false, true, "(615) 341-0188",
				null);
		bestWok.create();
	}

	private void breadAndCoData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(8, 0);
		Time stop = new Time(16, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(20, 00);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(20, 00);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(20, 00);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(20, 00);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(20, 00);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(18, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));


		Restaurant breadAndCo = new Restaurant("Bread and Company", rh, false,
				(int) (36.145872 * 1E6), (int) (-86.80846 * 1E6), "Sandwiches",
				null, null, R.drawable.breadandco, true, false, true, "(615) 329-1400",
				"http://www.breadandcompany.com/");
		breadAndCo.create();
	}

	private void brueggersBagelData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(7, 0);
		Time stop = new Time(17, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(6, 0);
		stop = new Time(18, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(6, 0);
		stop = new Time(18, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(6, 0);
		stop = new Time(18, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(6, 0);
		stop = new Time(18, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(6, 0);
		stop = new Time(18, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(17, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant brueggersBagel = new Restaurant("Bruegger's Bagels", rh, false,
				(int) (36.148542 * 1E6), (int) (-86.799112 * 1E6), "Sandwiches",
				null, null, R.drawable.brueggers, true, false, true, "(615) 327-0055",
				"http://www.brueggers.com/");
		brueggersBagel.create();
	}

	private void cabanaData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(16, 0);
		Time stop = new Time(0, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(16, 0);
		stop = new Time(3, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(16, 0);
		stop = new Time(3, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(16, 0);
		stop = new Time(3, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(16, 0);
		stop = new Time(3, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(16, 0);
		stop = new Time(3, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(16, 0);
		stop = new Time(3, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant cabana = new Restaurant("Cabana", rh, false,
				(int) (36.137065 * 1E6), (int) (-86.799215 * 1E6), "Fine Dining",
				null, null, R.drawable.cabana, true, false, true, "(615) 577-2262",
				"http://www.cabananashville.com/");
		cabana.create();
	}

	private void cafeCocoData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(7, 0);
		Time stop = new Time(6, 59);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(6, 59);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(6, 59);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(6, 59);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(7, 0);
		stop = new Time(6, 59);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));
		
		Restaurant cafeCoco = new Restaurant("Cafe Coco", rh, false,
				(int) (36.151832 * 1E6), (int) (-86.805135 * 1E6), "Cafe",
				null, null, R.drawable.cafecoco, true, false, true, "(615) 321-2626",
				"http://www.cafecoco.com/");
		cafeCoco.create();
	}

	private void cheeseburgerCharleysData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(11, 0);
		Time stop = new Time(20, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant cheeseburgerCharleys = new Restaurant("Cheeseburger Charley's", rh, false,
				(int) (36.147172 * 1E6), (int) (-86.798999 * 1E6), "American",
				null, null, R.drawable.cheeseburgercharleys, true, false, true, "(615) 327-0220",
				"http://www.cheeseburgercharleys.com/");
		cheeseburgerCharleys.create();
	}

	private void chilisData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(11, 0);
		Time stop = new Time(23, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(23, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant chilis = new Restaurant("Chili's", rh, false,
				(int) (36.148266 * 1E6), (int) (-86.806459 * 1E6), "Tex Mex",
				null, null, R.drawable.chilis, true, false, true, "(615) 327-1588",
				"http://www.chilis.com/");
		chilis.create();
	}

	private void medCuisineData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(11, 0);
		Time stop = new Time(21, 30);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(21, 30);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant medCuisine = new Restaurant("Mediterranean Cuisine", rh, false,
				(int) (36.147172 * 1E6), (int) (-86.798999 * 1E6), "Mediterranean",
				null, null, R.drawable.medcuisine, true, false, true, "(615) 321-8960",
				null);
		medCuisine.create();
	}

	private void michaelangelosPizzaData() {
		RestaurantHours rh = new RestaurantHours();
		
		Time start = new Time(14, 0);
		Time stop = new Time(20, 30);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(0, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(0, 30);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(0, 30);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(2, 30);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(2, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(2, 30);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		Restaurant michaelangelosPizza = new Restaurant("Michaelangelo's Pizza", rh, false,
				(int) (36.151744 * 1E6), (int) (-86.803774 * 1E6), "Pizza",
				null, null, R.drawable.michaelangelos, true, false, true, "(615) 329-2979",
				"http://www.michaelangelos-pizza.com/");
		michaelangelosPizza.create();
	}

	private void noshvilleData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(7, 30);
		Time stop = new Time(21, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(14, 30);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(21, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(6, 30);
		stop = new Time(22, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(7, 30);
		stop = new Time(22, 30);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant noshville = new Restaurant("Noshville", rh, false,
				(int) (36.150167 * 1E6), (int) (-86.797837 * 1E6), "Deli",
				null, null, R.drawable.noshville, true, false, true, "(615) 329-6674",
				"http://www.noshville.com/");
		noshville.create();
	}

	private void obiesPizzaData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(10, 0);
		Time stop = new Time(1, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(4, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant obiesPizza = new Restaurant("Obie's Flying Tomato Pizza", rh, false,
				(int) (36.1513 * 1E6), (int) (-86.804124 * 1E6), "Pizza",
				null, null, R.drawable.tasteofnashville, true, false, true, "(615) 327-4772",
				null);
		obiesPizza.create();
	}

	private void papaJohnsData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(9, 0);
		Time stop = new Time(1, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(9, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(9, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(9, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(9, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(9, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(9, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant papaJohns = new Restaurant("Papa John's", rh, false,
				(int) (36.148439 * 1E6), (int) (-86.806092 * 1E6), "Pizza",
				null, null, R.drawable.papajohns, true, false, true, "(615) 321-4000",
				"http://www.papajohns.com/");
		papaJohns.create();
	}

	private void qdobaData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(6, 0);
		Time stop = new Time(22, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(21, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(6, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(6, 0);
		stop = new Time(5, 59);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(6, 0);
		stop = new Time(5, 59);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant qdoba = new Restaurant("Qdoba", rh, false,
				(int) (36.150282 * 1E6), (int) (-86.800717 * 1E6), "Mexican",
				null, null, R.drawable.qdoba, true, false, true, "(615) 340-9039",
				"http://www.qdoba.com/");
		qdoba.create();
	}

	private void rolyPolyData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(10, 0);
		Time stop = new Time(19, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(19, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(16, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant rolyPoly = new Restaurant("Roly Poly Sandwiches", rh, false,
				(int) (36.139484 * 1E6), (int) (-86.800472 * 1E6), "Sandwiches",
				null, null, R.drawable.rolypoly, true, false, true, "(615) 255-4600",
				"http://www.rolypoly.com/");
		rolyPoly.create();
	}

	private void romaPizzaData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(10, 0);
		Time stop = new Time(5, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(5, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(5, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(5, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(5, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(5, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(10, 0);
		stop = new Time(5, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant romaPizza = new Restaurant("Roma Pizza and Pasta", rh, false,
				(int) (36.148327 * 1E6), (int) (-86.807474 * 1E6), "Pizza",
				null, null, R.drawable.roma, true, false, true, "(615) 340-0040",
				"http://www.romapizzaandpasta.com/");
		romaPizza.create();
	}

	private void samsSportsBarData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(11, 0);
		Time stop = new Time(2, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant samsSportsBar = new Restaurant("Sam's Sports Bar & Grill", rh, false,
				(int) (36.136348 * 1E6), (int) (-86.801419 * 1E6), "Sports Bar",
				null, null, R.drawable.sams, true, false, true, "(615) 383-3601",
				"http://www.samssportsgrill.com/");
		samsSportsBar.create();
	}

	private void satayThaiData() {
		RestaurantHours rh = new RestaurantHours();
		//TODO Satay Thai Grill needs actual hours
		Time start = new Time(11, 0);
		Time stop = new Time(2, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(2, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant satayThai = new Restaurant("Satay Thai Grill", rh, false,
				(int) (36.148368 * 1E6), (int) (-86.807427 * 1E6), "Thai",
				null, null, R.drawable.sataythai, true, false, true, "(615) 915-0972",
				"http://www.sataynashville.com/");
		satayThai.create();
	}

	private void schlotzskysData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(11, 0);
		Time stop = new Time(21, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(22, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(22, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(22, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(22, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(10, 30);
		stop = new Time(22, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(22, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant schlotzskys = new Restaurant("Schlotzsky's", rh, false,
				(int) (36.147596 * 1E6), (int) (-86.807077 * 1E6), "Sandwiches",
				null, null, R.drawable.schlotzskys, true, false, true, "(615) 320-9777",
				"http://www.schlotzskys.com/");
		schlotzskys.create();
	}

	private void smoothieKingTwentyFirstData() {
		RestaurantHours rh = new RestaurantHours();
		//TODO Check to see if Smoothie King hours are the same for 21st as Elliston
		Time start = new Time(10, 0);
		Time stop = new Time(20, 30);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(9, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));

		Restaurant smoothieKingTwentyFirst = new Restaurant("Smoothie King - 21st Avenue", rh, false,
				(int) (36.136823 * 1E6), (int) (-86.800831 * 1E6), "Smoothies",
				null, null, R.drawable.smoothieking, true, false, true, "(615) 463-8415",
				"http://www.smoothieking.com/");
		smoothieKingTwentyFirst.create();
	}

	private void smoothieKingEllistonData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(10, 0);
		Time stop = new Time(20, 30);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(8, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(9, 0);
		stop = new Time(21, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant smoothieKingElliston = new Restaurant("Smoothie King - Elliston Pl.", rh, false,
				(int) (36.148299 * 1E6), (int) (-86.807506 * 1E6), "Smoothies",
				null, null, R.drawable.smoothieking, true, false, true, "(615) 321-4909",
				"http://www.smoothieking.com/");
		smoothieKingElliston.create();
	}

	private void sunsetGrillData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(16, 45);
		Time stop = new Time(23, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(16, 45);
		stop = new Time(0, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(1, 30);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(16, 45);
		stop = new Time(1, 30);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant sunsetGrill = new Restaurant("Sunset Grill", rh, false,
				(int) (36.136725 * 1E6), (int) (-86.799483 * 1E6), "Fine Dining",
				null, null, R.drawable.sunsetgrill, true, false, true, "(615) 386-3663",
				"http://www.sunsetgrill.com/");
		sunsetGrill.create();
	}

	private void tgiFridaysData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(11, 0);
		Time stop = new Time(0, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(0, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(11, 0);
		stop = new Time(1, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant tgiFridays = new Restaurant("TGI Friday's", rh, false,
				(int) (36.135898 * 1E6), (int) (-86.822827 * 1E6), "American",
				null, null, R.drawable.fridays, true, false, true, "(615) 327-8071",
				"http://www.fridays.com/");
		tgiFridays.create();
	}

	private void wendysTwentyFirstData() {
		RestaurantHours rh = new RestaurantHours();
		//TODO Add Hours of Operations
		Restaurant wendysTwentyFirst = new Restaurant("Wendy's - 21st Avenue", rh, false,
				(int) (36.14859 * 1E6), (int) (-86.799095 * 1E6), "Fast Food",
				null, null, R.drawable.wendys, true, false, true, "(615) 321-9763",
				"http://www.wendys.com/");
		wendysTwentyFirst.create();
	}

	private void wendysWestEndData() {
		RestaurantHours rh = new RestaurantHours();
		//TODO Add Hours of Operations
		Restaurant wendysWestEnd = new Restaurant("Wendy's - West End", rh, false,
				(int) (36.135841 * 1E6), (int) (-86.82291 * 1E6), "Fast Food",
				null, null, R.drawable.wendys, true, false, true, "(615) 327-4930",
				"http://www.wendys.com/");
		wendysWestEnd.create();
	}

	private void yogurtOasisData() {
		RestaurantHours rh = new RestaurantHours();

		Time start = new Time(12, 0);
		Time stop = new Time(21, 0);
		rh.addRange(Calendar.SUNDAY, new Range(start, stop));

		start = new Time(12, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.MONDAY, new Range(start, stop));

		start = new Time(12, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.TUESDAY, new Range(start, stop));

		start = new Time(12, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.WEDNESDAY, new Range(start, stop));

		start = new Time(12, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.THURSDAY, new Range(start, stop));

		start = new Time(12, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.FRIDAY, new Range(start, stop));

		start = new Time(12, 0);
		stop = new Time(20, 0);
		rh.addRange(Calendar.SATURDAY, new Range(start, stop));
		
		Restaurant yogurtOasis = new Restaurant("Yogurt Oasis", rh, false,
				(int) (36.147596 * 1E6), (int) (-86.807077 * 1E6), "Yogurt",
				null, null, R.drawable.yogurtoasis, true, false, true, "(615) 963-3631",
				null);
		yogurtOasis.create();
	}
}
