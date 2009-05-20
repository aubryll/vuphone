package edu.vanderbilt.isis.wreckwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Starter extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		super.startService(new Intent(this, edu.vanderbilt.isis.wreckwatch.ServiceTest.class));
		//super.finish();
	}

}
