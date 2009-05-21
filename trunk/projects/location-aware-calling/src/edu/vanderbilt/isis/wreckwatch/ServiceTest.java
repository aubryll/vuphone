package edu.vanderbilt.isis.wreckwatch;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServiceTest extends Service implements LocationListener{

	public void onCreate(){
		super.onCreate();
		message("Service.onCreate()");
		
		LocationManager lMan = (LocationManager) super.getSystemService(Context.LOCATION_SERVICE);
		lMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
		(new Timer()).schedule(new TimerTask(){
			public void run(){
				ServiceTest.this.stopSelf();
			}
		}, 5000);
		
		
		/*
		// Check if accelerometer is available
		
		// The next call crashes the app without throwing an exception
		SensorManager sMan = (SensorManager) super.getSystemService(Context.SENSOR_SERVICE);

		
		if (sMan == null){
			message("Null manager", true);
			return;
		}else
			message("Good manager", true);
		
		this.stopSelf();
		*/
	}
	
	public void onDestroy(){
		message("Service.onDestroy()");
		super.onDestroy();
	}
	
	public IBinder onBind(Intent intent){

		message("Service.onBind(Intent)");
		return null;
	}

	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
		message("Service.onStart()");
	}
	
	public void message(String str){
		message(str, false);
	}
	
	public void message(String str, boolean isLong){
		Log.v("VUPHONE", str);
		int duration = isLong ? Toast.LENGTH_LONG: Toast.LENGTH_SHORT;
		
		Toast.makeText(this, str, duration).show();
	}
	
	
    public void onLocationChanged(Location loc){
    	message("New location received: " + loc.getLongitude() + ", " + loc.getLatitude(), true);
    }

    public void onProviderDisabled(String provider){}
    public void onProviderEnabled(String provider){}
    public void onStatusChanged(String provider, int status, Bundle extras){}

	
}
