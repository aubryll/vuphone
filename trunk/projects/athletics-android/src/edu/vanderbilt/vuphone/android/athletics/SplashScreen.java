package edu.vanderbilt.vuphone.android.athletics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;

public class SplashScreen extends Activity {

	// ===========================================================
	// Fields
	// ===========================================================

	private final int SPLASH_DISPLAY_LENGTH = 200;
        private final String APP_INITIALISED_FILENAME = "app_initialised";

	// ===========================================================
	// "Constructors"
	// ===========================================================

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.splash_screen);

		/*
		 * We need to check if the app has been run before.
		 * If not, we want to build the database schema.
		 * If so, we just skip ahead to the menu.
		 * To do this, we check for a specific preference; if it exists,
		 * the app has been initialized already. Otherwise, we
		 * initialize the app and create the preference.
		 */
		
		SharedPreferences settings = this.getPreferences(MODE_PRIVATE);
		if (!settings.contains("app_is_initialized")) {
		    System.out.println("App is not yet initialized.");
		    
		    SharedPreferences.Editor prefs = settings.edit();
		    prefs.putBoolean("app_is_initialized", true);
		    prefs.commit();
		} else {
		    System.out.println("App is already initialized.");
		}

		/*
		 * New Handler to start the Menu-Activity and close this Splash-Screen
		 * after some seconds.
		 */
			new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// Create an Intent that will start the Main-Activity. 
				Intent mainIntent = new Intent(SplashScreen.this, Main.class);
				SplashScreen.this.startActivity(mainIntent);
				SplashScreen.this.finish();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}
}